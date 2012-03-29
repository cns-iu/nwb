package edu.iu.nwb.converter.prefusebibtex;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Iterator;
import java.util.Map;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.utilities.UnicodeReader;
import org.osgi.service.log.LogService;

import prefuse.data.DataTypeException;
import prefuse.data.Schema;
import prefuse.data.Table;
import prefuse.data.column.Column;
import prefuse.util.collections.IntIterator;
import bibtex.dom.BibtexAbstractEntry;
import bibtex.dom.BibtexAbstractValue;
import bibtex.dom.BibtexEntry;
import bibtex.dom.BibtexFile;
import bibtex.expansions.ExpansionException;
import bibtex.expansions.MacroReferenceExpander;
import bibtex.parser.BibtexParser;
import bibtex.parser.ParseException;
import edu.iu.nwb.converter.prefusebibtex.util.StringUtil;

public class BibtexReaderAlgorithm implements Algorithm {
	//TODO: what if they have fields with these names?
	public static final String ENTRY_TYPE_KEY = "entry type";
	public static final String ENTRY_KEY_KEY = "entry key";
	
	public static final String AUTHOR_COLUMN_NAME = "author";
	public static final String ORIG_AUTHOR_COLUMN_SEPARATOR = " and ";
	public static final String NEW_AUTHOR_COLUMN_SEPARATOR = "|";
		
    private LogService log;
    
    private BibtexValueFormatter valueFormatter;
	private File inBibtexFile;
    
    public BibtexReaderAlgorithm(
    		Data[] data, Dictionary parameters, CIShellContext context) {
    	this.inBibtexFile = (File) data[0].getData();
    	
        this.log = (LogService) context.getService(LogService.class.getName());
        
        this.valueFormatter = new BibtexValueFormatter(log);		
    }

    public Data[] execute() throws AlgorithmExecutionException {
    	String bibtexFilePath = inBibtexFile.getAbsolutePath();

    	BibtexFile parsedBibtex = parseBibtex(bibtexFilePath);
    	// Write parsed bibtex File to table
    	Table bibtexTable = makeTable(parsedBibtex);
    	// Normalize author names
    	bibtexTable = normalizeAuthorNames(bibtexTable);
    	// Return bibtex table data
    	Data[] bibtexReturnData = createOutData(bibtexTable, bibtexFilePath);
    	
		return bibtexReturnData;
    }
    
    private Data[] createOutData(Table bibtex, String bibtexFilePath) {
    	Data[] tableToReturnData = 
			new Data[] {new BasicData(bibtex, Table.class.getName())};
		tableToReturnData[0].getMetadata().put(
				DataProperty.LABEL, "Parsed BibTeX file: " + bibtexFilePath);
        tableToReturnData[0].getMetadata().put(
        		DataProperty.TYPE, DataProperty.TABLE_TYPE);
        return tableToReturnData;
    }
    
    private Table makeTable(BibtexFile bibtex) throws AlgorithmExecutionException {
    	TableData table = createDefaultEmptyTable();
    	
    	for (Iterator entryIt = bibtex.getEntries().iterator(); entryIt.hasNext();) {
    		BibtexAbstractEntry abstractEntry = (BibtexAbstractEntry) entryIt.next();
    		if (abstractEntry instanceof BibtexEntry) {
    			BibtexEntry entry = (BibtexEntry) abstractEntry;
    			
    			table.moveOnToNextRow();
    			table.setString(ENTRY_TYPE_KEY, entry.getEntryType());
    			table.setString(ENTRY_KEY_KEY, entry.getEntryKey());
   
    			Map entryFields = entry.getFields();
    			for (Iterator fieldIt = entryFields.keySet().iterator(); fieldIt.hasNext();) {
    			
    				String fieldKey = (String) fieldIt.next();
    				BibtexAbstractValue fieldVal = (BibtexAbstractValue) entryFields.get(fieldKey);
    				table.setString(fieldKey, this.valueFormatter.formatFieldValue(fieldVal));
    			}
    		} else {
    			//ignore other weird entry types, like macros (which we should be removing anyway).
    		}
    	}
    	
    	return table.getPrefuseTable();
    }
    
    private BibtexFile parseBibtex(String bibtexFilePath)
    		throws AlgorithmExecutionException {
    	BibtexFile bibtexFile = new BibtexFile();
    	BibtexParser parser = new BibtexParser(false);
    	try {
    		parser.parse(bibtexFile, new UnicodeReader(new FileInputStream(bibtexFilePath)));
    	} catch (FileNotFoundException e) {
			throw new AlgorithmExecutionException(e.getMessage(), e);
		} catch (ParseException e) {
			throw new AlgorithmExecutionException(e.getMessage(), e);
		} catch (IOException e) {
			throw new AlgorithmExecutionException(e.getMessage(), e);
		} finally {
    		printNonFatalExceptions(parser.getExceptions(),
    								bibtexFile.getEntries().size());
    	}
		
    	try {
	    	MacroReferenceExpander macroExpander = 
	    		new MacroReferenceExpander(true, true, false, false);
	    	macroExpander.expand(bibtexFile);
    	} catch (ExpansionException e) {
    		throw new AlgorithmExecutionException(
    				"Error occurred while parsing bibtex file.", e);
    	}
    	return bibtexFile;
    }
    
