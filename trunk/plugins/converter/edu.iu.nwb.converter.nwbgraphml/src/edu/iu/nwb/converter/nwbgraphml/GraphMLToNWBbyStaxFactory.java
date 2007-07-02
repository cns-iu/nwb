package edu.iu.nwb.converter.nwbgraphml;


import java.io.IOException;
import java.net.URL;
import java.util.Dictionary;

import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.metatype.MetaTypeProvider;

/**
 * Algorithm factory for GraphML to NWB converter
 * 
 * @author bmarkine
 */

public class GraphMLToNWBbyStaxFactory implements AlgorithmFactory  {
	protected void activate(ComponentContext ctxt) {
    	//nothin here
    }
    protected void deactivate(ComponentContext ctxt) { }
    
    

    /**
     * Create an converter
     * 
     * @param data The data to convert
     * @param parameters Parameters passed to the algorithm
     * @param context Access to the CIShell environment
     */
    public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
    		//System.err.println("createAlgorithm");
				return new GraphMLToNWBbyStax(data, parameters, context);
		
        
    }
    
    /**
     * This converter accepts no parameters
     */
    public MetaTypeProvider createParameters(Data[] data) {
        return null;
    }
    

}
