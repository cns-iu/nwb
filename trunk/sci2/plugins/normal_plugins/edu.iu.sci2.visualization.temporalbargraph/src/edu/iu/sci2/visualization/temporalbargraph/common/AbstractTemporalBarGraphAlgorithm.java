package edu.iu.sci2.visualization.temporalbargraph.common;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Iterator;
import java.util.List;

import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.utilities.FileUtilities;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import prefuse.data.Tuple;

import au.com.bytecode.opencsv.CSVWriter;

import com.google.common.io.Files;

public abstract class AbstractTemporalBarGraphAlgorithm implements Algorithm {
	// SOMEDAY import external settings if possible
	public static final String POSTSCRIPT_MIME_TYPE = "file:text/ps";
	public static final String CSV_MIME_TYPE = "file:text/csv";
	public static final String EPS_FILE_SUFFIX = ".eps";
	public static final String CSV_FILE_SUFFIX = ".csv";

	@Override
	public Data[] execute() throws AlgorithmExecutionException {
		LogService logger = getLogger();
		Data inputData = getInputData();

		logger.log(LogService.LOG_INFO,
				"Creating PostScript. May take a few moments...");
		CSVWriter csvWriter = null;

		try {
			File barSizesFile = FileUtilities
					.createTemporaryFileInDefaultTemporaryDirectory("barSizes",
							CSV_FILE_SUFFIX);
			csvWriter = new CSVWriter(new FileWriter(barSizesFile));
			String[] header = new String[] { "Record Name", "Width", "Height",
					"Area (Width x Height)" };
			csvWriter.writeNext(header);
			String postScriptCode = createPostScriptCode(csvWriter);
			csvWriter.close();

			File temporaryPostScriptFile = writePostScriptCodeToTemporaryFile(
					postScriptCode, "horizontal-line-graph");

			return formOutData(temporaryPostScriptFile, barSizesFile, inputData);
		} catch (IOException e) {
			String message = String.format(
					"An error occurred when creating %s.",
					getLabel(inputData.getMetadata()));
			message += e.getMessage();
			throw new AlgorithmExecutionException(message, e);
		} catch (PostScriptCreationException e) {
			String exceptionMessage = String
					.format("An error occurred when creating the PostScript for the %s.",
							getLabel(inputData.getMetadata()) + e.getMessage());
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

	protected abstract LogService getLogger();

	protected abstract Data getInputData();

	protected abstract String createPostScriptCode(CSVWriter csvWriter)
			throws PostScriptCreationException;

	private static File writePostScriptCodeToTemporaryFile(
			String postScriptCode, String temporaryFileName) throws IOException {
		File psFile = File.createTempFile(temporaryFileName, EPS_FILE_SUFFIX);
		psFile.deleteOnExit();

		Files.write(postScriptCode, psFile, Charset.defaultCharset());

		return psFile;
	}

	private static Data[] formOutData(File postScriptFile, File barSizesFile,
			Data inputData) {

		Data postScriptData = new BasicData(postScriptFile,
				POSTSCRIPT_MIME_TYPE);

		Dictionary<String, Object> postScriptMetaData = postScriptData
				.getMetadata();
		postScriptMetaData.put(DataProperty.LABEL,
				"visualized with Temporal Bar Graph");
		postScriptMetaData.put(DataProperty.PARENT, inputData);
		postScriptMetaData.put(DataProperty.TYPE,
				DataProperty.VECTOR_IMAGE_TYPE);

		Data barSizesData = new BasicData(barSizesFile, CSV_MIME_TYPE);

		Dictionary<String, Object> barSizesMetadata = barSizesData
				.getMetadata();
		barSizesMetadata.put(DataProperty.LABEL, "bar sizes");
		barSizesMetadata.put(DataProperty.PARENT, inputData);
		barSizesMetadata.put(DataProperty.TYPE, DataProperty.TABLE_TYPE);

		return new Data[] { postScriptData, barSizesData };
	}

	private static String getLabel(Dictionary<String, Object> metadata) {
		Object label = metadata.get(DataProperty.LABEL);

		if (label != null) {
			return String.format("data \"%s\"", label.toString());
		}
		
		return "input data";
	}

	protected static List<Record> readRecordsFromTable(Table table,
			LogService logger, String labelColumn, String startDateColumn,
			String endDateColumn, String sizeByColumn, String startDateFormat,
			String endDateFormat, String categoryColumn) {
		List<Record> workingRecordSet = new ArrayList<Record>();

		for (Iterator<?> rows = table.tuples(); rows.hasNext();) {
			Tuple row = (Tuple) rows.next();

			try {

				Record newRecord = new Record(row, labelColumn,
						startDateColumn, endDateColumn, sizeByColumn,
						startDateFormat, endDateFormat, categoryColumn);

				workingRecordSet.add(newRecord);

			} catch (InvalidRecordException e) {
				logger.log(LogService.LOG_WARNING, "An invalid record will be ignored." + System.getProperty("line.separator") + e.getMessage(), e);
			}
		}

		return workingRecordSet;
	}
}