    private TableData createDefaultEmptyTable() {
    	int startingCapacity = 25;
    	Schema s = new Schema(startingCapacity);
    	
//for now, we add columns dynamically    	
//    	add(s, "address");
//    	add(s, "annote");
//    	add(s, "author");
//    	add(s, "booktitle");
//    	add(s, "chapter");
//    	add(s, "crossref");
//    	add(s, "edition");
//    	add(s, "editor");
//    	add(s, "eprint");
//    	add(s, "howpublished");
//    	add(s, "institution");
//    	add(s, "journal");
//    	add(s, "key");
//    	add(s, "month");
//    	add(s, "note");
//    	add(s, "number");
//    	add(s, "organization");
//    	add(s, "pages");
//    	add(s, "publisher");
//    	add(s, "school");
//    	add(s, "series");
//    	add(s, "title");
//    	add(s, "url");
//    	add(s, "volume");
//    	add(s, "year");
    	
    	return new TableData(s);
    }
    
    private void add(Schema schema, String column) {
    	schema.addColumn(column, String.class);
    }
    
    private Table normalizeAuthorNames(Table bibtexTable) {
    	Column authorColumn = bibtexTable.getColumn(AUTHOR_COLUMN_NAME);
    	if (authorColumn == null) {
    		printNoAuthorColumnWarning();
    		return bibtexTable;
    	}
    	try {
    		for (IntIterator tableIt = bibtexTable.rows(); tableIt.hasNext();) {
    			int rowIndex = tableIt.nextInt();
    		String authors = authorColumn.getString(rowIndex);
    		if (authors != null && ! authors.equals("")) {
    			String normalizedAuthors = normalizeAuthorNames(authors);
    			authorColumn.setString(normalizedAuthors, rowIndex);
    		}
    	}
    	} catch (DataTypeException e) {
    		printColumnNotOfTypeStringWarning(e);
    		return bibtexTable;
    	}
    	return bibtexTable;
    }
    
    private String normalizeAuthorNames(String authorNames) {
    	//trim leading and trailing whitespace from each author name.
    	String[] eachAuthorName = authorNames.split(ORIG_AUTHOR_COLUMN_SEPARATOR);
    	String normalizedAuthorNames = StringUtil.join(eachAuthorName, NEW_AUTHOR_COLUMN_SEPARATOR);
    	return normalizedAuthorNames;
    }
    
	private void printNonFatalExceptions(
			Exception[] exceptions, int numTotalEntries) {
		if (exceptions.length > 0) {
		
			log.log(LogService.LOG_WARNING, "Non-fatal exceptions:\n");
			
			for (int i = 0; i < exceptions.length; i++) {
				String message = exceptions[i].getMessage();
				if (message == null) {
					message = "";
				}
				log.log(LogService.LOG_WARNING, "  " + message, exceptions[i]);
			}
			float percentFlawed =
				((float) exceptions.length) / ((float) numTotalEntries);
			String percentFlawedAsString = String.valueOf(percentFlawed * 100);
			percentFlawedAsString = percentFlawedAsString.substring(0, 4);
			this.log.log(LogService.LOG_WARNING, "" + exceptions.length + 
					" non-fatal errors were found out of  " + numTotalEntries +
					" entries. Each will usually cause one or more fields to "
					+ "be lost for a single entry.");
		} else {
			this.log.log(LogService.LOG_INFO,
						 "File successfully parsed -- no issues arose.");
		}
	}
    
    private class TableData {
		private Table table;
		
		private int currentRow;
		private boolean currentRowIsFinished;
		
		public TableData(Schema schema) {
			table = schema.instantiate();
			currentRowIsFinished = true; //will cause first row to be created
		}
		
		public void moveOnToNextRow() { 
			currentRowIsFinished = true;
		}
		
		public void setString(String columnTag, String value)
				throws AlgorithmExecutionException {
			ensureRowNotFinishedYet();
			
			try {
				table.setString(currentRow, columnTag, value);
			} catch (Exception e1) {
				// Maybe column does not yet exist. Add it and try again.
				addColumn(columnTag, String.class);
				try {
					table.setString(currentRow, columnTag, value);
				} catch (Exception e2) {
					// Something else must be wrong.
					throw new AlgorithmExecutionException(e2);
				}
			}
		}
		
		public void addColumn(String columnName, Class columnType) {
			table.addColumn(columnName, columnType);
		}
		
		public Table getPrefuseTable() {
			return table;
		}
		
		private void ensureRowNotFinishedYet() {
			if (currentRowIsFinished) {
				currentRow = table.addRow();
				currentRowIsFinished = false;
			}
		}		
	}
    
    private void printColumnNotOfTypeStringWarning(DataTypeException e) {
    	this.log.log(LogService.LOG_WARNING, "The column '" + AUTHOR_COLUMN_NAME + 
    			"' in the bibtex file cannot be normalized, because it cannot be interpreted as text.", e);
    }
    
    private void printNoAuthorColumnWarning() {
    	this.log.log(LogService.LOG_WARNING, "Unable to find column with the name '" +
    			AUTHOR_COLUMN_NAME + "' in bibtex file. " +
    					"We will continue on without attempting to normalize this column");
    }
}