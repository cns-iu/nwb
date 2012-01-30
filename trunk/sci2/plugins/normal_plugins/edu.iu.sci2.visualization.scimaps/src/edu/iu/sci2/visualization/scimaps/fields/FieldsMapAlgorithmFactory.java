package edu.iu.sci2.visualization.scimaps.fields;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.cishell.utilities.AlgorithmUtilities;
import org.cishell.utilities.MutateParameterUtilities;
import org.cishell.utilities.TableUtilities;
import org.osgi.service.log.LogService;
import org.osgi.service.metatype.ObjectClassDefinition;

import prefuse.data.Table;

public class FieldsMapAlgorithmFactory implements AlgorithmFactory, ParameterMutator {
	public static final String NODE_ID_COLUMN_NAME_ID = "nodeIDColumnName";
	public static final String NODE_LABEL_COLUMN_NAME_ID = "nodeLabelColumnName";
	public static final String NODE_VALUE_COLUMN_NAME_ID = "nodeValueColumnName";
	public static final String NO_VALUE_COLUMN_TOKEN = "[None -- All Equal]";
	public static final String DATA_DISPLAY_NAME_ID = "datasetDisplayName";
	public static final String SCALING_FACTOR_ID = "scalingFactor";
	

	public Algorithm createAlgorithm(
			Data[] data, Dictionary<String, Object> parameters, CIShellContext context) {
		LogService logger = (LogService) context.getService(LogService.class.getName());
		
		String nodeIDColumnName = (String) parameters.get(NODE_ID_COLUMN_NAME_ID);
		String nodeLabelColumnName = (String) parameters.get(NODE_LABEL_COLUMN_NAME_ID);
		String nodeValueColumnName = (String) parameters.get(NODE_VALUE_COLUMN_NAME_ID);
		String dataDisplayName = (String) parameters.get(DATA_DISPLAY_NAME_ID);
		float scalingFactor = (Float) parameters.get(SCALING_FACTOR_ID);

		return new FieldsMapAlgorithm(data, logger, nodeIDColumnName, nodeLabelColumnName, nodeValueColumnName, dataDisplayName, scalingFactor);
	}

	/* Add a drop-down containing the String and Integer type column names
	 * in table, with those seeming mostly like to record journals coming first.
	 */
	public ObjectClassDefinition mutateParameters(
			Data[] data, ObjectClassDefinition oldParameters) {
		Table table = (Table) data[0].getData();
		
		List<String> columnNames = TableUtilities.getAllColumnNames(table.getSchema());
		
		ObjectClassDefinition newParameters;
		newParameters = addSourceDataFilenameParameter(oldParameters, data);
		newParameters =
			MutateParameterUtilities.mutateToDropdown(
					newParameters,
					NODE_ID_COLUMN_NAME_ID,
					columnNames,
					columnNames);
		newParameters =
			MutateParameterUtilities.mutateToDropdown(
					newParameters,
					NODE_LABEL_COLUMN_NAME_ID,
					columnNames,
					columnNames);
		
		List<String> columnNamesWithNone = new ArrayList<String>(columnNames);
		columnNamesWithNone.add(NO_VALUE_COLUMN_TOKEN);
		
		newParameters =
			MutateParameterUtilities.mutateToDropdown(
					newParameters,
					NODE_VALUE_COLUMN_NAME_ID,
					columnNamesWithNone,
					columnNamesWithNone);	
				
		return newParameters;
	}

	private ObjectClassDefinition addSourceDataFilenameParameter(
			ObjectClassDefinition newParameters, Data[] data) {
		String guessedSourceDataFilename = AlgorithmUtilities.guessSourceDataFilename(data[0]);
		
		return MutateParameterUtilities.mutateDefaultValue(
				newParameters, DATA_DISPLAY_NAME_ID, guessedSourceDataFilename);
	}
}