package edu.iu.scipolicy.database.isi.load;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;

public class ISIDatabaseLoaderAlgorithmFactory implements AlgorithmFactory {
    public Algorithm createAlgorithm(
    		Data[] data, Dictionary parameters, CIShellContext ciShellContext) {
        return new ISIDatabaseLoaderAlgorithm(data, parameters, ciShellContext);
    }
}