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

/* TODO This entire plugin should be scrapped and rewritten
 * using edu.iu.nwb.util.nwbfile utilities.
 */
public class GraphMLToNWBbyStaxFactory implements AlgorithmFactory  {
    public Algorithm createAlgorithm(
    		Data[] data, Dictionary<String, Object> parameters, CIShellContext context) {
		return new GraphMLToNWBbyStax(data);
    }
}
