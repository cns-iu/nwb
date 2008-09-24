package edu.iu.iv.analysis.pathfindernetworkscaling;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;


public class PathFinderAlgorithmFactory implements AlgorithmFactory {

	public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        return new PathFinderAlgorithm(data, parameters, context);
    }
    
}