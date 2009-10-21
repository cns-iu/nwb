package edu.iu.nwb.analysis.extractdirectednetfromtable.algorithms;

import java.util.Collections;
import java.util.Dictionary;
import java.util.List;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.cishell.utilities.TableUtilities;
import org.cishell.utilities.mutateParameter.DropdownMutator;
import org.osgi.service.metatype.ObjectClassDefinition;

import prefuse.data.Table;

public class ExtractDirectedNetworkAlgorithmFactory implements AlgorithmFactory, ParameterMutator {
	
	public static final String TARGET_COLUMN_NAME_PARAMETER_ID = "targetColumn";
	public static final String SOURCE_COLUMN_NAME_PARAMETER_ID = "sourceColumn";
	public static final String DELIMITER_PARAMETER_ID = "delimiter";
	public static final String AGGREGATION_FUNCTION_FILE_PARAMETER_ID = "aff";
	
	public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
		return new ExtractDirectedNetworkAlgorithm(data, parameters, context);
	}

	public ObjectClassDefinition mutateParameters(Data[] data, ObjectClassDefinition oldParameters) {
		Table table = (Table) data[0].getData();

		List columnNames = TableUtilities.getAllColumnNames(table.getSchema());
		Collections.sort(columnNames);

		DropdownMutator mutator = new DropdownMutator();
		mutator.add(SOURCE_COLUMN_NAME_PARAMETER_ID, columnNames);
		mutator.add(TARGET_COLUMN_NAME_PARAMETER_ID, columnNames);

		return mutator.mutate(oldParameters);
	}
}
