package edu.iu.nwb.analysis.pathfindergraphnetworkscaling;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;

/**
 * @author Russell Duhon
 *
 */
public class PathfinderGraphAlgorithmFactory implements AlgorithmFactory {
    
    public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        return new PathfinderGraphAlgorithm(data, parameters, context);
    }
}