package edu.iu.nwb.converter.nwbgraphml;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;

/* TODO This entire plugin should be scrapped and rewritten
 * using edu.iu.nwb.util.nwbfile utilities.
 */
public class NWBToGraphMLbyStaxFactory implements AlgorithmFactory {
    public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        return new NWBToGraphMLbyStax(data, parameters, context);
    }
}