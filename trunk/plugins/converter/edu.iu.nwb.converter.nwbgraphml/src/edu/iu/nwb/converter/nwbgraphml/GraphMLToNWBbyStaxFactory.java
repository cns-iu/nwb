package edu.iu.nwb.converter.nwbgraphml;


import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;

/**
 * Algorithm factory for GraphML to NWB converter
 * 
 * @author bmarkine
 */

public class GraphMLToNWBbyStaxFactory implements AlgorithmFactory  {

    /**
     * Create an converter
     * 
     * @param data The data to convert
     * @param parameters Parameters passed to the algorithm
     * @param context Access to the CIShell environment
     */
    public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
				return new GraphMLToNWBbyStax(data, parameters, context);
    }

}
