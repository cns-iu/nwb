package edu.iu.nwb.visualization.radialgraphbeta;

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
public class RadialGraph implements AlgorithmFactory {
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
	            RadialGraphVisualization.create(graph, "label");
	            return null;
	        }
	    }
}
