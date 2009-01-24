package edu.iu.scipolicy.visualization.horizontallinegraph;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;

public class HorizontalLineGraphAlgorithmFactory implements AlgorithmFactory {
    public Algorithm createAlgorithm(Data[] data,
    								 Dictionary parameters,
    								 CIShellContext context)
    {
        return new HorizontalLineGraphAlgorithm(data, parameters, context);
    }
}