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
    Dictionary<String, Object> parameters;
    CIShellContext context;
    LogService logger;

    public BetweennessCentralityAlgorithm(
    		Data[] data, Dictionary<String, Object> parameters, CIShellContext ciShellContext) {
        this.data = data;
        this.parameters = parameters;
        this.context = ciShellContext;
        this.logger = (LogService) ciShellContext.getService(LogService.class.getName());
    }

    public Data[] execute() {
    	double threshold = ((Double) parameters.get("threshold")).doubleValue();
	    boolean normalize = ((Boolean) parameters.get("normalize")).booleanValue();

	    Graph graph = (Graph) ((Graph) data[0].getData()).copy();
	    BC.setLogger(logger);
        graph = BC.clusterGraph(graph, threshold, normalize);

        Data dm = new BasicData(graph, Graph.class.getName());
		dm.getMetadata().put(DataProperty.LABEL, "Betweenness Centrality");
		dm.getMetadata().put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
		dm.getMetadata().put(DataProperty.PARENT, data[0]);

	    return new Data[]{dm}; 
	}
}