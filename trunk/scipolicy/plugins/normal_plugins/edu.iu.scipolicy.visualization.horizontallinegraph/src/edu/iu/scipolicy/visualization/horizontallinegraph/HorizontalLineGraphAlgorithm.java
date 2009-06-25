package edu.iu.scipolicy.visualization.horizontallinegraph;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import edu.iu.scipolicy.utilities.FileUtilities;

public class HorizontalLineGraphAlgorithm implements Algorithm {
	public static String LABEL_FIELD_ID = "label";
	public static String START_DATE_FIELD_ID = "start_date";
	public static String END_DATE_FIELD_ID = "end_date";
	public static String SIZE_BY_FIELD_ID = "size_by";
	
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
    	
    	// This will throw an exception with an appropriate message if it fails.
    	verifyThatTableHasAppropriateFields(inTable);
    	
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
    
    private void verifyThatTableHasAppropriateFields(Table table)
    	throws AlgorithmExecutionException
    {
    	// TODO: Something here?
    }
    
    private String createPostScriptCode(Table grantsTable,
    									int minNumberOfDaysForGrantBar)
    	throws AlgorithmExecutionException
    {
    	// Get user-inputted parameters.
    	final String labelKey = this.parameters.get(LABEL_FIELD_ID).toString();
    	
    	final String startDateKey =
    		this.parameters.get(START_DATE_FIELD_ID).toString();
    	
    	final String endDateKey = this.parameters.get(END_DATE_FIELD_ID).toString();
    	final String sizeByKey = this.parameters.get(SIZE_BY_FIELD_ID).toString();
    	
    	// Create the PostScript... creator.
    	HorizontalLineGraphPostScriptCreator postScriptCreator =
    		new HorizontalLineGraphPostScriptCreator
    			(labelKey, startDateKey, endDateKey, sizeByKey);
    	
    	String postScriptCode = null;
    	
    	try {
    		postScriptCode =
    			postScriptCreator.createPostScript
    				(grantsTable, minNumberOfDaysForGrantBar, this.logger);
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
		postScriptMetaData.put(DataProperty.TYPE, DataProperty.VECTOR_IMAGE_TYPE);
    	
        return new Data[] { postScriptData };
    }
}