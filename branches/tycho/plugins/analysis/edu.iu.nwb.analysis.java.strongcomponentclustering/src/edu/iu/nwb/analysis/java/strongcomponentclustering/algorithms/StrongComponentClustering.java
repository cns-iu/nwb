package edu.iu.nwb.analysis.java.strongcomponentclustering.algorithms;

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
import edu.iu.nwb.analysis.java.strongcomponentclustering.components.AnnotateStrongComponents;

public class StrongComponentClustering implements Algorithm, ProgressTrackable {
	Data[] data;
	Dictionary parameters;
	CIShellContext context;
	LogService logger;
	ProgressMonitor monitor;
    
    public StrongComponentClustering(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
        this.logger = (LogService)context.getService(LogService.class.getName());
    }

    public Data[] execute() throws AlgorithmExecutionException {
    	
    	Graph sourceGraph = (Graph)this.data[0].getData();
    	
    	AnnotateStrongComponents asc = new AnnotateStrongComponents(this.getProgressMonitor());
    	Graph targetGraph = asc.strongComponentCalculation(sourceGraph);
    	
    	
    	Data returnData = constructData(this.data[0],targetGraph,prefuse.data.Graph.class.toString(),DataProperty.NETWORK_TYPE,"Graph with Annotated Strong Components.");
    	
        return new Data[]{returnData};
    }
    
    public ProgressMonitor getProgressMonitor() {
		// TODO Auto-generated method stub
		return this.monitor;
	}

	public void setProgressMonitor(ProgressMonitor monitor) {
		this.monitor = monitor;	
	}
	
	private Data constructData(Data parent, Object obj, String className, String type, String label){
		Data outputData = new BasicData(obj,className);
		Dictionary dataAttributes = outputData.getMetadata();
		dataAttributes.put(DataProperty.MODIFIED, new Boolean(true));
		dataAttributes.put(DataProperty.PARENT, parent);
		dataAttributes.put(DataProperty.TYPE, type);
		dataAttributes.put(DataProperty.LABEL,label);

		return outputData;
	}
	
}