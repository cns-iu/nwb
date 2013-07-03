package edu.iu.nwb.converter.prefusegraphml.for_guess;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;


public class GraphMLToGraphMLForGuessAlgorithmFactory implements AlgorithmFactory {
    public Algorithm createAlgorithm(
    		Data[] data,
    		Dictionary<String, Object> parameters,
    		CIShellContext ciShellContext) {
        return new GraphMLToGraphMLForGuessAlgorithm(data, ciShellContext);
    }
}