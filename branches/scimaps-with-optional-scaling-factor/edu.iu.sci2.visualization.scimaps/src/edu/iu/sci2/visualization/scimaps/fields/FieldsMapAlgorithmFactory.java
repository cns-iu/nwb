package edu.iu.sci2.visualization.scimaps.fields;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.utilities.AlgorithmUtilities;
import org.cishell.utilities.MutateParameterUtilities;
import org.cishell.utilities.TableUtilities;
import org.osgi.service.log.LogService;
import org.osgi.service.metatype.ObjectClassDefinition;

import prefuse.data.Table;
import edu.iu.sci2.visualization.scimaps.journals.JournalsMapAlgorithmFactory;
import edu.iu.sci2.visualization.scimaps.parameters.ScalingFactorAttributeDefinition;
import edu.iu.sci2.visualization.scimaps.parameters.ScalingStrategy;
import edu.iu.sci2.visualization.scimaps.rendering.Layout;

public class FieldsMapAlgorithmFactory implements AlgorithmFactory, ParameterMutator {
	public static final String NODE_ID_COLUMN_NAME_ID = "nodeIDColumnName";
	public static final String NODE_LABEL_COLUMN_NAME_ID = "nodeLabelColumnName";
	public static final String NODE_VALUE_COLUMN_NAME_ID = "nodeValueColumnName";
	public static final String NO_VALUE_COLUMN_TOKEN = "[None -- All Equal]";
	public static final String SUBTITLE_ID = "subtitle";
	public static final String SCALING_FACTOR_ID = "scalingFactor";
	public static final String WEB_VERSION_ID = "webVersion";
	public static final String SHOW_WINDOW_ID = "showWindow";
	public static final String DEFAULT_SUBTITLE_PREFIX = "Generated from ";
	

	@Override
	public Algorithm createAlgorithm(
			Data[] data, Dictionary<String, Object> parameters, CIShellContext context) {
		LogService logger = (LogService) context.getService(LogService.class.getName());
		
		String nodeIDColumnName = (String) parameters.get(NODE_ID_COLUMN_NAME_ID);
		String nodeLabelColumnName = (String) parameters.get(NODE_LABEL_COLUMN_NAME_ID);
		String nodeValueColumnName = (String) parameters.get(NODE_VALUE_COLUMN_NAME_ID);
		String dataDisplayName = (String) parameters.get(SUBTITLE_ID);
		String scalingFactorString = (String) parameters.get(SCALING_FACTOR_ID);
		boolean webVersion = (Boolean) parameters.get(WEB_VERSION_ID);
		Layout layout = webVersion ? Layout.SIMPLE : Layout.FULL;
		
		boolean showWindow = (Boolean) parameters.get(SHOW_WINDOW_ID);
		
		ScalingStrategy scalingFactor = JournalsMapAlgorithmFactory
				.tryInterpretingAsScalingStrategy(scalingFactorString);
		
		return new FieldsMapAlgorithm(data, logger, nodeIDColumnName, nodeLabelColumnName,
				nodeValueColumnName, dataDisplayName, scalingFactor, layout, showWindow);
	}

	/* Add a drop-down containing the String and Integer type column names
	 * in table, with those seeming mostly like to record journals coming first.
	 */
	@Override
	public ObjectClassDefinition mutateParameters(
			Data[] data, ObjectClassDefinition oldParameters) {
		Table table = (Table) data[0].getData();
		
		List<String> columnNames = TableUtilities.getAllColumnNames(table.getSchema());
		
		ObjectClassDefinition newParameters = addSourceDataFilenameParameter(oldParameters, data);
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
		
		newParameters = ScalingFactorAttributeDefinition.mutateParameters(newParameters, JournalsMapAlgorithmFactory.SCALING_FACTOR_ID);
				
		return mutateSubtitleParameter(newParameters, data);
	}
	
	/**
	 * Generate a default subtitle.
	 */
	private static ObjectClassDefinition mutateSubtitleParameter(
			ObjectClassDefinition newParameters, Data[] data) {
		// Generate default subtitle from the dataset label
		String defaultSubtitle = DEFAULT_SUBTITLE_PREFIX 
				+ data[0].getMetadata().get(DataProperty.LABEL);
		
		return MutateParameterUtilities
				.mutateDefaultValue(newParameters, SUBTITLE_ID,defaultSubtitle);
	}

	private static ObjectClassDefinition addSourceDataFilenameParameter(
			ObjectClassDefinition newParameters, Data[] data) {
		String guessedSourceDataFilename = AlgorithmUtilities.guessSourceDataFilename(data[0]);
		
		return MutateParameterUtilities.mutateDefaultValue(
				newParameters, SUBTITLE_ID, guessedSourceDataFilename);
	}
}