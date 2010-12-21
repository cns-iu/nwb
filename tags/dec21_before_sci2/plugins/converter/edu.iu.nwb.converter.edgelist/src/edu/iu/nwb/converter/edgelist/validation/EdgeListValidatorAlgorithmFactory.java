package edu.iu.nwb.converter.edgelist.validation;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;

public class EdgeListValidatorAlgorithmFactory implements AlgorithmFactory {
	public Algorithm createAlgorithm(Data[] dm, Dictionary parameters,
			CIShellContext context) {
		return new EdgeListValidatorAlgorithm(dm, parameters, context);
	}
}
