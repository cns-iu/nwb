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
	
	public Data[] execute() {
		// TODO Auto-generated method stub
		
		prefuse.data.Graph netGraph = (prefuse.data.Graph)data[0].getData();
		
		
		NetworkProperties np = new NetworkProperties(netGraph);
		
		logger.log(LogService.LOG_INFO, np.toString());
		StringBuffer warning = new StringBuffer();
		if(np.densityInfo().length() < 1){
			if(np.hasSelfLoops() && np.hasParallelEdges())
				warning.append("Did not calculate density due to the presence of self-loops and parallel edges.");
			if(np.hasSelfLoops() && !np.hasParallelEdges())
				warning.append("Did not calculate density due to the presence of self-loops.");
			 if(!np.hasSelfLoops() && np.hasParallelEdges())
				warning.append("Did not calculate density due to the presence of parallel edges.");
			 warning.append(System.getProperty("line.separator"));
		}
		if(np.hasSelfLoops() && !np.isDirected()){
			warning.append("This graph claims to be undirected but has self-loops. Please re-examine your data.");
			warning.append(System.getProperty("line.separator"));
		}
		if(np.hasParallelEdges() && !np.isDirected()){
			warning.append("This graph claims to be undirected but has parallel edges. Please re-examine your data.");
			warning.append(System.getProperty("line.separator"));
		}
		if((np.hasParallelEdges() || np.hasSelfLoops()) && !np.isDirected()){
			warning.append("Many algorithms will not function correctly with this graph.");
			warning.append(System.getProperty("line.separator"));
		}
		
		//logger.log(LogService.LOG_INFO, np.testPrint());
		
		logger.log(LogService.LOG_WARNING, warning.toString());
		
		StringBuffer logFileInfo = new StringBuffer();
		logFileInfo.append(np.toString());
		logFileInfo.append(warning.toString());
		
		File logFile = this.logToFile(logFileInfo);
		
		if(logFile == null){
			logger.log(LogService.LOG_ERROR, "Unable to generate a log file.");
			warning.append(System.getProperty("line.separator"));
			return null;
		}
		
		final Data outputData = new BasicData(logFile,logFile.getClass().getName());
		
		final Dictionary logAttributes = outputData.getMetadata();
		
		logAttributes.put(DataProperty.MODIFIED, new Boolean(true));
		logAttributes.put(DataProperty.PARENT, data[0]);
		logAttributes.put(DataProperty.TYPE, DataProperty.TEXT_TYPE);
		logAttributes.put(DataProperty.LABEL,
				"Graph and Network Analysis Log");
		
		
		np = null;
		return new Data[] {outputData};
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
			logger.log(LogService.LOG_ERROR, e.toString());
			tempFile = new File (tempPath+File.separator+"nwbTemp"+File.separator+"temp.nwb");

		}
		return tempFile;
	}
	
	private File logToFile(StringBuffer logInfo){
		File logFile;
		logFile = this.getTempFile();
		try{
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(logFile)),true);
			pw.write(logInfo.toString());
			pw.close();
			return logFile;
		}catch (FileNotFoundException e){
			logger.log(LogService.LOG_ERROR, "Unable to find the temporary log file.", e);
			return null;
		}catch (IOException ioe){
			logger.log(LogService.LOG_ERROR, "IO Errors while writing to the temporary log file.", ioe);
			return null;
		}
		
		
		
	}

}
