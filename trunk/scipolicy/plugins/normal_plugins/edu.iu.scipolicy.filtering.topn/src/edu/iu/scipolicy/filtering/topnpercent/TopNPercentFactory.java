package edu.iu.scipolicy.filtering.topnpercent;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.osgi.service.metatype.ObjectClassDefinition;

import edu.iu.scipolicy.filtering.topncommon.TopNUtilities;

public class TopNPercentFactory implements AlgorithmFactory, ParameterMutator {
	public Algorithm createAlgorithm(Data[] data,
									 Dictionary parameters,
									 CIShellContext context)
	{
		return new TopNPercentAlgorithm(data, parameters, context);
	}

	public ObjectClassDefinition mutateParameters(Data[] data,
												  ObjectClassDefinition parameters)
	{
		return TopNUtilities.mutateParameters(data, parameters);
	}
}