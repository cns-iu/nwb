package edu.iu.scipolicy.filtering.topn;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.osgi.service.metatype.ObjectClassDefinition;

import edu.iu.scipolicy.filtering.topncommon.TopNUtilities;

public class TopNFactory implements AlgorithmFactory, ParameterMutator {
	public Algorithm createAlgorithm(Data[] data,
									 Dictionary parameters,
									 CIShellContext context)
	{
		return new TopNAlgorithm(data, parameters, context);
	}

	public ObjectClassDefinition mutateParameters(Data[] data,
												  ObjectClassDefinition parameters)
	{
		return TopNUtilities.mutateParameters(data, parameters);
	}
}