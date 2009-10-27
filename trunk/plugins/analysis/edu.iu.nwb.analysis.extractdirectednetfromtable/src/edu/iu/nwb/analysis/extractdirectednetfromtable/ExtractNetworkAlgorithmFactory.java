package edu.iu.nwb.analysis.extractdirectednetfromtable;

import java.util.Collections;
import java.util.Dictionary;
import java.util.List;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.cishell.utilities.TableUtilities;
import org.cishell.utilities.mutateParameter.dropdown.DropdownMutator;
import org.osgi.service.metatype.ObjectClassDefinition;

import prefuse.data.Table;

public abstract class ExtractNetworkAlgorithmFactory implements AlgorithmFactory, ParameterMutator {
	public static final String TARGET_COLUMN_NAME_PARAMETER_ID = "targetColumn";
	public static final String SOURCE_COLUMN_NAME_PARAMETER_ID = "sourceColumn";
	public static final String DELIMITER_PARAMETER_ID = "delimiter";
	public static final String AGGREGATION_FUNCTION_FILE_PARAMETER_ID = "aff";

	public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
		String sourceColumnName = (String) parameters.get(SOURCE_COLUMN_NAME_PARAMETER_ID);
		String targetColumnName = (String) parameters.get(TARGET_COLUMN_NAME_PARAMETER_ID);

		return new ExtractNetworkAlgorithm(data, parameters, context, isBipartite(),
				createOutDataLabel(sourceColumnName, targetColumnName));
	}

	public abstract boolean isBipartite();

	public abstract String createOutDataLabel(String sourceColumnName, String targetColumnName);

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
