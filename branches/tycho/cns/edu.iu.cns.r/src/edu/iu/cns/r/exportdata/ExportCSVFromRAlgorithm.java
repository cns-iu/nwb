package edu.iu.cns.r.exportdata;

import java.io.File;
import java.io.IOException;
import java.util.Dictionary;

import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

import edu.iu.cns.r.utility.RInstance;

public class ExportCSVFromRAlgorithm implements Algorithm {
	public static final String CSV_MIME_TYPE = "file:text/csv";

	private Data inputData;
	private String variableNameInR;
	private RInstance rInstance;
	private LogService logger;

	public ExportCSVFromRAlgorithm(
			Data rInstanceData, String variableNameInR, RInstance rInstance, LogService logger) {
		this.inputData = rInstanceData;
		this.variableNameInR = variableNameInR;
		this.rInstance = rInstance;
		this.logger = logger;
	}

	public Data[] execute() throws AlgorithmExecutionException {
		try {
			RFileExportLog output = this.rInstance.exportTable(this.variableNameInR);
			output.log(this.logger, true, true);

			return wrapWrittenFileAsOutputData(output.getWrittenFile());
		} catch (IOException e) {
			throw new AlgorithmExecutionException(e.getMessage(), e);
		}
	}

	private Data[] wrapWrittenFileAsOutputData(File writtenFile) {
		Data outputData = new BasicData(writtenFile, CSV_MIME_TYPE);
		Dictionary<String, Object> metadata = outputData.getMetadata();

		String label = String.format("R variable '%s' exported to CSV", this.variableNameInR);
		metadata.put(DataProperty.LABEL, label);
		metadata.put(DataProperty.PARENT, this.inputData);
		metadata.put(DataProperty.TYPE, DataProperty.TABLE_TYPE);

		return new Data[] { outputData };
	}
}