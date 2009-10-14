package edu.iu.nwb.toolkit.networkanalysis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

import prefuse.data.Graph;
import edu.iu.nwb.toolkit.networkanalysis.analysis.NetworkProperties;
import org.cishell.utilities.FileUtilities;


public class ToolkitAlgorithm implements Algorithm {
	private Data inputData;
	private LogService logger;
	
    
    public static class Factory implements AlgorithmFactory {
    	public Algorithm createAlgorithm(
    			Data[] data,
    			Dictionary parameters,
    			CIShellContext ciShellContext) {
    		return new ToolkitAlgorithm(data, ciShellContext);
    	}
    }
	public ToolkitAlgorithm(Data[] data, CIShellContext ciShellContext) {
		this.inputData = data[0];
		
		this.logger =
			(LogService) ciShellContext.getService(LogService.class.getName());
	}
	
	
	public Data[] execute() throws AlgorithmExecutionException{
		Graph netGraph = (Graph) inputData.getData();
		
		StringBuffer log =
			NetworkProperties.calculateNetworkProperties(netGraph);
		
		logger.log(LogService.LOG_INFO, log.toString());
		
		File logFile = logToFile(log);
	
		Data outputData = createOutputData(logFile);
		
		return new Data[] {outputData};
	}
	
	private Data createOutputData(File logFile) {
		Data outputData = new BasicData(logFile, logFile.getClass().getName());
		
		final Dictionary logAttributes = outputData.getMetadata();
		
		logAttributes.put(DataProperty.MODIFIED, Boolean.TRUE);
		logAttributes.put(DataProperty.PARENT, inputData);
		logAttributes.put(DataProperty.TYPE, DataProperty.TEXT_TYPE);
		logAttributes.put(
				DataProperty.LABEL, "Graph and Network Analysis Log");
		
		return outputData;
	}
	
	private File logToFile(final StringBuffer logInfo)
			throws AlgorithmExecutionException {		
		try {
			File logFile =
				FileUtilities.createTemporaryFileInDefaultTemporaryDirectory(
						"toolkitErrorLog", "txt");
			
			PrintWriter writer =
				new PrintWriter(
						new BufferedWriter(new FileWriter(logFile)),
						true);
			writer.write(logInfo.toString());
			writer.close();
			
			return logFile;
		} catch (FileNotFoundException e) {
			throw new AlgorithmExecutionException(
					"Unable to generate a log file.", e);
		} catch (IOException e) {
			throw new AlgorithmExecutionException(
					"Unable to generate a log file.", e);
		}		
	}
}
