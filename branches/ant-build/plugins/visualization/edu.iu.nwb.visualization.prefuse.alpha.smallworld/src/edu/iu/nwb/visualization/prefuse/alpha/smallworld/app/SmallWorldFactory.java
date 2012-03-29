package edu.iu.nwb.visualization.prefuse.alpha.smallworld.app;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;

import edu.berkeley.guir.prefuse.graph.Graph;
import edu.iu.nwb.visualization.prefuse.alpha.smallworld.types.BasicGraphReader;





/**
 * @author Weixia(Bonnie) Huang 
 */
public class SmallWorldFactory implements AlgorithmFactory {
	    
	    /**
	     * @see org.cishell.framework.algorithm.AlgorithmFactory#createAlgorithm(org.cishell.framework.data.Data[], java.util.Dictionary, org.cishell.framework.CIShellContext)
	     */
	    public Algorithm createAlgorithm(Data[] dm, Dictionary parameters,
	            CIShellContext context) {
	        return new GraphViewAlgorithm(dm, parameters, context);
	    }
	    
	    private class GraphViewAlgorithm implements Algorithm {
	        Data[] dm;
	        Dictionary parameters;
	        CIShellContext ciContext;
	        
	        public GraphViewAlgorithm(Data[] dm, Dictionary parameters,
	        		CIShellContext ciContext) {
	        	this.dm = dm;
	        	this.parameters = parameters;
	            this.ciContext = ciContext;
	        }

	        public Data[] execute() {
	            Graph graph = (Graph) dm[0].getData();
	            new SmallWorldFrame(new BasicGraphReader().convertGraph(graph));
	            return null;
	        }
	    }
}
