package edu.iu.nwb.analysis.weakcomponentclustering;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;

/**
 * @author Russell Duhon
 *
 */
public class ClusteringAlgorithmFactory implements AlgorithmFactory {

    public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        return new ClusteringAlgorithm(data, parameters, context);
    }
}