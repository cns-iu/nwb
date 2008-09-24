package edu.iu.nwb.analysis.java.nodeindegree;

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
import edu.iu.nwb.analysis.java.nodedegree.components.DegreeType;
import edu.iu.nwb.analysis.java.nodedegree.components.NodeDegreeAnnotator;

public class NodeIndegree implements Algorithm, ProgressTrackable {
	private Data[] data;
	private Dictionary parameters;
	private CIShellContext context;
	private ProgressMonitor monitor;
	private LogService logger;
    
    public NodeIndegree(Data[] data, Dictionary parameters, CIShellContext context) {
    	this.data = data;
		this.parameters = parameters;
		this.context = context;
		this.logger = (LogService)context.getService(LogService.class.getName());
    }
    	
    
    public Data[] execute() throws AlgorithmExecutionException {

    		Graph originalGraph = (Graph)this.data[0].getData();
    		
    		NodeDegreeAnnotator nodeAnnotator = new NodeDegreeAnnotator(this.getProgressMonitor(),DegreeType.inDegree);

    		Graph annotatedGraph = nodeAnnotator.createAnnotatedGraph(originalGraph);

    		

    		try {
    			monitor.start(ProgressMonitor.WORK_TRACKABLE, 2*originalGraph.getNodeCount());
    			nodeAnnotator.annotateGraph(originalGraph, annotatedGraph);
    		} catch (InterruptedException e) {
    			throw new AlgorithmExecutionException("Execution was unexpectedly interrupted.",e);
    		}

    		Data returnData = constructData(data[0],annotatedGraph,prefuse.data.Graph.class.toString(),
    				DataProperty.NETWORK_TYPE,"Network with indegree attribute added to node list");

    		monitor.done();
    		return new Data[]{returnData};
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
    		// TODO Auto-generated method stub
    		return this.monitor;
    	}

    	public void setProgressMonitor(ProgressMonitor monitor) {
    		// TODO Auto-generated method stub
    		this.monitor = monitor;
    	}


}