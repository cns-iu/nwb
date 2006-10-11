package edu.iu.nwb.visualization.jungnetworklayout;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;
import org.osgi.service.metatype.MetaTypeProvider;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.contrib.CircleLayout;

/**
 * @author Weixia(Bonnie) Huang 
 */
public class JUNGCircleLayout implements AlgorithmFactory {

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
	        return new JUNGCircleLayoutAlg(dm, parameters, context);
	    }
	    
	    private class JUNGCircleLayoutAlg implements Algorithm {
	        Data[] dm;
	        Dictionary parameters;
	        CIShellContext ciContext;
	        
	        public JUNGCircleLayoutAlg(Data[] dm, Dictionary parameters,
	        		CIShellContext ciContext) {
	        	this.dm = dm;
	        	this.parameters = parameters;
	            this.ciContext = ciContext;
	        }

	        public Data[] execute() {
	            Graph graph = (Graph) dm[0].getData();
	            ;
	            JUNGLayoutGUI gui = new JUNGLayoutGUI("Circle Layout", new CircleLayout(graph)); 
	    		gui.pack();
	    	    gui.setSize(400, 400) ;
	    	    gui.show();
	    	    return null;
	        }
	    }
}
