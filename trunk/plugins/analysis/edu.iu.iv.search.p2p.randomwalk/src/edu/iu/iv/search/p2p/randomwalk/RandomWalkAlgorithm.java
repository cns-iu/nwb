package edu.iu.iv.search.p2p.randomwalk;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.Data;
import org.osgi.service.log.LogService;

import edu.uci.ics.jung.graph.Graph;

public class RandomWalkAlgorithm implements Algorithm {
    Data[] data;
    Dictionary parameters;
    CIShellContext context;
    LogService log;
    
    public RandomWalkAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
        this.log = (LogService)context.getService(LogService.class.getName());
    }

    public Data[] execute() {
    	// must minus 1 to source and target since nodes are zero-based
	    int sourceNode = ((Integer) getValue("sourceNode")).intValue() -1;
	    int targetNode = ((Integer) getValue("targetNode")).intValue() -1;
	    int numWalkers = ((Integer) getValue("walkers")).intValue() -1;
	    
	    println("Input Parameters: source Node = " + sourceNode + 
				", target Node = " + targetNode +
				", number of Walkers = " + numWalkers);
	    Graph graph = (Graph)data[0].getData();
		RandomWalk rw = new RandomWalk(graph, numWalkers);
		rw.setLogger(log);
		boolean is_done = rw.searchNetwork(sourceNode, targetNode);
		if (is_done) {
			println("Search Cost : "+rw.getSearchCost());
		} else {
			println("Search failed: "+rw.getFailReason());
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