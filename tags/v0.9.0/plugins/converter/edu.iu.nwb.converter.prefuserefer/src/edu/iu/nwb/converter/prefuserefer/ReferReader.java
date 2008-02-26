package edu.iu.nwb.converter.prefuserefer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

import prefuse.data.Schema;
import prefuse.data.Table;

public class ReferReader implements Algorithm {
    Data[] data;
    Dictionary parameters;
    CIShellContext context;
    LogService log;

    public ReferReader(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
        this.log = (LogService) context.getService("org.osgi.service.log.LogService");
    }

    public Data[] execute() {
    	File referFile = (File) data[0].getData();
    	BufferedReader referReader = makeReader(referFile); if (referReader == null) return null;
    	Table referTable = extractTable(referReader);
    	Data[] referData = formatAsData(referTable, referFile.getAbsolutePath());
    	return referData;
    }
    
    private BufferedReader makeReader(File file) {
    	try {
    	InputStream stream = new FileInputStream(file);
    	InputStreamReader streamReader = new InputStreamReader(stream, "UTF-8");
    	BufferedReader reader = new BufferedReader(streamReader);
    	return reader;
    	} catch (FileNotFoundException e1) {
    		this.log.log(LogService.LOG_ERROR, "ReferReader could not find a file at " + file.getAbsolutePath(), e1);
    		return null;
    	} catch (UnsupportedEncodingException e2) {
    		this.log.log(LogService.LOG_ERROR, "the UTF-8 encoding is not supported on this machine.", e2);
    		return null;
    	}
    }
    
    //states for state machine
    private static final int START = 0;
    private static final int READ_NEW_FIELD = 1; 
    private static final int READ_MORE_OF_FIELD = 2; 
    private static final int READ_BLANK_LINE_AFTER_FIELD_DATA = 3;
    private static final int READ_BLANK_LINE = 4; 
    private static final int ERROR = 5;
    private static final int END = 6; 
    
    private static final String MULTI_LINE_SEP_CHAR = " ";
    
    private int linesRead = 0;
    /*
     * NOTE: As there is no standard format for refer, this is based on several examples that I currently have access to. 
     * Unexpected format changes may lead to lost or mangled data.
     * 
     * Assumptions for table extraction:
     * 1) Each record is separated by TWO lines. If records are separated by one line, we will treat the second are part of the first record.
     * 2) Fields may have a single blank line inside of them, but no more. If a field (such as an abstract) has more than one blank line
     * 	inside it, we will treat the extra lines as junk until we come upon a new field start marker (%)
     * 3) Field start markers always occur as first character. A line with anything before the % will be treated as either a continuation of
     *     the previous field, or junk.
     * 4) Field contents start with 3rd character of a line with a %. If it starts earlier, it will be chopped off. If it starts later
     *    extra blanks will be added.
     * 5) Probably more that I am not thinking of
     */
    
    private Table extractTable(BufferedReader referReader) {
    	TableData table = createEmptyTable(); // the table we are filling with reference records
    	
    	String field = null; //the field we are currently reading ("Author", "Year", etc...)
    	String fieldContents = null; //the content of the field we have read so far (Nikola Tesla, 1899, etc...)
    	String line = null; //the current line of the file we are parsing
    	
    
    	boolean doneParsing = false;
    	//a finite state machine
    	/* basic steps for each state:
    	 *    1) process current line
    	 *    2) read in next line
    	 *    3) jump to next state based on the line we just read in
    	 */
    	
    	int state = START;
    	while (! doneParsing) {
    		switch (state) {
    	
    		case START: {  //we start the file
    			//get first line
    			line = getNextLine(referReader);
    			//go to next state
    			if (isEndOfFile(line)) {
    				state = END;
    			} else if (startsWithFieldMarker(line)) {
    				state = READ_NEW_FIELD;
    			} else if (isBlank(line)) {
    				state = READ_BLANK_LINE;
    			}  else {
    				state = ERROR;
    			}
    			break;
    		}
    		
    		case READ_NEW_FIELD: { // we come upon a new field
    			//make and fill new field
    			field = extractFieldName(line);
    			fieldContents = extractContentsFromFirstLineOfField(line);
    			//get next line
    			line = getNextLine(referReader);
    			//go to next state
    			if (isEndOfFile(line)) {
    				commitFieldToRecord(table, field, fieldContents);
    				commitRecordToTable(table);
    				state = END;
    			}else if (startsWithFieldMarker(line)) {
    				commitFieldToRecord(table, field, fieldContents);
    				state = READ_NEW_FIELD;
    			} else if (isBlank(line)) {
    				state = READ_BLANK_LINE_AFTER_FIELD_DATA;
    			}  else {
    				fieldContents += MULTI_LINE_SEP_CHAR;
    				state = READ_MORE_OF_FIELD;
    			}
    			break;
    		} 
    		
    		case READ_MORE_OF_FIELD: { //we continue getting data from a multi-line field
    			//add line to contents of current field
    			fieldContents += extractContentsFromLine(line);
    			//get next line
    			line = getNextLine(referReader);
    			//go to next state
    			if (isEndOfFile(line)) {
    				commitFieldToRecord(table, field, fieldContents);
    				commitRecordToTable(table);
    				state = END;
    			} else if (startsWithFieldMarker(line)) {
    				commitFieldToRecord(table, field, fieldContents);
    				state = READ_NEW_FIELD;
    			} else if (isBlank(line)) {
    				state = READ_BLANK_LINE_AFTER_FIELD_DATA;
    			}  else {
    				fieldContents += MULTI_LINE_SEP_CHAR;
    				state = READ_MORE_OF_FIELD;
    			}
    			break;
    		}
    		
    		case READ_BLANK_LINE_AFTER_FIELD_DATA: {  //we read a blank line directly after a data field (may be part of field data or not)
    			//get next line
    			line = getNextLine(referReader);
    			if (isEndOfFile(line)) {
    				//blank line was just extra space at the end of the file
    				commitFieldToRecord(table, field, fieldContents);
    				commitRecordToTable(table);
    				state = END;
    			}else if (startsWithFieldMarker(line)) {
    				//blank line was for the end of a field
    				commitFieldToRecord(table, field, fieldContents);
    				state = READ_NEW_FIELD;
    			} else if (isBlank(line)) {
    				//blank line was for the end of a record
      				commitFieldToRecord(table, field, fieldContents);
    				commitRecordToTable(table);
    				state = READ_BLANK_LINE;
    			}  else {
    				//blank line was part of multi-line field data (part of multi-paragraph abstract, for instance)
    				fieldContents += getBlankLine();
    				state = READ_MORE_OF_FIELD;
    			}
    			break;
    		}
    		
    		case READ_BLANK_LINE: { //we read a blank line in any other circumstances
    			//get next line
    			line = getNextLine(referReader);
    			//go to next state
    			if (isEndOfFile(line)) {
    				state = END;
    			}else if (startsWithFieldMarker(line)) {
    				state = READ_NEW_FIELD;
    			} else if (isBlank(line)) {
    				state = READ_BLANK_LINE;
    			}  else {
    				state = ERROR;
    			}
    			break;
    		}
    		
    		case ERROR: {  //we see a line which we do not know how to handle
    			//print error
    			printError(line, getLinesRead());
    			//get next line
    			line = getNextLine(referReader);
    			//go to next state
    			 if (isEndOfFile(line)) {
     				state = END;
     			} else if (startsWithFieldMarker(line)) {
    				state = READ_NEW_FIELD;
    			} else if (isBlank(line)) {
    				state = READ_BLANK_LINE;
    			}else {
    				state = ERROR;
    			}
    			break;
    		}
    		
    		case END: { //we end the file
    			doneParsing = true;
    			break;
    		}
    		
    		default:
    			printProgrammerErrorMessage();
    			doneParsing = true;
    			break;
    	}
    }
    	
    	return table.getPrefuseTable();
    }
    
