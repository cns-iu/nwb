package edu.iu.sci2.visualization.horizontallinegraph;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
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

import com.google.common.io.Files;

public class HorizontalLineGraphAlgorithm implements Algorithm {
	 // TODO import external settings if possible
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
    private double pageWidth;
    private double pageHeight;
    private boolean shouldScaleOutput;
	private String query;
    
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
    		String query,
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
        this.query = query;
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
    		csvWriter.close();

    		File temporaryPostScriptFile =
    			writePostScriptCodeToTemporaryFile(postScriptCode, "horizontal-line-graph");

			return formOutData(temporaryPostScriptFile, barSizesFile, inputData);
    	} catch (IOException e) {
    		String message = String.format(
    			"An error occurred when creating %s.",
    			getLabel(this.inputData.getMetadata()));
    		message += e.getMessage();
    		throw new AlgorithmExecutionException(message, e);
    	} catch (PostScriptCreationException e) {
    		String exceptionMessage = String.format(
    			"An error occurred when creating the PostScript for the %s.",
    			getLabel(this.inputData.getMetadata()) + e.getMessage());
    		throw new AlgorithmExecutionException(exceptionMessage, e);
    	} finally {
    		if (csvWriter != null) {
    			try {
    				csvWriter.close();
    			} catch (IOException e) {
    				throw new AlgorithmExecutionException(e.getMessage(), e);
    			}
    		}
    	}
    }

	private String createPostScriptCode(CSVWriter csvWriter)
			throws PostScriptCreationException {
		DocumentPostScriptCreator postScriptCreator = new DocumentPostScriptCreator(
				labelColumn, startDateColumn, endDateColumn, sizeByColumn,
				startDateFormat, endDateFormat, query, pageWidth, pageHeight,
				shouldScaleOutput);

		String postScriptCode = postScriptCreator.createPostScript(this.inputTable, this.logger, csvWriter);

		return postScriptCode;
	}

    private static File writePostScriptCodeToTemporaryFile(
    		String postScriptCode, String temporaryFileName) throws IOException {
    	File psFile = File.createTempFile(temporaryFileName, EPS_FILE_EXTENSION);
    	psFile.deleteOnExit();
    	
    	Files.write(postScriptCode, psFile, Charset.defaultCharset());
    	
    	return psFile;
    }

    private static Data[] formOutData(File postScriptFile, File barSizesFile, Data inputData) {
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

    private static String getLabel(Dictionary<String, Object> metadata) {
    	Object label = metadata.get(DataProperty.LABEL);

    	if (label != null) {
    		return String.format("data \"%s\"", label.toString());
    	} else {
    		return "input data";
    	}
    }
}