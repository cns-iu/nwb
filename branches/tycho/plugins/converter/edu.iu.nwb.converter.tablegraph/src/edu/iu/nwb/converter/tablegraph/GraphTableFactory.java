package edu.iu.nwb.converter.tablegraph;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;

/* TODO: This thing isn't a converter in the CIShell sense.  It shouldn't have
 * converter in its project or package names, nor should it be in the converters
 * folder in the repository.
 */

public class GraphTableFactory implements AlgorithmFactory {
    public Algorithm createAlgorithm(
    		Data[] data, Dictionary parameters, CIShellContext context) {
        return new GraphTable(data, parameters, context);
    }
}