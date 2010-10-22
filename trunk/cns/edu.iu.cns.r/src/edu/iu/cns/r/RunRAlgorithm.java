package edu.iu.cns.r;

import java.io.IOException;

import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.Data;
import org.osgi.service.log.LogService;

import edu.iu.cns.r.utility.RInstance;
import edu.iu.cns.r.utility.ROutput;

public class RunRAlgorithm implements Algorithm {
	private RInstance rInstance;
	private LogService logger;

	public RunRAlgorithm(RInstance rInstance, LogService logger) {
		this.rInstance = rInstance;
	}

	public Data[] execute() throws AlgorithmExecutionException {
		try {
			ROutput messagesFromRunningRGUI = this.rInstance.runRGUI();
			messagesFromRunningRGUI.log(this.logger, true, true);
		} catch (IOException e) {
			throw new AlgorithmExecutionException(e.getMessage(), e);
		}

		return null;
	}
}