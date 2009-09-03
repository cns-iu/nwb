package edu.iu.nwb.visualization.jungnetworklayout;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.contrib.KKLayout;

/**
 * @author Weixia(Bonnie) Huang 
 */
public class JUNGKamadaKawaiLayout implements AlgorithmFactory {
	    /**
	     * @see org.cishell.framework.algorithm.AlgorithmFactory#createAlgorithm(org.cishell.framework.data.Data[], java.util.Dictionary, org.cishell.framework.CIShellContext)
	     */
	    public Algorithm createAlgorithm(Data[] dm, Dictionary parameters,
	            CIShellContext context) {
	        return new JUNGKamadaKawaiLayoutAlg(dm, parameters, context);
	    }
	    
	    private class JUNGKamadaKawaiLayoutAlg implements Algorithm {
	        Data[] dm;
	        Dictionary parameters;
	        CIShellContext ciContext;
	        
	        public JUNGKamadaKawaiLayoutAlg(Data[] dm, Dictionary parameters,
	        		CIShellContext ciContext) {
	        	this.dm = dm;
	        	this.parameters = parameters;
	            this.ciContext = ciContext;
	        }

	        public Data[] execute() {
	            Graph graph = (Graph) (((Graph) dm[0].getData()).copy());
	            JUNGLayoutGUI gui = new JUNGLayoutGUI("Kamada-Kawai (JUNG)", new KKLayout(graph)); 
	    		//gui.pack();
	    	    //gui.setSize(400, 400) ;
	    	    gui.show();
	    	    return null;
	        }
	    }
}
