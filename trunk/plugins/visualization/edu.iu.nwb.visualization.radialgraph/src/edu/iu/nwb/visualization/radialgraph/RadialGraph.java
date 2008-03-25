package edu.iu.nwb.visualization.radialgraph;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;

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
	        
	        public RadialGraphAlg(Data[] dm, Dictionary parameters,
	        		CIShellContext ciContext) {
	        	this.dm = dm;
	        	this.parameters = parameters;
	            this.ciContext = ciContext;
	        }

	        public Data[] execute() {
	            Graph graph = (Graph) dm[0].getData();
	            RadialGraphVisualization rgv = new RadialGraphVisualization();
	            rgv.setInput(graph);
	            rgv.show();
	            return null;
	        }
	    }
}
