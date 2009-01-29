package edu.iu.scipolicy.filtering.topn;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.cishell.reference.service.metatype.BasicAttributeDefinition;
import org.cishell.reference.service.metatype.BasicObjectClassDefinition;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.ObjectClassDefinition;

import prefuse.data.Table;
import edu.iu.scipolicy.utilities.MutateParameterUtilities;

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