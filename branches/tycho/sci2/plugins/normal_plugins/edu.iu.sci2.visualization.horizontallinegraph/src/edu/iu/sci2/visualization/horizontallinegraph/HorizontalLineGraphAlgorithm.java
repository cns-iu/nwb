package edu.iu.sci2.visualization.horizontallinegraph;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Dictionary;

import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.utilities.FileUtilities;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import au.com.bytecode.opencsv.CSVWriter;

public class HorizontalLineGraphAlgorithm implements Algorithm {
	public static final double DEFAULT_PAGE_WIDTH = 8.5;
	public static final double DEFAULT_PAGE_HEIGTH = 11.0;
	public static final int MINIMUM_NUMBER_OF_DAYS_FOR_BAR = 15;

	public static final String POSTSCRIPT_MIME_TYPE = "file:text/ps";
	public static final String CSV_MIME_TYPE = "file:text/csv";
	public static final String EPS_FILE_EXTENSION = "eps";
	public static final String CSV_FILE_EXTENSION = "csv";
	
    private Data inputData;
    private Table inputTable;
    private LogService logger;
    private String labelColumn;
    private String startDateColumn;
    private String endDateColumn;
    private String sizeByColumn;
    private String startDateFormat;
    private String endDateFormat;
    private double pageWidth = DEFAULT_PAGE_WIDTH;
    private double pageHeight = DEFAULT_PAGE_HEIGTH;
    private boolean shouldScaleOutput = false;
    
    public HorizontalLineGraphAlgorithm(
    		Data inputData,
    		Table inputTable,
    		LogService logger,
    		String labelColumn,
    		String startDateColumn,
    		String endDateColumn,
    		String sizeByColumn,
    		String startDateFormat,
    		String endDateFormat,
    		double pageWidth,
    		double pageHeight,
    		boolean shouldScaleOutput) {
        this.inputData = inputData;
        this.inputTable = inputTable;
        this.logger = logger;
        this.labelColumn = labelColumn;
        this.startDateColumn = startDateColumn;
        this.endDateColumn = endDateColumn;
        this.sizeByColumn = sizeByColumn;
        this.startDateFormat = startDateFormat;
        this.endDateFormat = endDateFormat;
        this.pageWidth = pageWidth;
        this.pageHeight = pageHeight;
        this.shouldScaleOutput = shouldScaleOutput;
    }

    public Data[] execute() throws AlgorithmExecutionException {
    	logger.log(LogService.LOG_INFO, "Creating PostScript. May take a few moments...");
    	CSVWriter csvWriter = null;

    	try {
    		File barSizesFile = FileUtilities.createTemporaryFileInDefaultTemporaryDirectory(
    			"barSizes", CSV_FILE_EXTENSION);
    		csvWriter = new CSVWriter(new FileWriter(barSizesFile));
    		String[] header = new String[] {
    			"Record Name", "Width", "Height", "Area (Width x Height)"
    		};
    		csvWriter.writeNext(header);
    		String postScriptCode = createPostScriptCode(csvWriter);
    		File temporaryPostScriptFile =
    			writePostScriptCodeToTemporaryFile(postScriptCode, "horizontal-line-graph");
    		csvWriter.close();

			return formOutData(temporaryPostScriptFile, barSizesFile, inputData);
    	} catch (IOException e) {
    		String exceptionMessage = String.format(
    			"An error occurred when creating the bar sizes CSV file for %s.",
    			getLabel(this.inputData.getMetadata()));
    		throw new AlgorithmExecutionException(exceptionMessage, e);
    	} catch (PostScriptCreationException e) {
    		String exceptionMessage = String.format(
    			"An error occurred when creating the PostScript for the %s.",
    			getLabel(this.inputData.getMetadata()));
    		throw new AlgorithmExecutionException(exceptionMessage, e);
    	} finally {
    		if (csvWriter != null) {
    			try {
    				csvWriter.close();
    			} catch (Exception e) {
    				throw new AlgorithmExecutionException(e.getMessage(), e);
    			}
    		}
    	}
    }

    private String createPostScriptCode(CSVWriter csvWriter) throws PostScriptCreationException {
    	HorizontalLineGraphPostScriptCreator postScriptCreator =
    		new HorizontalLineGraphPostScriptCreator(
    			this.labelColumn,
    			this.startDateColumn,
    			this.endDateColumn,
    			this.sizeByColumn,
    			this.startDateFormat,
    			this.endDateFormat,
    			this.pageWidth,
    			this.pageHeight,
    			this.shouldScaleOutput);

		String postScriptCode = postScriptCreator.createPostScript(
			this.inputTable, MINIMUM_NUMBER_OF_DAYS_FOR_BAR, this.logger, csvWriter);

		return postScriptCode;
    }

    private File writePostScriptCodeToTemporaryFile(
    		String postScriptCode, String temporaryFileName) throws AlgorithmExecutionException {
    	File temporaryPostScriptFile = null;
    	
    	try {
    		temporaryPostScriptFile = FileUtilities.createTemporaryFileInDefaultTemporaryDirectory(
				temporaryFileName, EPS_FILE_EXTENSION);
    	} catch (IOException e) {
    		String exceptionMessage = "Error creating temporary PostScript file.";
    		throw new AlgorithmExecutionException(exceptionMessage, e);
    	}

    	FileWriter writer = null;

		try {		
			writer = new FileWriter(temporaryPostScriptFile);
			
			writer.write(postScriptCode);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			String exceptionMessage = "Error writing PostScript out to temporary file";
			throw new AlgorithmExecutionException(exceptionMessage, e);
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					String exceptionMessage = "Error writing PostScript out to temporary file";
					throw new AlgorithmExecutionException(exceptionMessage, e);
				}
			}
		}
		
		return temporaryPostScriptFile;
    }

    private Data[] formOutData(File postScriptFile, File barSizesFile, Data inputData) {
		Data postScriptData = new BasicData(postScriptFile, POSTSCRIPT_MIME_TYPE);
		Dictionary<String, Object> postScriptMetaData = postScriptData.getMetadata();
		postScriptMetaData.put(
			DataProperty.LABEL, "visualized with Horizontal Line Graph");
		postScriptMetaData.put(DataProperty.PARENT, inputData);
		postScriptMetaData.put(DataProperty.TYPE, DataProperty.VECTOR_IMAGE_TYPE);

		Data barSizesData = new BasicData(barSizesFile, CSV_MIME_TYPE);
		Dictionary<String, Object> barSizesMetadata = barSizesData.getMetadata();
		barSizesMetadata.put(DataProperty.LABEL, "bar sizes");
		barSizesMetadata.put(DataProperty.PARENT, inputData);
		barSizesMetadata.put(DataProperty.TYPE, DataProperty.TABLE_TYPE);

        return new Data[] { postScriptData, barSizesData };
    }

    private String getLabel(Dictionary<String, Object> metadata) {
    	Object label = metadata.get(DataProperty.LABEL);

    	if (label != null) {
    		return String.format("data \"%s\"", label.toString());
    	} else {
    		return "input data";
    	}
    }
}