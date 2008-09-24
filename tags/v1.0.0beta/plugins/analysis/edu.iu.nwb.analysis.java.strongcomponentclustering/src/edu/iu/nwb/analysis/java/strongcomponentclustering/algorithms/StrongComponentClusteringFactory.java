package edu.iu.nwb.analysis.java.strongcomponentclustering.algorithms;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;

public class StrongComponentClusteringFactory implements AlgorithmFactory {
    public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        return new StrongComponentClustering(data, parameters, context);
    }
}