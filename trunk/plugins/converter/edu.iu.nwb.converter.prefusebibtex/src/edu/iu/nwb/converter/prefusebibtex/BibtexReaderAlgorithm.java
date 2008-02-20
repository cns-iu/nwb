package edu.iu.nwb.converter.prefusebibtex;

import java.io.File;
import java.io.FileReader;
import java.util.Dictionary;
import java.util.Iterator;
import java.util.Map;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

import prefuse.data.Schema;
import prefuse.data.Table;
import bibtex.dom.BibtexAbstractValue;
import bibtex.dom.BibtexEntry;
import bibtex.dom.BibtexFile;
import bibtex.expansions.ExpansionException;
import bibtex.expansions.MacroReferenceExpander;
import bibtex.parser.BibtexParser;

public class BibtexReaderAlgorithm implements Algorithm {
	private static final String ENTRY_TYPE_KEY = "ENTRY_TYPE";
	private static final String ENTRY_KEY_KEY = "ENTRY_KEY";
    Data[] data;
    Dictionary parameters;
    CIShellContext context;
    LogService log;
    
    public BibtexReaderAlgorithm(Data[] data, Dictionary parameters, CIShellContext context, LogService log) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
        this.log = log;
    }

    public Data[] execute() {
    	File bibtexFile = (File) data[0].getData();
    	String bibtexFilePath = bibtexFile.getAbsolutePath();
    	//parse bibtex File
    	BibtexFile parsedBibtex = parseBibtex(bibtexFilePath);
    	if (parsedBibtex == null) {
    		return null;
    	}
    	//write parsed bibtex File to table
    	Table bibtexTable = makeTable(parsedBibtex);
    	//return bibtex table data
    	Data[] bibtexReturnData = formatAsData(bibtexTable, bibtexFilePath);
		return bibtexReturnData;
    }
    
    private Data[] formatAsData(Table bibtex, String bibtexFilePath) {
    	Data[] tableToReturnData = 
			new Data[] {new BasicData(bibtex, Table.class.getName())};
		tableToReturnData[0].getMetaData().put(DataProperty.LABEL, "BibTeX file: " + bibtexFilePath);
		//TODO: should this really be a text_type?
        tableToReturnData[0].getMetaData().put(DataProperty.TYPE, DataProperty.TEXT_TYPE);
        return tableToReturnData;
    }
    
    private Table makeTable(BibtexFile bibtex) {
    	TableData table = createDefaultEmptyTable();
    	
    	for (Iterator entryIt = bibtex.getEntries().iterator(); entryIt.hasNext();) {
    		BibtexEntry entry = (BibtexEntry) entryIt.next();
    		
    		table.moveOnToNextRow();
    		table.setString(ENTRY_TYPE_KEY, entry.getEntryType());
    		table.setString(ENTRY_KEY_KEY, entry.getEntryKey());
   
    		Map entryFields = entry.getFields();
    		for (Iterator fieldIt = entryFields.keySet().iterator(); fieldIt.hasNext();) {
    			
    			String fieldKey = (String) fieldIt.next();
    			BibtexAbstractValue fieldVal = (BibtexAbstractValue) entryFields.get(fieldKey);
    			table.setString(fieldKey, fieldVal.toString());
    		}
    	}
    	
    	return table.getPrefuseTable();
    }
    
    private BibtexFile parseBibtex(String bibtexFilePath) {
    	BibtexFile bibtexFile = new BibtexFile();
    	BibtexParser parser = new BibtexParser(false);
    	try {
    	parser.parse(bibtexFile, new FileReader(bibtexFilePath));
    	} catch (Exception e) {
    		log.log(LogService.LOG_ERROR, "Fatal exception occurred while parsing bibtex file.", e);
    		return null;
    	}
    	try {
    	MacroReferenceExpander macroExpander = 
    		new MacroReferenceExpander(true, true, true, false);
    	macroExpander.expand(bibtexFile);
    	} catch (ExpansionException e) {
    		log.log(LogService.LOG_WARNING, "Error occurred while parsing bibtex file. Check command line for details.");
    		e.printStackTrace();
    		return null;
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
		
		public void setString(String columnTag, String value) {
			ensureRowNotFinishedYet();
			
			try {
			table.setString(currentRow, columnTag, value);
			} catch (Exception e1) {
				//maybe column does not yet exist. Add it and try again.
				addColumn(columnTag, String.class);
				try {
				table.setString(currentRow, columnTag, value);
				} catch (Exception e2) {
					//something else must be wrong.
					throw new Error(e2);
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
}