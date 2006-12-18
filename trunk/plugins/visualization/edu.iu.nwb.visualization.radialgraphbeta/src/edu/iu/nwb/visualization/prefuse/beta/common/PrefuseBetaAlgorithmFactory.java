package edu.iu.nwb.visualization.prefuse.beta.common;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;
import org.osgi.service.metatype.MetaTypeProvider;

import prefuse.data.Graph;



/**
 * @author Weixia(Bonnie) Huang 
 */
public class PrefuseBetaAlgorithmFactory implements AlgorithmFactory {
	
	    /**
	     * @see org.cishell.framework.algorithm.AlgorithmFactory#createParameters(org.cishell.framework.data.Data[])
	     */
	    public MetaTypeProvider createParameters(Data[] dm) {
	        return null;
	    }
	    
	    /**
	     * @see org.cishell.framework.algorithm.AlgorithmFactory#createAlgorithm(org.cishell.framework.data.Data[], java.util.Dictionary, org.cishell.framework.CIShellContext)
	     */
	    public Algorithm createAlgorithm(Data[] dm, Dictionary parameters,
	            CIShellContext context) {
	        return new GraphViewAlgorithm(dm, parameters, context, getVisualization());
	    }
	    
	    protected PrefuseBetaVisualization getVisualization() {
	    	return null;
	    }
	    
	    private class GraphViewAlgorithm implements Algorithm {
	        Data[] dm;
	        Dictionary parameters;
	        CIShellContext ciContext;
			private PrefuseBetaVisualization visualization;
	        
	        public GraphViewAlgorithm(Data[] dm, Dictionary parameters,
	        		CIShellContext ciContext, PrefuseBetaVisualization visualization) {
	        	this.dm = dm;
	        	this.parameters = parameters;
	            this.ciContext = ciContext;
	            this.visualization = visualization;
	        }

	        public Data[] execute() {
	            Graph graph = (Graph) dm[0].getData();
	            visualization.create(graph, "label");
	            return null;
	        }
	    }
}
