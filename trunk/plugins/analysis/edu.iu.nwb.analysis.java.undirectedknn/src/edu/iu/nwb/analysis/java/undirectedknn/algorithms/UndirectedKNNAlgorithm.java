package edu.iu.nwb.analysis.java.undirectedknn.algorithms;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.ProgressMonitor;
import org.cishell.framework.algorithm.ProgressTrackable;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

import prefuse.data.Graph;
import edu.iu.nwb.analysis.java.undirectedknn.components.KNNCalculator;

public class UndirectedKNNAlgorithm implements Algorithm, ProgressTrackable {
	private Data[] data;
    private Dictionary parameters;
    private CIShellContext context;
    private ProgressMonitor monitor;
    private LogService logger;
    
    public UndirectedKNNAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
        this.logger = (LogService)context.getService(LogService.class.getName());
    }

    public Data[] execute() throws AlgorithmExecutionException {
    	
    	final Graph originalData = (Graph)this.data[0].getData();
    	this.monitor.start(ProgressMonitor.WORK_TRACKABLE, 2*originalData.getNodeCount());
    	
    	KNNCalculator knnCalc = new KNNCalculator(this.getProgressMonitor());
    	Graph annotatedGraph = knnCalc.createAnnotatedGraph(originalData);
    	
    	try{
    		Date d = new Date();
    		File degreeDegreeCorrelationFile = File.createTempFile(new Long(d.getTime()).toString(), ".txt");
    		knnCalc.calculateKNN(originalData, annotatedGraph,degreeDegreeCorrelationFile);
    		Data returnData1 = constructData(data[0],annotatedGraph,prefuse.data.Graph.class.toString(),
    				DataProperty.NETWORK_TYPE,"Network with knn attribute added to node list");
    		Data returnData2 = constructData(data[0],degreeDegreeCorrelationFile,"file:text/grace",DataProperty.PLOT_TYPE,"Degree-degree correlation normalized to expected value of total degree for uncorrelated network");
    		monitor.done();
    		return new Data[]{returnData1,returnData2};
    	}catch(InterruptedException ie){
    		throw new AlgorithmExecutionException("Execution was unexpectedly interrupted.",ie);
    	}catch(IOException ioe){
    		throw new AlgorithmExecutionException("Error creating the file for Degee-Degree Correlation.",ioe);
    	}
    
    }
    
    private static Data constructData(Data parent, Object obj, String className, String type, String label){
		Data outputData = new BasicData(obj,className);
		Dictionary dataAttributes = outputData.getMetadata();
		dataAttributes.put(DataProperty.MODIFIED, new Boolean(true));
		dataAttributes.put(DataProperty.PARENT, parent);
		dataAttributes.put(DataProperty.TYPE, type);
		dataAttributes.put(DataProperty.LABEL,label);

		return outputData;
	}
    
    public ProgressMonitor getProgressMonitor() {
		return this.monitor;
	}

	public void setProgressMonitor(ProgressMonitor monitor) {
		this.monitor = monitor;
	}
}