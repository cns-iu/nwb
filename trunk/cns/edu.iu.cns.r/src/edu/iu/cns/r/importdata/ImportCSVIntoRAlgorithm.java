package edu.iu.cns.r.importdata;

import java.io.File;
import java.io.IOException;

import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.Data;
import org.cishell.utilities.FileCopyingException;
import org.osgi.service.log.LogService;

import edu.iu.cns.r.utility.RInstance;
import edu.iu.cns.r.utility.ROutput;

public class ImportCSVIntoRAlgorithm implements Algorithm {
	private String variableNameInR;
	private RInstance rInstance;
	private File tableFileToImport;
	private LogService logger;

	public ImportCSVIntoRAlgorithm(
			String variableNameInR, RInstance rInstance, File tableToImport, LogService logger) {
		this.variableNameInR = variableNameInR;
		this.rInstance = rInstance;
		this.tableFileToImport = tableToImport;
		this.logger = logger;
	}

	public Data[] execute() throws AlgorithmExecutionException {
		try {
			ROutput output =
				this.rInstance.importTable(this.tableFileToImport, true, this.variableNameInR);
			output.log(this.logger, true, true);
			String logMessage = String.format(
				"The file '%s' was successfully read into your R instance as the variable %s!",
				this.tableFileToImport.getName(),
				this.variableNameInR);
			this.logger.log(LogService.LOG_INFO, logMessage);
		} catch (FileCopyingException e) {
			throw new AlgorithmExecutionException(e.getMessage(), e);
		} catch (IOException e) {
			throw new AlgorithmExecutionException(e.getMessage(), e);
		}

		return null;
	}
}