package edu.iu.nwb.analysis.extractdirectednetfromtable.algorithms;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Properties;

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
import edu.iu.nwb.analysis.extractnetfromtable.components.GraphContainer;
import edu.iu.nwb.analysis.extractnetfromtable.components.InvalidColumnNameException;
import edu.iu.nwb.analysis.extractnetfromtable.components.PropertyHandler;

public class ExtractDirectedNetworkAlgorithm implements Algorithm, ProgressTrackable {
    Data[] data;
    Dictionary parameters;
    CIShellContext context;
    LogService logger;
    ProgressMonitor progressMonitor;
    
    
    
    public ProgressMonitor getProgressMonitor() {
		// TODO Auto-generated method stub
		return this.progressMonitor;
	}

	public void setProgressMonitor(ProgressMonitor monitor) {
		this.progressMonitor = monitor;
	}

	public ExtractDirectedNetworkAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
        this.logger = (LogService) context.getService(LogService.class.getName());
    }

    public Data[] execute() throws AlgorithmExecutionException{
    	final prefuse.data.Table dataTable = (prefuse.data.Table) data[0].getData();
    	
    	String delimiter = null;
    	String sourceColumn = null;
    	String  targetColumn = null;
    	Object functionFile = null;
    	Properties functions = null;
    	
    	delimiter = this.parameters.get("delimiter").toString();
    	sourceColumn = this.parameters.get("sourceColumn").toString();
    	targetColumn = this.parameters.get("targetColumn").toString();
    	functionFile = this.parameters.get("aff");
    	
    	if(functionFile != null)
    	{
    		functions = PropertyHandler.getProperties(functionFile.toString(), this.logger);
    	}
    	
    	try
    	{
    		GraphContainer gc = GraphContainer.initializeGraph(dataTable, sourceColumn,
    			targetColumn, true, functions, this.logger, this.progressMonitor);
    		
    		Graph directedNetwork =
    			gc.buildGraph(sourceColumn, targetColumn, delimiter, this.logger);
    	
    		Data network = ExtractDirectedNetworkAlgorithm.constructData(data[0],
    			(Object)directedNetwork, prefuse.data.Graph.class.toString(), DataProperty.NETWORK_TYPE, 
    			"Network with directed edges from " + sourceColumn + " to " + targetColumn+ ".");
    	
    		return new Data[] {network};
    	}
    	catch(InvalidColumnNameException ice)
    	{
    		throw new AlgorithmExecutionException(ice.getMessage(), ice);
    	}
    	
    }
    
    private static Data constructData(Data d, Object obj, String className, String type, String label){
    	Data outputData = new BasicData(obj,className);
		Dictionary dataAttributes = outputData.getMetadata();
		dataAttributes.put(DataProperty.MODIFIED, new Boolean(true));
		dataAttributes.put(DataProperty.PARENT, d);
		dataAttributes.put(DataProperty.TYPE, type);
		dataAttributes.put(DataProperty.LABEL,label);

		return outputData;
    }
    

    
}