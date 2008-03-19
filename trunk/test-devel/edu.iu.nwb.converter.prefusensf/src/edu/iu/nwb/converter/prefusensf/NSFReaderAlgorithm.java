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

import javax.xml.stream.events.Characters;

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
		  Data[] outputTableData = formatAsData(data[0],table);
		  return outputTableData;
    }
    
	private Table normalizeCoPIs(Table nsfTable) {
		Column coPIColumn = nsfTable.getColumn(CO_PI_COLUMN_NAME);
		for (int rowIndex = 0; rowIndex < nsfTable.getRowCount(); rowIndex++) {
			String contents = (String) coPIColumn.getString(rowIndex);
			if (contents != null && (! contents.equals(""))) {
				String[] coPINames = contents.split("\\" + INPUT_NAME_SEPARATOR);
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
		for (int rowIndex = 0; rowIndex < nsfTable.getRowCount(); rowIndex++) {
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
		normalizedNSFTable.addColumn(ALL_PI_COLUMN_NAME, String.class);
		Column allPIColumn = normalizedNSFTable.getColumn(ALL_PI_COLUMN_NAME);
		Column primaryPIColumn = normalizedNSFTable.getColumn(PRIMARY_PI_COLUMN_NAME);
		Column coPIColumn = normalizedNSFTable.getColumn(CO_PI_COLUMN_NAME);
		for (int rowIndex = 0; rowIndex < normalizedNSFTable.getRowCount(); rowIndex++) {
			String primaryPI = primaryPIColumn.getString(rowIndex);
			String coPIs = coPIColumn.getString(rowIndex);
			String allPIs = null;
			if (primaryPI != null && coPIs != null) {
				allPIs = primaryPI + OUTPUT_NAME_SEPARATOR + coPIs;
			} else if (primaryPI == null && allPIs == null) {
				allPIs = "";
			} else if (primaryPI == null) {
				allPIs = coPIs;
			} else if (coPIs == null) {
				allPIs = primaryPI;
			}
			allPIColumn.setString(allPIs, rowIndex);
		}
//		normalizedNSFTable.addColumn(ALL_PI_COLUMN_NAME, 
//				"CONCAT_WS('" + OUTPUT_NAME_SEPARATOR + "',[" + PRIMARY_PI_COLUMN_NAME + "],[" + CO_PI_COLUMN_NAME + "])");
		return normalizedNSFTable;
	}
	
	private Data[] formatAsData(Data originalData, Table normalizedNSFTable) {
		try{
			Data[] dm = new Data[] {new BasicData(normalizedNSFTable, "prefuse.data.Table")};
			dm[0].getMetaData().put(DataProperty.LABEL, "Normalized NSF table");
			dm[0].getMetaData().put(DataProperty.TYPE, DataProperty.MATRIX_TYPE);
			dm[0].getMetaData().put(DataProperty.PARENT, originalData);
			return dm;
		}catch (SecurityException exception){
			log.log(LogService.LOG_ERROR, "SecurityException", exception);
			exception.printStackTrace();
			return null;
		}
	}
	
	private String normalizeCOPIName(String coPIName) {
		//remove all excessive whitespace between characters.
		String oneOrMoreWhitespaces = "[\\s]+";
		String normalizedCoPIName = coPIName.replaceAll(oneOrMoreWhitespaces, " ").trim();
		return normalizedCoPIName;
	}
	
	private String normalizePrimaryPIName(String primaryPIName) {
		//take everything after the last comma, remove it from the back, and stick it on front
		int lastCommaIndex = primaryPIName.lastIndexOf(",");
		if (lastCommaIndex == -1) {
			printUnexpectedPrimaryPINameWarning(primaryPIName);
			return primaryPIName;
		}
		
		String beforeComma = primaryPIName.substring(0, lastCommaIndex).trim();
		String afterComma = primaryPIName.substring(lastCommaIndex + 1).trim();
		
		String normalizedPrimaryPIName = afterComma + " " + beforeComma;
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
			//add separator to end of all but last token
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
		Data formatChangedData = new BasicData(inputData.getMetaData(), cleanNSFCSVFormat((File) inputData.getData()), "file:text/csv");
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
	
	private static final String AWARD_NUMBER_COLUMN_NAME = "Award Number";
	private static final int READ_AHEAD_LIMIT = 2;
	
	private File cleanNSFCSVFormat(File escapedQuoteFile) {
		BufferedReader in = null;
		BufferedWriter out = null;
		
		try {
	    	InputStream stream = new FileInputStream(escapedQuoteFile);
	    	/*
	    	 * UnicodeReader contains a hack for eating funny encoding characters that are 
	    	 * sometimes stuck onto the beginning of files (BOMs). Necessary
	    	 *  due to bug in standard reader.
	    	 */
	    	UnicodeReader unicodeReader = new UnicodeReader(stream, "UTF-8"); 
	    	in = new BufferedReader(unicodeReader);
	    	
	    	File outFile = File.createTempFile("cleanedNSFCSV", "csv");
	    	FileWriter fstream = new FileWriter(outFile);
	    	out = new BufferedWriter(fstream);
	    	
	    	String headerLine = in.readLine();
	    	if (headerLine != null) {
	    		if (hasTwoAwardNumberColumns(headerLine)) {
	    			headerLine = renameDuplicateAwardNumberColumn(headerLine);
	    		}
	    		out.write(headerLine + "\r\n");
	    	}
	    	
	    	int c;
	    	while ((c =  in.read()) != -1) {
	    		//if we see \". write "" instead
	    		if (c == '\\') {
	    			in.mark(READ_AHEAD_LIMIT);
	    			int nextC = in.read();
	    			if (nextC == '\"') {
	    				out.write('\"');
	    			}
	    			in.reset();
	    		} 
	    		//if we see \r then something other than \n, don't write the \r
	    		else if (c == '\r') {
	    			in.mark(READ_AHEAD_LIMIT);
	    			int nextC = in.read();
	    			if (nextC != '\n') {
	    				//don't write the \r
	    			} else {
	    				//write the \r
	    				out.write('\r');
	    			}
	    			in.reset();
	    		} else {
	    			out.write((char) c);
	    		}
	    	}
	    	
	    	return outFile;
	    	} catch (FileNotFoundException e1) {
	    		this.log.log(LogService.LOG_ERROR, "NSFReader could not find a file at " +  escapedQuoteFile.getAbsolutePath(), e1);
	    		return escapedQuoteFile;
	    	}  catch (IOException e2) {
	    		this.log.log(LogService.LOG_ERROR, "Unable to remove slash escaped quotes from nsf csv file due to IO Exception", e2);
	    		return escapedQuoteFile;
	    	} finally {
	    		try {
	    			if (in != null) {
	    				in.close();
	    			}
	    			if (out != null) {
	    				out.flush();
	    				out.close();
	    			}
	    		} catch (IOException e3) {
	    			this.log.log(LogService.LOG_ERROR, "Could not close file.", e3);
	    		}
	    	}
	}
	
	private boolean hasTwoAwardNumberColumns(String headerLine) {
		return headerLine.indexOf( AWARD_NUMBER_COLUMN_NAME) != headerLine.lastIndexOf( AWARD_NUMBER_COLUMN_NAME);
	}
	
	private String renameDuplicateAwardNumberColumn(String line) {
		return line.replaceAll( AWARD_NUMBER_COLUMN_NAME, AWARD_NUMBER_COLUMN_NAME + " Duplicate")
			.replaceFirst(AWARD_NUMBER_COLUMN_NAME + " Duplicate",  AWARD_NUMBER_COLUMN_NAME);
	}
}