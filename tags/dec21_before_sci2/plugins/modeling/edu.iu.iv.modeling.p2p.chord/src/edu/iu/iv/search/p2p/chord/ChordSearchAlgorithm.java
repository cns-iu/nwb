package edu.iu.iv.search.p2p.chord;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.Data;
import org.osgi.service.log.LogService;

import edu.uci.ics.jung.graph.Graph;

public class ChordSearchAlgorithm implements Algorithm {
	Data[] data;
    Dictionary parameters;
    CIShellContext context;
    private LogService logger;
    
    public ChordSearchAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
    }
	public Data[] execute() throws AlgorithmExecutionException {
		int sourceNode = ((Integer) parameters.get("source")).intValue()-1;
        int targetNode = ((Integer) parameters.get("target")).intValue()-1;
        
		println("Input Parameters: source Node = " + sourceNode + 
				", target Node = " + targetNode);	
		
		//ChordSearch cs = new ChordSearch(graph);
		ChordSearch cs = new ChordSearch((Graph)data[0].getData(),logger);
		boolean is_done = cs.searchNetwork(sourceNode, targetNode);
		if (is_done) {
			println("Search Cost : "+cs.getSearchCost());
		} else {
			println("Search failed. "+cs.getFailReason());
		}
		return null;
		
	}
	
	 private void println(String string) {
		 LogService log = (LogService) context.getService(LogService.class.getName());
	        log.log(LogService.LOG_INFO, string);
    }
}
