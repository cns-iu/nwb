package edu.iu.scipolicy.visualization.horizontallinegraph;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import prefuse.data.tuple.TableTuple;
import edu.iu.scipolicy.utilities.FileUtilities;

public class HorizontalLineGraphAlgorithm implements Algorithm {
    private Data[] data;
    private Dictionary parameters;
    private CIShellContext context;
    private LogService logger;
    
    public HorizontalLineGraphAlgorithm(Data[] data,
    									Dictionary parameters,
    									CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
        
        this.logger = (LogService)context.getService(LogService.class.getName());
    }

    public Data[] execute() throws AlgorithmExecutionException {
    	Data inData = this.data[0];
    	// Unpack the in-data table.
    	Table inTable = (Table)inData.getData();
    	
    	// "Get" the input parameters.
        int minNumberOfDaysForGrantBar = 15;
        
        // Inform the user that creating the PostScript may take a little while.
    	logger.log(LogService.LOG_INFO,
    			   "Creating PostScript.  May take a few moments...");
    	
    	String postScriptCode =
    		createPostScriptCode(inTable, minNumberOfDaysForGrantBar);
    	
    	File temporaryPostScriptFile =
    		writePostScriptCodeToTemporaryFile(postScriptCode, "TEMP-POSTSCRIPT");
		
		Data[] outData = formOutData(temporaryPostScriptFile, inData);
		
		return outData;
    }
    
    private String createPostScriptCode(Table grantsTable,
    									int minNumberOfDaysForGrantBar)
    	throws AlgorithmExecutionException
    {
    	// Create the PostScript... creator.
    	HorizontalLineGraphPostScriptCreator horizontalLineGraphPostScriptCreator =
    		new HorizontalLineGraphPostScriptCreator();
    	
    	String postScriptCode = null;
    	
    	try {
    		postScriptCode =
    			horizontalLineGraphPostScriptCreator.createPostScript
    				(grantsTable, minNumberOfDaysForGrantBar);
    	}
    	catch (PostScriptCreationException e) {
    		throw new AlgorithmExecutionException(e);
    	}
    	
    	return postScriptCode;
    }
    
    private File writePostScriptCodeToTemporaryFile(String postScriptCode,
    												String temporaryFileName)
    	throws AlgorithmExecutionException
    {
    	File temporaryPostScriptFile = null;
    	
    	try {
    		temporaryPostScriptFile =
    			FileUtilities.createTemporaryFileInDefaultTemporaryDirectory
    				(temporaryFileName, "eps");
    	}
    	catch (IOException e) {
    		throw new AlgorithmExecutionException
    			("Error creating temporary file to write PostScript out to", e);
    	}
    	
    	// TODO: make variable names shorter (?)
    	
    	// Write the contents of the PostScript to this temporary file now.
		try {
			FileWriter temporaryPostScriptFileWriter =
				new FileWriter(temporaryPostScriptFile);
			
			temporaryPostScriptFileWriter.write(postScriptCode);
			temporaryPostScriptFileWriter.flush();
		}
		catch (IOException e) {
			throw new AlgorithmExecutionException
				("Error writing PostScript out to temporary file", e);
		}
		
		return temporaryPostScriptFile;
    }
    
    private Data[] formOutData(File postScriptFile, Data singleInData) {
    	Dictionary inMetaData = singleInData.getMetadata();
    	
    	// Wrap the file.
		Data postScriptData =
			new BasicData(postScriptFile, "file:text/ps");
		
		// Metadata fun.
		Dictionary postScriptMetaData = postScriptData.getMetadata();
		
		postScriptMetaData.put(DataProperty.LABEL,
							   "PostScript: " + inMetaData.get(DataProperty.LABEL));
		postScriptMetaData.put(DataProperty.PARENT, singleInData);
		postScriptMetaData.put(DataProperty.TYPE, DataProperty.POST_SCRIPT_TYPE);
    	
        return new Data[] { postScriptData };
    }
    
    public static void main(String[] args) {
    	String[] columnNames =
    		new String[] { "label", "start date", "end date", "amount" };
    	
    	Table table = new Table();
    	
    	// Set the table columns.
    	table.addColumn(Grant.GRANT_AWARD_LABEL_KEY, String.class);
    	table.addColumn(Grant.GRANT_AWARD_START_DATE_KEY, Date.class);
    	table.addColumn(Grant.GRANT_AWARD_END_DATE_KEY, Date.class);
    	table.addColumn(Grant.GRANT_AWARD_AMOUNT, Integer.class);
    	
    	// Add some rows and fill them in.
    	table.addRow();
    	table.set(0, Grant.GRANT_AWARD_LABEL_KEY, "Micah");
    	table.set(0, Grant.GRANT_AWARD_START_DATE_KEY, new Date(1985, 6, 3));
    	table.set(0, Grant.GRANT_AWARD_END_DATE_KEY, new Date(2009, 6, 4));
    	table.set(0, Grant.GRANT_AWARD_AMOUNT, new Integer(1000));
    	// ----
    	table.addRow();
    	table.set(1, Grant.GRANT_AWARD_LABEL_KEY, "Patrick");
    	table.set(1, Grant.GRANT_AWARD_START_DATE_KEY, new Date(1984, 2, 15));
    	table.set(1, Grant.GRANT_AWARD_END_DATE_KEY, new Date(1985, 2, 15));
    	table.set(1, Grant.GRANT_AWARD_AMOUNT, new Integer(100));
    	// ----
    	table.addRow();
    	table.set(2, Grant.GRANT_AWARD_LABEL_KEY, "Elisha");
    	table.set(2, Grant.GRANT_AWARD_START_DATE_KEY, new Date(1985, 10, 19));
    	table.set(2, Grant.GRANT_AWARD_END_DATE_KEY, new Date(1994, 9, 20));
    	table.set(2, Grant.GRANT_AWARD_AMOUNT, new Integer(500));
    	
    	Data dataItem = new BasicData(table, "file:text/table");
    	Data[] data = new Data[] { dataItem };
    	HorizontalLineGraphAlgorithm hlga = new HorizontalLineGraphAlgorithm(data, null, null);
    	
    	try {
    		hlga.execute();
    	}
    	catch (AlgorithmExecutionException e) {
    		System.err.println("AlgorithmExecutionException: " + e.getMessage());
    	}
    	catch (Exception e) {
    		System.err.println("Exception (" + e.getClass().getName() + "): " + e.getMessage());
    		e.printStackTrace();
    	}
    }
}