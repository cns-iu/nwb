package edu.iu.sci2.preprocessing.scholar;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;

public class GenericPublicationAlgorithmFactory implements AlgorithmFactory {
    public Algorithm createAlgorithm(Data[] data,
    								 Dictionary<String, Object> parameters,
    								 CIShellContext ciShellContext) {
        return new GenericPublicationAlgorithm(data, parameters, ciShellContext);
    }
}