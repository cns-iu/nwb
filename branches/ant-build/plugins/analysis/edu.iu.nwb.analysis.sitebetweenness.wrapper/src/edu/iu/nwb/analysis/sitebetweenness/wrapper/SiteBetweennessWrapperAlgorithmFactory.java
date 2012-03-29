package edu.iu.nwb.analysis.sitebetweenness.wrapper;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;

public class SiteBetweennessWrapperAlgorithmFactory implements AlgorithmFactory {
    public Algorithm createAlgorithm(
    		Data[] data, Dictionary<String, Object> parameters, CIShellContext ciShellContext) {
        return new SiteBetweennessWrapperAlgorithm(data, parameters, ciShellContext);
    }
}