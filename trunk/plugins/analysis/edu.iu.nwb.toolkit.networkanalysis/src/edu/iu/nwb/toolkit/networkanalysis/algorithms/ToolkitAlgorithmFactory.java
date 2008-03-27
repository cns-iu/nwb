package edu.iu.nwb.toolkit.networkanalysis.algorithms;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;

public class ToolkitAlgorithmFactory implements AlgorithmFactory{

	public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
		return new ToolkitAlgorithm(data,parameters,context);
	}

}
