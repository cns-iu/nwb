package edu.iu.scipolicy.analysis.blondelcommunitydetection;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;

public class BlondelCommunityDetectionAlgorithmFactory implements AlgorithmFactory {
    public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        return new BlondelCommunityDetectionAlgorithm(data, parameters, context);
    }
}