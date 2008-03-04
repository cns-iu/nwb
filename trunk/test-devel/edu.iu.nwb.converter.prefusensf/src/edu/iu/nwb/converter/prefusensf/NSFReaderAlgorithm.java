package edu.iu.nwb.converter.prefusensf;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Dictionary;
import java.util.Iterator;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.service.conversion.DataConversionService;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import prefuse.data.Tuple;
import prefuse.data.column.Column;
import edu.iu.nwb.converter.prefusensf.util.UnicodeReader;

public class NSFReaderAlgorithm implements Algorithm {
    Data[] data;
    Dictionary parameters;
    CIShellContext context;
    LogService log;
    
    private static final String INPUT_NAME_SEPARATOR = "|";
    private static final String OUTPUT_NAME_SEPARATOR = "|";
    
	private static final String PRIMARY_PI_COLUMN_NAME = "Principal Investigator";
    private static final String CO_PI_COLUMN_NAME = "Co-PI Name(s)";
    private static final String ALL_PI_COLUMN_NAME = "All Investigators";
    
    public NSFReaderAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
        this.log = (LogService) context.getService(LogService.class.getName());
    }

    public Data[] execute() {
		  Data inputTableData = convertInputData(data[0]);
		  Table table = copyTable((Table) inputTableData.getData());
		  //normalize co-pis
		  table = normalizeCoPIs(table); if (table == null) return null;
		  //normalize primary pi
		  table = normalizePrimaryPIs(table); if (table == null) return null;
		  //make co-pi/primary pi joined column
		  table = addPIColumn(table); if (table == null) return null;
		  //format as data
		  Data[] outputTableData = formatAsData(table);
		  return outputTableData;
    }
    
	private Table normalizeCoPIs(Table nsfTable) {
		Column coPIColumn = nsfTable.getColumn(CO_PI_COLUMN_NAME);
		for (int rowIndex = 0; rowIndex < nsfTable.getMaximumRow(); rowIndex++) {
			String contents = (String) coPIColumn.getString(rowIndex);
			if (contents != null && (! contents.equals(""))) {
				String[] coPINames = contents.split(INPUT_NAME_SEPARATOR);
				String[] normalizedCOPINames = new String[coPINames.length];
				for (int coPIIndex = 0; coPIIndex < coPINames.length; coPIIndex++) {
					String coPIName = coPINames[coPIIndex];
					String normalizedName = normalizeCOPIName(coPIName);
					normalizedCOPINames[coPIIndex] = normalizedName;
				}
				String normalizedContents = join(normalizedCOPINames, OUTPUT_NAME_SEPARATOR);
				coPIColumn.setString(normalizedContents, rowIndex);
			}
		}
		return nsfTable;
	}
	

	
	private Table normalizePrimaryPIs(Table nsfTable) {
		Column coPIColumn = nsfTable.getColumn(PRIMARY_PI_COLUMN_NAME);
		for (int rowIndex = 0; rowIndex < nsfTable.getMaximumRow(); rowIndex++) {
			String primaryPIName = (String) coPIColumn.getString(rowIndex);
			if (primaryPIName != null  && (! primaryPIName.equals(""))) {
				String normalizedPIName = normalizePrimaryPIName(primaryPIName);
				coPIColumn.setString(normalizedPIName, rowIndex);
			}
		}
		return nsfTable;
	}
	
	private Table addPIColumn(Table normalizedNSFTable) {
		//add extra column made up of primary pi name + OUTPUT_NAME_SEPARATOR + all the co-pi names
		normalizedNSFTable.addColumn(ALL_PI_COLUMN_NAME, 
				"CONCAT_WS('" + OUTPUT_NAME_SEPARATOR + "',[" + PRIMARY_PI_COLUMN_NAME + "],[" + CO_PI_COLUMN_NAME + "])");
		return normalizedNSFTable;
	}
	
	private Data[] formatAsData(Table normalizedNSFTable) {
		try{
			Data[] dm = new Data[] {new BasicData(normalizedNSFTable, "prefuse.data.Table")};
			dm[0].getMetaData().put(DataProperty.LABEL, "Normalized NSF table");
			dm[0].getMetaData().put(DataProperty.TYPE, DataProperty.TEXT_TYPE);
			return dm;
		}catch (SecurityException exception){
			log.log(LogService.LOG_ERROR, "SecurityException", exception);
			exception.printStackTrace();
			return null;
		}
	}
	
	private String normalizeCOPIName(String coPIName) {
		//remove all excessive whitespace between characters, then reform with a space between each token.
		String anyWhitespace = "\\s";
		String[] nameTokens = coPIName.split(anyWhitespace);
		String normalizedCoPIName = join(nameTokens, " ");
		return normalizedCoPIName;
	}
	
	private String normalizePrimaryPIName(String primaryPIName) {
		//take everything after the last comma, remove it from the back, and stick it on front
		int lastCommaIndex = primaryPIName.lastIndexOf(",");
		if (lastCommaIndex == -1) {
			printUnexpectedPrimaryPINameWarning(primaryPIName);
			return primaryPIName;
		}
		
		String beforeComma = primaryPIName.substring(0, lastCommaIndex);
		String afterComma = primaryPIName.substring(lastCommaIndex + 1);
		
		String normalizedPrimaryPIName = beforeComma + afterComma;
		return normalizedPrimaryPIName;
	}
	
    
	private Table copyTable(Table t) {
		Table tCopy = new Table();
		tCopy.addColumns(t.getSchema());
		
		for (Iterator ii = t.tuples(); ii.hasNext();) {
			Tuple tuple = (Tuple) ii.next();
			tCopy.addTuple(tuple);
		}
		return tCopy;
	}
	
	private String join(String[] tokens, String separator) {
		StringBuilder joinedTokens = new StringBuilder();
		for (int i = 0; i < tokens.length; i++) {
			joinedTokens.append(tokens[i]);
			//add comma to end of all but last token
			if (i < tokens.length - 1) {
				joinedTokens.append(separator);
			}
		}
		return joinedTokens.toString();
	}
	
	private void printUnexpectedPrimaryPINameWarning(String primaryPIName) {
		this.log.log(LogService.LOG_WARNING, "Expected to find a comma separating last name" +
				" from first name in the primary investigator name '" + primaryPIName + "'. \r\n " + 
				" We will not normalize this name, and will instead leave it as it is.");
	}
	
	private Data convertInputData(Data inputData) {
		 DataConversionService converter = (DataConversionService)
         context.getService(DataConversionService.class.getName());
		//this is a bit like a cast. We know the nsf format is also a csv, so we change the format to csv so
		//the Conversion service knows it is a csv when it tries to convert it to a prefuse.data.Table
		 
		//printTable((Table) inputData.getData());
		Data formatChangedData = new BasicData(inputData.getMetaData(), changeEscapedQuotes((File) inputData.getData()), "file:text/csv");
		Data convertedData = converter.convert(formatChangedData, "prefuse.data.Table");
		return convertedData;
	}
	
	private void printTable(Table t) {
		Iterator ii = t.tuples();
		while (ii.hasNext()) {
			System.out.println((Tuple) ii.next());
		}
	}
	
	private void printFileContents(File file) {
		try {
	    	InputStream stream = new FileInputStream(file);
	    	/*
	    	 * UnicodeReader contains a hack for eating funny encoding characters that are 
	    	 * sometimes stuck onto the beginning of files. Necessary
	    	 *  due to bug in standard reader.
	    	 */
	    	UnicodeReader unicodeReader = new UnicodeReader(stream, "UTF-8"); 
	    	BufferedReader reader = new BufferedReader(unicodeReader);
	    	String line = null;
	    	while ((line =reader.readLine()) != null) {
	    		System.out.println(line);
	    	}
	    	} catch (FileNotFoundException e1) {
	    		this.log.log(LogService.LOG_WARNING, "ReferReader could not find a file at " + file.getAbsolutePath());
	    	}  catch (IOException e2) {
	    		this.log.log(LogService.LOG_WARNING, "Unable to print file contents due to IO Exception.");
	    	}
	}
	
	private File changeEscapedQuotes(File escapedQuoteFile) {
		//change \" style quote escapes to "" style quote escapes in file
		try {
	    	InputStream stream = new FileInputStream(escapedQuoteFile);
	    	/*
	    	 * UnicodeReader contains a hack for eating funny encoding characters that are 
	    	 * sometimes stuck onto the beginning of files. Necessary
	    	 *  due to bug in standard reader.
	    	 */
	    	UnicodeReader unicodeReader = new UnicodeReader(stream, "UTF-8"); 
	    	BufferedReader reader = new BufferedReader(unicodeReader);
	    	
	    	File outFile = File.createTempFile("quotesAlteredCSV", "csv");
	    	FileWriter fstream = new FileWriter(outFile);
	    	BufferedWriter out = new BufferedWriter(fstream);
	    	String line = null;
	    	while ((line = reader.readLine()) != null) {
	    		String slashAndQuote = "\\\"";
	    		String twoQuotes = "\"\"";
	    		String newLine = line.replace(slashAndQuote, twoQuotes);
	    		writeLine(out, newLine);
	    	}
	    	return outFile;
	    	} catch (FileNotFoundException e1) {
	    		this.log.log(LogService.LOG_ERROR, "NSFReader could not find a file at " +  escapedQuoteFile.getAbsolutePath(), e1);
	    		return escapedQuoteFile;
	    	}  catch (IOException e2) {
	    		this.log.log(LogService.LOG_ERROR, "Unable to remove slash escaped quotes from nsf csv file due to IO Exception", e2);
	    		return escapedQuoteFile;
	    	}
	}
	
	private void writeLine(BufferedWriter out, String line) throws IOException {
        out.write(line, 0, line.length());
        out.newLine();
    }
}