package edu.iu.iv.search.p2p.bfs;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.Data;
import org.osgi.service.log.LogService;

import edu.uci.ics.jung.graph.Graph;

public class BreadthFirstSearchAlgorithm implements Algorithm {
    Data[] data;
    Dictionary parameters;
    CIShellContext context;
    LogService log;
    
    public BreadthFirstSearchAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
        this.log = (LogService) context.getService(LogService.class.getName());
    }

    public Data[] execute() {
    	int sourceNode = ((Integer) getValue("source")).intValue() -1;
    	int targetNode = ((Integer) getValue("target")).intValue() -1;
	    double threshold = ((Double) getValue("threshold")).doubleValue();
	    	    
		println("Input Parameters: source Node = " + sourceNode + 
				", target Node = " + targetNode + 
				", threshold = " + threshold);
		Graph graph = (Graph)data[0].getData();
		BreadthFirstSearch bfs = new BreadthFirstSearch(graph, threshold);
        bfs.setLogger(log);
        
		boolean is_done = bfs.searchNetwork(sourceNode, targetNode);
		if (is_done) {
			println("Search Cost : "+bfs.getSearchCost());
		}else {
			println("Search failed: "+bfs.getFailReason());
		}

		return null;
    }
    private Object getValue(String key) {
	    return parameters.get(key);
	}
	
	private void println(String string) {
        log.log(LogService.LOG_INFO, string);
    }
}