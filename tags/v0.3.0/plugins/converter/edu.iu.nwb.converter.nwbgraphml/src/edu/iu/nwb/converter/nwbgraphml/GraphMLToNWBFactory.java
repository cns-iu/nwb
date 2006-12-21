package edu.iu.nwb.converter.nwbgraphml;

import java.util.Dictionary;

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
public class GraphMLToNWBFactory implements AlgorithmFactory {

    protected void activate(ComponentContext ctxt) { }
    protected void deactivate(ComponentContext ctxt) { }

    /**
     * Create an converter
     * 
     * @param data The data to convert
     * @param parameters Parameters passed to the algorithm
     * @param context Access to the CIShell environment
     */
    public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        return new GraphMLToNWB(data, parameters, context);
    }
    
    /**
     * This converter accepts no parameters
     */
    public MetaTypeProvider createParameters(Data[] data) {
        return null;
    }
}