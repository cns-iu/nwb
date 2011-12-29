package edu.iu.nwb.analysis.multipartitejoining;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;

/**
 * @author Russell Duhon
 *
 */
public class JoiningAlgorithmFactory implements AlgorithmFactory {
    
    public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        return new JoiningAlgorithm(data, parameters, context);
    }
}