package edu.iu.iv.clustering.betweennesscentrality;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;


import edu.uci.ics.jung.graph.Graph;

public class BetweennessCentralityAlgorithm implements Algorithm {
    Data[] data;
    Dictionary parameters;
    CIShellContext context;
    LogService log;
    
    
    public BetweennessCentralityAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
        this.log = (LogService)context.getService(LogService.class.getName());
        
    }

    public Data[] execute() {
    	double threshold = ((Double)parameters.get("threshold")).doubleValue();
	    boolean normalize = ((Boolean)parameters.get("normalize")).booleanValue();
	    
	    Graph graph = (Graph)((Graph)data[0].getData()).copy();
	    BC.setLogger(log);
        graph = BC.clusterGraph(graph, threshold, normalize);
        
        
        	    
        Data dm = new BasicData(graph, Graph.class.getName());
		dm.getMetaData().put(DataProperty.LABEL,"Betweenness Centrality");
		dm.getMetaData().put(DataProperty.TYPE,DataProperty.NETWORK_TYPE);
		dm.getMetaData().put(DataProperty.PARENT,data[0]);
		    
        
        
	    return new Data[]{dm}; 
	}
}