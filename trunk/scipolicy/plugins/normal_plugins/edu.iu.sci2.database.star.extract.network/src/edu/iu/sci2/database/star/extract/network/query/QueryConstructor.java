package edu.iu.sci2.database.star.extract.network.query;

import java.io.InputStreamReader;
import java.util.Map;

import org.antlr.stringtemplate.StringTemplateGroup;
import org.cishell.utilities.swt.model.GUIModel;
import org.cishell.utilities.swt.model.GUIModelField;
import org.cishell.utilities.swt.model.GUIModelGroup;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import edu.iu.sci2.database.star.common.StarDatabaseMetadata;
import edu.iu.sci2.database.star.common.parameter.ColumnDescriptor;
import edu.iu.sci2.database.star.extract.network.attribute.AttributeFunction;

public abstract class QueryConstructor {
	public static final String STRING_TEMPLATE_BASE_FILE_PATH =
		"/edu/iu/sci2/database/star/extract/network/query/stringtemplate/";

	public static final String DEFAULT_NODE_ID_COLUMN = "ID";
	public static final String DEFAULT_SOURCE_COLUMN = "SOURCE";
	public static final String DEFAULT_TARGET_COLUMN = "TARGET";

	public abstract String constructNodeQuery(GUIModel model);
	public abstract String constructEdgeQuery(GUIModel model);
	
	public String getNodeIDColumn(GUIModel model) {
		return DEFAULT_NODE_ID_COLUMN;
	}

	public String getSourceNodeName(GUIModel model) {
		return DEFAULT_SOURCE_COLUMN;
	}

	public String getTargetNodeName(GUIModel model) {
		return DEFAULT_TARGET_COLUMN;
	}
	
	public abstract boolean isDirected(GUIModel model);

	public static StringTemplateGroup loadTemplate(String relativeTemplatePath) {
		String absoluteTemplatePath = STRING_TEMPLATE_BASE_FILE_PATH + relativeTemplatePath;

		return new StringTemplateGroup(new InputStreamReader(
			QueryConstructor.class.getResourceAsStream(absoluteTemplatePath)));
	}

	public static Multimap<String, String> mapAggregatedColumnNamesToQueryString(
			GUIModelGroup aggregateFunctionGroup,
			GUIModelGroup coreEntityColumnGroup,
			GUIModelGroup resultColumnLabelGroup) {
		Multimap<String, String> aggregatesToQueryStrings = ArrayListMultimap.create();

		for (GUIModelField aggregateFunctionField : aggregateFunctionGroup.getFields()) {
			String id = aggregateFunctionField.getName();
			GUIModelField coreEntityColumnField = coreEntityColumnGroup.getField(id);
			GUIModelField resultColumnLabelField = resultColumnLabelGroup.getField(id);

			String aggregatedColumnName = (String) coreEntityColumnField.getValue();
			AttributeFunction attributeFunction =
				AttributeFunction.ATTRIBUTE_FUNCTIONS_BY_NAME.get(
					aggregateFunctionField.getValue());
			String queryString = attributeFunction.databaseRepresentation(
				aggregatedColumnName, (String) resultColumnLabelField.getValue());
			aggregatesToQueryStrings.put(aggregatedColumnName, queryString);
		}

		return aggregatesToQueryStrings;
	}

	public static String getNonAggregateCoreColumnsForQuery(
			StarDatabaseMetadata metadata,
			Multimap<String, String> aggregatedColumnNamesToQueryString) {
		StringBuilder nonAggregateCoreColumnsForQuery = new StringBuilder();

		for (ColumnDescriptor columnDescriptor : metadata.getColumnDescriptors().values()) {
			if (!aggregatedColumnNamesToQueryString.containsKey(
					columnDescriptor.getNameForDatabase())) {
				nonAggregateCoreColumnsForQuery.append(String.format(
					"\"%s\" AS \"%s\", ",
					columnDescriptor.getNameForDatabase(),
					columnDescriptor.getName()));
			}
		}

		nonAggregateCoreColumnsForQuery.delete(
			(nonAggregateCoreColumnsForQuery.length() - 2),
			nonAggregateCoreColumnsForQuery.length());

		return nonAggregateCoreColumnsForQuery.toString();
	}
}