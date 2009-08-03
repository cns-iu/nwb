package edu.iu.nwb.converter.prefuserefer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.Data;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import edu.iu.nwb.converter.prefuserefer.util.TableData;

public class ReferReader implements Algorithm {
    private LogService log;    
    private ReferUtil util;

    public ReferReader(
    		Data[] data, Dictionary parameters, CIShellContext context) {
        this.inReferFile = (File) data[0].getData();
        this.log =
        	(LogService) context.getService(LogService.class.getName());
        
        this.util = new ReferUtil(log);		
    }

    public Data[] execute() throws AlgorithmExecutionException {
    	BufferedReader referReader = util.makeReader(inReferFile);
    	Table referTable = extractTable(referReader);
    	Data[] referData =
    		util.createOutData(referTable, inReferFile.getAbsolutePath());
    	return referData;
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
     * 1) Each record is separated by TWO lines. If records are separated by one line, we will treat the second are part of the first record, UNLESS the next record is of type '0' (zero)
     * 3) Field start markers always occur as first character. A line with anything before the % will be treated as either a continuation of
     *     the previous field, or junk.
     * 4) Field contents start with 3rd character of a line with a %. If it starts earlier, it will be chopped off. If it starts later
     *    extra blanks will be added.
     * 5) Probably more that I am not thinking of
     */
	private File inReferFile;
    
    private Table extractTable(BufferedReader referReader)
    		throws AlgorithmExecutionException {
    	// The table we are filling with reference records
    	TableData table = util.createEmptyTable();
    	
    	// The field we are currently reading ("Author", "Year", etc...)
    	String field = null;
    	/* The content of the field we have read
    	 * so far (Nikola Tesla, 1899, etc...)
    	 */
    	String fieldContents = null;
    	// The current line of the file we are parsing
    	String line = null; 
    	/* Used to determine whether blank lines are part of field data,
    	 * or are separating two records
    	 */
    	int numBlankLinesInARowAfterField = 0;
    	
    
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
    			if (util.isEndOfFile(line)) {
    				state = END;
    			} else if (util.startsWithFieldMarker(line)) {
    				state = READ_NEW_FIELD;
    			} else if (util.isBlank(line)) {
    				state = READ_BLANK_LINE;
    			}  else {
    				state = ERROR;
    			}
    			break;
    		}
    		
    		case READ_NEW_FIELD: { // we come upon a new field
    			//make and fill new field
    			field = util.extractFieldName(line);
    			fieldContents = util.extractContentsFromFirstLineOfField(line);
    			//get next line
    			line = getNextLine(referReader);
    			//go to next state
    			if (util.isEndOfFile(line)) {
    				util.commitFieldToRecord(table, field, fieldContents);
    				util.commitRecordToTable(table);
    				state = END;
    			}else if (util.startsWithFieldMarker(line)) {
    				util.commitFieldToRecord(table, field, fieldContents);
    				state = READ_NEW_FIELD;
    			} else if (util.isBlank(line)) {
    				state = READ_BLANK_LINE_AFTER_FIELD_DATA;
    			}  else {
    				fieldContents += MULTI_LINE_SEP_CHAR;
    				state = READ_MORE_OF_FIELD;
    			}
    			break;
    		} 
    		
    		case READ_MORE_OF_FIELD: { //we continue getting data from a multi-line field
    			//add line to contents of current field
    			fieldContents += util.extractContentsFromLine(line);
    			//get next line
    			line = getNextLine(referReader);
    			//go to next state
    			if (util.isEndOfFile(line)) {
    				util.commitFieldToRecord(table, field, fieldContents);
    				util.commitRecordToTable(table);
    				state = END;
    			} else if (util.startsWithFieldMarker(line)) {
    				util.commitFieldToRecord(table, field, fieldContents);
    				state = READ_NEW_FIELD;
    			} else if (util.isBlank(line)) {
    				state = READ_BLANK_LINE_AFTER_FIELD_DATA;
    			}  else {
    				fieldContents += MULTI_LINE_SEP_CHAR;
    				state = READ_MORE_OF_FIELD;
    			}
    			break;
    		}
    		
    		case READ_BLANK_LINE_AFTER_FIELD_DATA: {  //we read a blank line directly after a data field (blank line may be part of field data or not)
    			numBlankLinesInARowAfterField++;
    			//get next line
    			line = getNextLine(referReader);
    			if (util.isEndOfFile(line)) {
    				//blank line was just extra space at the end of the file
    				util.commitFieldToRecord(table, field, fieldContents);
    				util.commitRecordToTable(table);
    				//reset blank line in a row counter
    				numBlankLinesInARowAfterField = 0;
    				state = END;
    			}else if (util.startsWithFieldMarker(line) && numBlankLinesInARowAfterField == 1 && (! util.startsThisFieldType(line, "0"))) {
    				//blank line was separating fields in the same record
    				util.commitFieldToRecord(table, field, fieldContents);
    				//reset blank line in a row counter
    				numBlankLinesInARowAfterField = 0;
    				state = READ_NEW_FIELD;
    			} else if (util.startsWithFieldMarker(line) && (numBlankLinesInARowAfterField >= 2 || util.startsThisFieldType(line, "0"))) {
    				//blank lines were separating two records
    				util.commitFieldToRecord(table, field, fieldContents);
    				util.commitRecordToTable(table);
    				//reset blank line in a row counter
    				numBlankLinesInARowAfterField = 0;
    				state = READ_NEW_FIELD;
    			} else if (util.isBlank(line)) {
    				//yet another blank line...
    				//either these blank lines separate two records (most likely), or there are two blank lines in the content of a field (freak circumstance)
    				state = READ_BLANK_LINE_AFTER_FIELD_DATA;
    			}  else {
    				//blank line was part of multi-line field data (part of multi-paragraph abstract, for instance)
    				//add blank lines to field contents for each blank line we just saw
    				for (int i = 0; i < numBlankLinesInARowAfterField; i++) {
    					fieldContents += util.getBlankLine();
    				}
    				//reset blank line in a row counter
    				numBlankLinesInARowAfterField = 0;
    				state = READ_MORE_OF_FIELD;
    			}
    			break;
    		}
    		
    		case READ_BLANK_LINE: { //we read a blank line in any other circumstances
    			//get next line
    			line = getNextLine(referReader);
    			//go to next state
    			if (util.isEndOfFile(line)) {
    				state = END;
    			}else if (util.startsWithFieldMarker(line)) {
    				state = READ_NEW_FIELD;
    			} else if (util.isBlank(line)) {
    				state = READ_BLANK_LINE;
    			}  else {
    				state = ERROR;
    			}
    			break;
    		}
    		
    		case ERROR: {  //we see a line which we do not know how to handle
    			this.log.log(
    					LogService.LOG_WARNING,
    					"Format error on line " + getLinesRead()
    					+ " of reference file. The line '" + line 
    					+ "' was not inside of a field. "
    					+ "Ignoring line and moving on.");
    			//get next line
    			line = getNextLine(referReader);
    			//go to next state
    			 if (util.isEndOfFile(line)) {
     				state = END;
     			} else if (util.startsWithFieldMarker(line)) {
    				state = READ_NEW_FIELD;
    			} else if (util.isBlank(line)) {
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
    			log.log(
    					LogService.LOG_WARNING,
    					"Programmer error: attempted to enter invalid state "
    					+ "for state machine in ReferReader.");
    			doneParsing = true;
    			break;
    	}
    }
    	
    	return table.getPrefuseTable();
    }
    
    
    public String getNextLine(BufferedReader reader){
    	try {
	    	String line = reader.readLine();
	    	linesRead++;
	    	return line;
    	} catch (IOException e) {
    		log.log(LogService.LOG_WARNING,
    				"Unable to read the next line from file. "
    				+ "Treating this as the end of the file", e);
    		return null;
    	}
    }
    
    private int getLinesRead() {
    	return this.linesRead;
    }
}