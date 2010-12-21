package edu.iu.nwb.converter.prefuserefer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Map;

import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.utilities.UnicodeReader;
import org.osgi.service.log.LogService;

import prefuse.data.Schema;
import prefuse.data.Table;
import edu.iu.nwb.converter.prefuserefer.util.TableData;

public class ReferUtil {
	
	private LogService log;
	
	public ReferUtil(LogService log) {
		this.log = log;
	}
	
	public BufferedReader makeReader(File file)
			throws AlgorithmExecutionException {
    	try {
	    	InputStream stream = new FileInputStream(file);
	    	/*
	    	 * UnicodeReader contains a hack for eating funny encoding characters that are 
	    	 * sometimes stuck onto the beginning of files. Necessary
	    	 *  due to bug in standard reader.
	    	 */
	    	return new BufferedReader(new UnicodeReader(stream));
    	} catch (FileNotFoundException e) {
    		throw new AlgorithmExecutionException(
    				"ReferReader could not find a file at "
    				+ file.getAbsolutePath(), e);
    	} 
    }
	
	public boolean startsWithFieldMarker(String line) {
    	return line.startsWith("%");
    }
    
    public boolean startsThisFieldType(String line, String fieldType) {
    	//after %, the line starts with fieldType
    	return line.startsWith(fieldType, 1);
    }
    
    public boolean isBlank(String line) {
    	String isEntirelyWhitespace = "^[\\s]*$";
    	return line.length() == 0 || line.matches(isEntirelyWhitespace);
    }
    
    public boolean isEndOfFile(String line) {
    	return line == null;
    }
    
    public TableData createEmptyTable() {
    			Map columnNameMap = createColumnNameMap();
    	    	Schema s = new Schema();
    	    	return new TableData(s, columnNameMap);
    }
    
    public String extractFieldName(String line) {
    	//extract second character
    	return line.substring(1, 2);
    	//example: "%A Allen Ginsberg"
    	//result is:  "A"
    }
    
    public String extractContentsFromFirstLineOfField(String line) {
    	//extract everything beyond 3rd character
    	return line.substring(3);
    	//example: "%A Allen Ginsberg"
    	//result is:  "Allen Ginsberg"
    }
    
    public String extractContentsFromLine(String line) {
    	//returns exact same line (may want to change this later)
    	return line;
    }
    
    public String getBlankLine() {
    	return "\r\n";
    }
    
    public void commitFieldToRecord(
    		TableData table, String field, String fieldContents)
    			throws AlgorithmExecutionException {
    	// Will create the column if it does not already exist
    	table.setString(field, fieldContents);
    }
    
    public void commitRecordToTable(TableData table) {
    	/* Only actually creates the next row if we later try to some field
    	 * data into it.
    	 */
    	table.moveOnToNextRow();
    }
    
    public Data[] createOutData(Table referTable, String referFilePath) {
    	Data[] tableToReturnData = 
			new Data[] {new BasicData(referTable, Table.class.getName())};
		tableToReturnData[0].getMetadata().put(
				DataProperty.LABEL,
				"Parsed EndNote reference file: " + referFilePath);
        tableToReturnData[0].getMetadata().put(
        		DataProperty.TYPE, DataProperty.MATRIX_TYPE);
        return tableToReturnData;
    }
    
    private Map createColumnNameMap() {
    	Map columnNameMap = new Hashtable();
    	columnNameMap.put("A", "Authors");
		columnNameMap.put("B", "Secondary Title");
		columnNameMap.put("C", "Place Published");
		columnNameMap.put("D", "Year");
		columnNameMap.put("E", "Editor");
		columnNameMap.put("F", "Label");
		columnNameMap.put("G", "Language");
		columnNameMap.put("H", "Translated Author");
		columnNameMap.put("I", "Publisher");
		columnNameMap.put("J", "Journal");
		columnNameMap.put("K", "Keywords");
		columnNameMap.put("L", "Call Number");
		columnNameMap.put("M", "Accession Number");
		columnNameMap.put("N", "Number");
		columnNameMap.put("0", "Journal Article"); //(?)
		columnNameMap.put("P", "Pages");
		columnNameMap.put("Q", "Translated Title");
		columnNameMap.put("R", "Electronic Resource Number");
		columnNameMap.put("S", "Tertiary Title");
		columnNameMap.put("T", "Title");
		columnNameMap.put("U", "URL");
		columnNameMap.put("V", "Volume");
		columnNameMap.put("W", "Database Provider");
		columnNameMap.put("X", "Abstract");
		columnNameMap.put("Y", "Tertiary Author");
		columnNameMap.put("Z", "Notes");
		columnNameMap.put("0", "Reference Type");
		columnNameMap.put("1", "Custom 1");
		columnNameMap.put("2", "Custom 2");
		columnNameMap.put("3", "Custom 3");
		columnNameMap.put("4", "Custom 4");
		columnNameMap.put("6", "Number of Volumes");
		columnNameMap.put("7", "Edition");
		columnNameMap.put("8", "Date");
		columnNameMap.put("9", "Type of Work");
		columnNameMap.put("?", "Subsidiary Author");
		columnNameMap.put("@", "ISBN/ISSN");
		columnNameMap.put("!", "Short Title");
		columnNameMap.put("#", "Custom 5");
		columnNameMap.put("$", "Custom 6");
		columnNameMap.put("]", "Custom 7");
		columnNameMap.put("&", "Section");
		columnNameMap.put("(", "Original Publication");
		columnNameMap.put(")", "Reprint Edition");
		columnNameMap.put("*", "Reviewed Item");
		columnNameMap.put("+", "Author Address");
		columnNameMap.put("^", "Caption");
		columnNameMap.put(">", "Link to PDF");
		columnNameMap.put("<", "Research Notes");
    	return columnNameMap;
    }
}
