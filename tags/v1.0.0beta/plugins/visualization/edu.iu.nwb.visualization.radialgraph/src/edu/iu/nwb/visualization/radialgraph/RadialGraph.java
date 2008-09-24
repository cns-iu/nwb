package edu.iu.nwb.visualization.radialgraph;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;
import org.osgi.service.log.LogService;

import edu.berkeley.guir.prefuse.graph.Graph;

/**
 * @author Weixia(Bonnie) Huang 
 */
public class RadialGraph implements AlgorithmFactory {
	    
	    /**
	     * @see org.cishell.framework.algorithm.AlgorithmFactory#createAlgorithm(org.cishell.framework.data.Data[], java.util.Dictionary, org.cishell.framework.CIShellContext)
	     */
	    public Algorithm createAlgorithm(Data[] dm, Dictionary parameters,
	            CIShellContext context) {
	        return new RadialGraphAlg(dm, parameters, context);
	    }
	    
	    private class RadialGraphAlg implements Algorithm {
	        Data[] dm;
	        Dictionary parameters;
	        CIShellContext ciContext;
	        LogService log;
	        
	        public RadialGraphAlg(Data[] dm, Dictionary parameters,
	        		CIShellContext ciContext) {
	        	this.dm = dm;
	        	this.parameters = parameters;
	            this.ciContext = ciContext;
	            this.log = (LogService) ciContext.getService(LogService.class.getName());
	        }

	        public Data[] execute() {
	        	log.log(LogService.LOG_INFO, "");
	        	log.log(LogService.LOG_INFO, "Note that the radial graph layout will ignore all but the first weak component in a graph.");
	        	log.log(LogService.LOG_INFO, "Nodes belonging to additional weak components will be placed in the upper-left-hand corner.");
	        	log.log(LogService.LOG_INFO, "Because of this, the Radial Graph layout is not recommended for graphs with more than one weak component");
	        	
	            Graph graph = (Graph) dm[0].getData();
	            RadialGraphVisualization rgv = new RadialGraphVisualization();
	            rgv.setInput(graph);
	            rgv.show();
	            return null;
	        }
	    }
}
