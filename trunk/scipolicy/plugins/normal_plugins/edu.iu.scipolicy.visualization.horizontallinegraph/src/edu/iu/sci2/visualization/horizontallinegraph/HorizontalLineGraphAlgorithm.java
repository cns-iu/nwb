package edu.iu.sci2.visualization.horizontallinegraph;

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
import org.cishell.utilities.FileUtilities;
import org.osgi.service.log.LogService;

import prefuse.data.Table;

public class HorizontalLineGraphAlgorithm implements Algorithm {
	public static final double DEFAULT_PAGE_WIDTH = 8.5;
	public static final double DEFAULT_PAGE_HEIGTH = 11.0;

	public static final String POSTSCRIPT_MIME_TYPE="file:text/ps";
	public static final String EPS_FILE_EXTENSION="eps";
	public static final String LABEL_FIELD_ID = "label";
	public static final String START_DATE_FIELD_ID = "start_date";
	public static final String END_DATE_FIELD_ID = "end_date";
	public static final String SIZE_BY_FIELD_ID = "size_by";
	public static final String DATE_FORMAT_FIELD_ID = "date_format";
	public static final String PAGE_WIDTH_FIELD_ID = "page_width";
	public static final String PAGE_HEIGHT_FIELD_ID = "page_height";
	public static final String SHOULD_SCALE_OUTPUT_FIELD_ID =
		"should_scale_output";
	
    private Data inputData;
    private String labelKey;
    private String startDateKey;
    private String endDateKey;
    private String sizeByKey;
    private String startDateFormat;
    private String endDateFormat;
    private double pageWidth = DEFAULT_PAGE_WIDTH;
    private double pageHeight = DEFAULT_PAGE_HEIGTH;
    private boolean shouldScaleOutput = false;
    
    private LogService logger;
    
    public HorizontalLineGraphAlgorithm(Data[] data,
    									Dictionary<String, Object> parameters,
    									CIShellContext context) {
        this.inputData = data[0];
        
        this.labelKey = parameters.get(LABEL_FIELD_ID).toString();
        this.startDateKey = parameters.get(START_DATE_FIELD_ID).toString();
        this.endDateKey = parameters.get(END_DATE_FIELD_ID).toString();
        this.sizeByKey = parameters.get(SIZE_BY_FIELD_ID).toString();
        this.startDateFormat = (String)parameters.get(DATE_FORMAT_FIELD_ID);
        this.endDateFormat = (String)parameters.get(DATE_FORMAT_FIELD_ID);
        this.pageWidth =
        	((Double)parameters.get(PAGE_WIDTH_FIELD_ID)).doubleValue();
        this.pageHeight =
        	((Double)parameters.get(PAGE_HEIGHT_FIELD_ID)).doubleValue();
        this.shouldScaleOutput =
        	((Boolean)parameters.get(SHOULD_SCALE_OUTPUT_FIELD_ID)).
        		booleanValue();
        
        this.logger =
        	(LogService) context.getService(LogService.class.getName());
    }

    public Data[] execute() throws AlgorithmExecutionException {
    	Table inTable = (Table) this.inputData.getData();
    	
        int minNumberOfDaysForGrantBar = 15;
        
    	logger.log(LogService.LOG_INFO,
    			   "Creating PostScript.  May take a few moments...");
    	
    	String postScriptCode = null;
    	
    	try {
    		postScriptCode =
    			createPostScriptCode(inTable, minNumberOfDaysForGrantBar);
    	} catch (PostScriptCreationException postScriptCreationException) {
    		String exceptionMessage =
    			"An error occurred when creating the PostScript for the " +
    			"data \"" +
    			this.inputData.getMetadata().get(DataProperty.LABEL) +
    			"\".";
    		
    		throw new AlgorithmExecutionException(
    			exceptionMessage, postScriptCreationException);
    	}
    	
    	File temporaryPostScriptFile =
    		writePostScriptCodeToTemporaryFile(
	    		postScriptCode, "horizontal-line-graph");
		
		return formOutData(temporaryPostScriptFile, inputData);
    }
    
    private String createPostScriptCode(
    		Table grantsTable, int minNumberOfDaysForGrantBar)
    			throws PostScriptCreationException {
    	HorizontalLineGraphPostScriptCreator postScriptCreator =
    		new HorizontalLineGraphPostScriptCreator(
    			this.labelKey,
    			this.startDateKey,
    			this.endDateKey,
    			this.sizeByKey,
    			this.startDateFormat,
    			this.endDateFormat,
    			this.pageWidth,
    			this.pageHeight,
    			this.shouldScaleOutput);

		String postScriptCode =
			postScriptCreator.createPostScript
				(grantsTable, minNumberOfDaysForGrantBar, this.logger);
		
		return postScriptCode;
    }
    
    private File writePostScriptCodeToTemporaryFile(
    		String postScriptCode, String temporaryFileName)
    		throws AlgorithmExecutionException {
    	File temporaryPostScriptFile = null;
    	
    	try {
    		temporaryPostScriptFile =
    			FileUtilities.createTemporaryFileInDefaultTemporaryDirectory(
    				temporaryFileName, EPS_FILE_EXTENSION);
    	} catch (IOException postScriptFileCreationException) {
    		String exceptionMessage =
    			"Error creating temporary PostScript file.";
    		
    		throw new AlgorithmExecutionException(
    			exceptionMessage, postScriptFileCreationException);
    	}
    	
    	// TODO: Make variable names shorter?
    	
		try {		
			FileWriter temporaryPostScriptFileWriter =
				new FileWriter(temporaryPostScriptFile);
			
			temporaryPostScriptFileWriter.write(postScriptCode);
			temporaryPostScriptFileWriter.flush();
			temporaryPostScriptFileWriter.close();
		}
		catch (IOException postScriptFileWritingException) {
			String exceptionMessage =
				"Error writing PostScript out to temporary file";
			
			throw new AlgorithmExecutionException(
				exceptionMessage, postScriptFileWritingException);
		}
		
		return temporaryPostScriptFile;
    }
    
    @SuppressWarnings("unchecked") // Raw Dictionary
    private Data[] formOutData(File postScriptFile, Data singleInData) {
    	Dictionary inMetaData = singleInData.getMetadata();
    	
		Data postScriptData =
			new BasicData(postScriptFile, POSTSCRIPT_MIME_TYPE);

		Dictionary postScriptMetaData = postScriptData.getMetadata();

		postScriptMetaData.put(
			DataProperty.LABEL,
			"PostScript: " + inMetaData.get(DataProperty.LABEL));
		postScriptMetaData.put(DataProperty.PARENT, singleInData);
		postScriptMetaData.put(
			DataProperty.TYPE, DataProperty.VECTOR_IMAGE_TYPE);
    	
        return new Data[] { postScriptData };
    }
}