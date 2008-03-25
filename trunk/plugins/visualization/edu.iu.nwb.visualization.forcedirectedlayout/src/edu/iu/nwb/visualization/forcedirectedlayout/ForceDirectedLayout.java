package edu.iu.nwb.visualization.forcedirectedlayout;


import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;

import edu.berkeley.guir.prefuse.graph.Graph;
import edu.berkeley.guir.prefusex.force.DragForce;
import edu.berkeley.guir.prefusex.force.ForceSimulator;
import edu.berkeley.guir.prefusex.force.NBodyForce;
import edu.berkeley.guir.prefusex.force.SpringForce;

/**
 * @author Weixia(Bonnie) Huang 
 */
public class ForceDirectedLayout implements AlgorithmFactory {
	    
	    /**
	     * @see org.cishell.framework.algorithm.AlgorithmFactory#createAlgorithm(org.cishell.framework.data.Data[], java.util.Dictionary, org.cishell.framework.CIShellContext)
	     */
	    public Algorithm createAlgorithm(Data[] dm, Dictionary parameters,
	            CIShellContext context) {
	        return new ForceDirectedLayoutAlg(dm, parameters, context);
	    }
	    
	    private class ForceDirectedLayoutAlg implements Algorithm {
	        Data[] dm;
	        Dictionary parameters;
	        CIShellContext ciContext;
	        
	        public ForceDirectedLayoutAlg(Data[] dm, Dictionary parameters,
	        		CIShellContext ciContext) {
	        	this.dm = dm;
	        	this.parameters = parameters;
	            this.ciContext = ciContext;
	        }

	        public Data[] execute() {
	            Graph graph = (Graph) dm[0].getData();
	            ForceSimulator fsim = new ForceSimulator();
	            fsim.addForce(new NBodyForce(-0.4f, -1f, 0.9f));
	            fsim.addForce(new SpringForce(4E-5f, 75f));
	            fsim.addForce(new DragForce(-0.005f));

	            ForceDirectedLayoutVisualization viz = 
	            	new ForceDirectedLayoutVisualization(graph, fsim);
	            viz.show();
	            return null;
	        }
	    }
}
