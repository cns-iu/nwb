package edu.iu.sci2.database.star.extract.network.query;

import java.util.Map;

import org.antlr.stringtemplate.StringTemplateGroup;
import org.cishell.utilities.StringUtilities;
import org.cishell.utilities.swt.model.GUIModel;

import com.google.common.collect.Multimap;

import edu.iu.sci2.database.star.common.StarDatabaseMetadata;

public class CoOccurrenceNetworkQueryConstructor extends QueryConstructor {
	public static final String CO_OCCURRENCE_WITH_AGGREGATES_STRING_TEMPLATE_FILE_PATH =
		"co_occurrence_aggregates.st";
	public static final String CO_OCCURRENCE_WITHOUT_AGGREGATES_STRING_TEMPLATE_FILE_PATH =
		"co_occurrence_no_aggregates.st";

	public static final StringTemplateGroup AGGREGATES_GROUP =
		loadTemplate(CO_OCCURRENCE_WITH_AGGREGATES_STRING_TEMPLATE_FILE_PATH);
	public static final StringTemplateGroup NO_AGGREGATES_GROUP =
		loadTemplate(CO_OCCURRENCE_WITHOUT_AGGREGATES_STRING_TEMPLATE_FILE_PATH);

	private String leafTableName;
	private String coreTableName;
	private String nodeAggregatesForQuery;
	private String edgeAggregatesForQuery;
	private String nonAggregateCoreColumnsForQuery;

	public CoOccurrenceNetworkQueryConstructor(
			String headerGroupName,
			String leafTableFieldName,
			String nodeAttributeFunctionGroupName,
			String nodeCoreEntityColumnGroupName,
			String nodeResultNameGroupName,
			String edgeAttributeFunctionGroupName,
			String edgeCoreEntityColumnGroupName,
			String edgeResultNameGroupName,
			GUIModel model,
			StarDatabaseMetadata metadata) {
		this.leafTableName =
			(String) model.getGroup(headerGroupName).getField(leafTableFieldName).getValue();
		this.coreTableName = metadata.getCoreEntityTableName();

		Multimap<String, String> nodeAggregatedColumnNamesToQueryString =
			mapAggregatedColumnNamesToQueryString(
				model.getGroup(nodeAttributeFunctionGroupName),
				model.getGroup(nodeCoreEntityColumnGroupName),
				model.getGroup(nodeResultNameGroupName));
		this.nodeAggregatesForQuery =
			StringUtilities.implodeItems(nodeAggregatedColumnNamesToQueryString.values(), ", ");
		System.err.println(nodeAggregatesForQuery);

		Multimap<String, String> edgeAggregatedColumnNamesToQueryString =
			mapAggregatedColumnNamesToQueryString(
				model.getGroup(edgeAttributeFunctionGroupName),
				model.getGroup(edgeCoreEntityColumnGroupName),
				model.getGroup(edgeResultNameGroupName));
		this.edgeAggregatesForQuery =
			StringUtilities.implodeItems(edgeAggregatedColumnNamesToQueryString.values(), ", ");
		System.err.println(edgeAggregatesForQuery);

		this.nonAggregateCoreColumnsForQuery = getNonAggregateCoreColumnsForQuery(
			metadata, edgeAggregatedColumnNamesToQueryString);

		System.err.println(nonAggregateCoreColumnsForQuery);
	}

	public String constructNodeQuery(GUIModel model) {
		return null;
	}

	public String constructEdgeQuery(GUIModel model) {
		return null;
	}

	public boolean isDirected(GUIModel model) {
		return false;
	}
}