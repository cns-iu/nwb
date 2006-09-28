package edu.iu.nwb.visualization.jungnetworklayout;

import java.util.Dictionary;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.metatype.MetaTypeProvider;
import org.osgi.service.metatype.MetaTypeService;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.SpringLayout;

/**
 * @author Weixia(Bonnie) Huang 
 */
public class JUNGSpringLayout implements AlgorithmFactory {
	   private MetaTypeProvider provider;

	   protected void activate(ComponentContext ctxt) {
	        MetaTypeService mts = (MetaTypeService)ctxt.locateService("MTS");
	        provider = mts.getMetaTypeInformation(ctxt.getBundleContext().getBundle());  
	    }
	    protected void deactivate(ComponentContext ctxt) {
	        provider = null;
	    }

	    /**
	     * @see org.cishell.framework.algorithm.AlgorithmFactory#createParameters(org.cishell.framework.data.Data[])
	     */
	    public MetaTypeProvider createParameters(Data[] dm) {
	        return provider;
	    }
	    
	    /**
	     * @see org.cishell.framework.algorithm.AlgorithmFactory#createAlgorithm(org.cishell.framework.data.Data[], java.util.Dictionary, org.cishell.framework.CIShellContext)
	     */
	    public Algorithm createAlgorithm(Data[] dm, Dictionary parameters,
	            CIShellContext context) {
	        return new JUNGSpringLayoutAlg(dm, parameters, context);
	    }
	    
	    private class JUNGSpringLayoutAlg implements Algorithm {
	        Data[] dm;
	        Dictionary parameters;
	        CIShellContext ciContext;
	        
	        public JUNGSpringLayoutAlg(Data[] dm, Dictionary parameters,
	        		CIShellContext ciContext) {
	        	this.dm = dm;
	        	this.parameters = parameters;
	            this.ciContext = ciContext;
	        }

	        public Data[] execute() {
	            Graph graph = (Graph) dm[0].getData();
	            ;
	            JUNGLayoutGUI gui = new JUNGLayoutGUI("Spring Layout", new SpringLayout(graph)); 
	    		gui.pack();
	    	    gui.setSize(400, 400) ;
	    	    gui.show();
	    	    return null;
	        }
	    }
}
