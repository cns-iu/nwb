package edu.iu.nwb.converter.prefuserefer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

import prefuse.data.Schema;
import prefuse.data.Table;
import edu.iu.nwb.converter.prefuserefer.util.TableData;
import edu.iu.nwb.converter.prefuserefer.util.UnicodeReader;

public class ReferUtil {
	
	private LogService log;
	
	public ReferUtil(LogService log) {
		this.log = log;
	}
	
	public BufferedReader makeReader(File file) {
	    	try {
	    	InputStream stream = new FileInputStream(file);
	    	/*
	    	 * UnicodeReader contains a hack for eating funny encoding characters that are 
	    	 * sometimes stuck onto the beginning of files. Necessary
	    	 *  due to bug in standard reader.
	    	 */
	    	UnicodeReader unicodeReader = new UnicodeReader(stream, "UTF-8"); 
	    	BufferedReader reader = new BufferedReader(unicodeReader);
	    	return reader;
	    	} catch (FileNotFoundException e1) {
	    		this.log.log(LogService.LOG_ERROR, "ReferReader could not find a file at " + file.getAbsolutePath(), e1);
	    		return null;
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
    
    public void printError(String line, int lineNum) {
    	this.log.log(LogService.LOG_WARNING,
				"Format error on line " + lineNum + " of reference file. The line '" + line 
				+ "' was not inside of a field. Ignoring line and moving on.");
    }
    
    public TableData createEmptyTable() {
    	    	Schema s = new Schema();
    	    	return new TableData(s);
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
    
    public void commitFieldToRecord(TableData table, String field, String fieldContents) {
    	//will create the column if it does not already exist
    	table.setString(field, fieldContents);
    }
    
    public void commitRecordToTable(TableData table) {
    	//only actually creates the next row if we later try to some field data into it.
    	table.moveOnToNextRow();
    }
    
    public Data[] formatAsData(Table referTable, String referFilePath) {
    	Data[] tableToReturnData = 
			new Data[] {new BasicData(referTable, Table.class.getName())};
		tableToReturnData[0].getMetaData().put(DataProperty.LABEL, "Parsed EndNote reference file: " + referFilePath);
		//TODO: should this really be a text_type?
        tableToReturnData[0].getMetaData().put(DataProperty.TYPE, DataProperty.TEXT_TYPE);
        return tableToReturnData;
    }
    
    public void printProgrammerErrorMessage() {
    	this.log.log(LogService.LOG_WARNING,
    			"Programmer error: attempted to enter invalid state for state machine in ReferReader.");
    }
}
