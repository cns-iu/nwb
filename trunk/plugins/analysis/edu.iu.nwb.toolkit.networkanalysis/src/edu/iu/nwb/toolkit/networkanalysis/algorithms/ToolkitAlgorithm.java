package edu.iu.nwb.toolkit.networkanalysis.algorithms;

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
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

import edu.iu.nwb.toolkit.networkanalysis.analysis.NetworkProperties;



public class ToolkitAlgorithm implements Algorithm{
	Data[] data;
    Dictionary parameters;
    CIShellContext ciContext;
    LogService logger;
	
	public ToolkitAlgorithm(Data[] dm, Dictionary parameters, CIShellContext cContext){
		this.data = dm;
		this.parameters = parameters;
		this.ciContext = cContext;
		logger = (LogService)ciContext.getService(LogService.class.getName());
	}
	
	public Data[] execute() throws AlgorithmExecutionException{
		prefuse.data.Graph netGraph = (prefuse.data.Graph)data[0].getData();
		
		StringBuffer log = NetworkProperties.calculateNetworkProperties(netGraph);
		
		logger.log(LogService.LOG_INFO, log.toString());
		
		File logFile = this.logToFile(log);
	
		Data outputData = createOutputData(logFile);
		return new Data[] {outputData};
	}
	
	private Data createOutputData(File logFile) {
		Data outputData = new BasicData(logFile,logFile.getClass().getName());
		
		final Dictionary logAttributes = outputData.getMetadata();
		
		logAttributes.put(DataProperty.MODIFIED, new Boolean(true));
		logAttributes.put(DataProperty.PARENT, data[0]);
		logAttributes.put(DataProperty.TYPE, DataProperty.TEXT_TYPE);
		logAttributes.put(DataProperty.LABEL,
				"Graph and Network Analysis Log");
		return outputData;
	}
	
	private File getTempFile(){
		File tempFile;
    
		String tempPath = System.getProperty("java.io.tmpdir");
		File tempDir = new File(tempPath+File.separator+"temp");
		if(!tempDir.exists())
			tempDir.mkdir();
		try{
			tempFile = File.createTempFile("NWB-Session-", ".nwb", tempDir);
		
		}catch (IOException e){
			logger.log(LogService.LOG_ERROR, e.toString(), e);
			tempFile = new File (tempPath+File.separator+"nwbTemp"+File.separator+"temp.nwb");

		}
		return tempFile;
	}
	
	private File logToFile(final StringBuffer logInfo) throws AlgorithmExecutionException {
		File logFile;
		logFile = this.getTempFile();
		try{
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(logFile)),true);
			pw.write(logInfo.toString());
			pw.close();
			return logFile;
		}catch (FileNotFoundException e){
			throw new AlgorithmExecutionException("Unable to generate a log file.");
		}catch (IOException ioe){
			throw new AlgorithmExecutionException("Unable to generate a log file.");
		}
		
	}

}
