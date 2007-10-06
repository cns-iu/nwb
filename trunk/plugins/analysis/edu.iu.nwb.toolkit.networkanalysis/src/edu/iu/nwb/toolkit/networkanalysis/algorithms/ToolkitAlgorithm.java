package edu.iu.nwb.toolkit.networkanalysis.algorithms;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.Data;
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
			warning.append("Could not calculate density due to the presence of self-loops or parallel edges.");
		}
		if(np.hasSelfLoops() && !np.isDirected()){
			warning.append("This graph claims to be undirected but has self-loops. Please re-examine your data.\n");
		}
		if(np.hasParallelEdges() && !np.isDirected()){
			warning.append("This graph claims to be undirected but has parallel edges. Please re-examine your data.\n");
		}
		if((np.hasParallelEdges() || np.hasSelfLoops()) && !np.isDirected())
			warning.append("Many algorithms will not function correctly with this graph.\n");
		logger.log(LogService.LOG_WARNING, warning.toString());
		
		return null;
	}

}
