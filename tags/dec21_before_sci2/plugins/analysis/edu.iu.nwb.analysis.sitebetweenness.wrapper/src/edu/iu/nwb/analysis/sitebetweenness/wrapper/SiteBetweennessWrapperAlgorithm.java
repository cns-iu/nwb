package edu.iu.nwb.analysis.sitebetweenness.wrapper;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.Data;

public class SiteBetweennessWrapperAlgorithm implements Algorithm {
    private Data[] data;
    private Dictionary parameters;
    private CIShellContext ciShellContext;
    
    public SiteBetweennessWrapperAlgorithm(Data[] data,
    				  Dictionary parameters,
    				  CIShellContext ciShellContext) {
        this.data = data;
        this.parameters = parameters;
        this.ciShellContext = ciShellContext;
    }

    public Data[] execute() throws AlgorithmExecutionException {
        return null;
    }
}