    private boolean startsWithFieldMarker(String line) {
    	return line.startsWith("%");
    }
    
    private boolean isBlank(String line) {
    	String isEntirelyWhitespace = "^[\\s]*$";
    	return line.length() == 0 || line.matches(isEntirelyWhitespace);
    }
    
    private boolean isEndOfFile(String line) {
    	return line == null;
    }
    
    private void printError(String line, int lineNum) {
    	this.log.log(LogService.LOG_WARNING,
				"Format error on line " + lineNum + " of reference file. The line '" + line 
				+ "' was not inside of a field. Ignoring line and moving on.");
    }
    
    private String getNextLine(BufferedReader reader) {
    	try {
    	String line = reader.readLine();
    	linesRead++;
    	return line;
    	} catch (IOException e1) {
    		this.log.log(LogService.LOG_ERROR, "Unable to read the next line from file. Treating this as the end of the file", e1);
    		return null;
    	}
    }
    
    private TableData createEmptyTable() {
    	    	Schema s = new Schema();
    	    	return new TableData(s);
    }
    
    private String extractFieldName(String line) {
    	//extract second character
    	return line.substring(1, 2);
    	//example: "%A Allen Ginsberg"
    	//result is:  "A"
    }
    
    private String extractContentsFromFirstLineOfField(String line) {
    	//extract everything beyond 3rd character
    	return line.substring(3);
    	//example: "%A Allen Ginsberg"
    	//result is:  "Allen Ginsberg"
    }
    
    private String extractContentsFromLine(String line) {
    	//returns exact same line (may want to change this later)
    	return line;
    }
    
    private String getBlankLine() {
    	return "\r\n";
    }
    
    private void commitFieldToRecord(TableData table, String field, String fieldContents) {
    	//will create the column if it does not already exist
    	table.setString(field, fieldContents);
    }
    
    private void commitRecordToTable(TableData table) {
    	//only actually creates the next row if we later try to some field data into it.
    	table.moveOnToNextRow();
    }
    
    private Data[] formatAsData(Table referTable, String referFilePath) {
    	Data[] tableToReturnData = 
			new Data[] {new BasicData(referTable, Table.class.getName())};
		tableToReturnData[0].getMetaData().put(DataProperty.LABEL, "Parsed EndNote reference file: " + referFilePath);
		//TODO: should this really be a text_type?
        tableToReturnData[0].getMetaData().put(DataProperty.TYPE, DataProperty.TEXT_TYPE);
        return tableToReturnData;
    }
    
    private void printProgrammerErrorMessage() {
    	this.log.log(LogService.LOG_WARNING,
    			"Programmer error: attempted to enter invalid state for state machine in ReferReader.");
    }
    
    private int getLinesRead() {
    	return this.linesRead;
    }
    
//    private boolean singleLineSepWarningHasBeenPrinted = false;
//    
//    private void printSingleLineRecordSeparationWarning() {
//    	if (! singleLineSepWarningHasBeenPrinted) {
//    		this.log.log(LogService.LOG_WARNING, "WARNING:" +
//    				" This file contains references which are separated by a single line, instead of the standard two." +
//    		" This may cause errors for fields which contain data with line breaks, such as the abstracts of papers.");
//    		singleLineSepWarningHasBeenPrinted = true;
//    	} 
//    }
    
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
		
		//will create the column if it does not already exist
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