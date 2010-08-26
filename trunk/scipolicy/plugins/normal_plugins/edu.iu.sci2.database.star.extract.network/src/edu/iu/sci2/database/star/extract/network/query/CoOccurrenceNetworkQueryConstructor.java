package edu.iu.sci2.database.star.extract.network.query;

import java.util.Map;

import org.antlr.stringtemplate.StringTemplateGroup;
import org.cishell.utility.datastructure.datamodel.DataModel;

import edu.iu.sci2.database.star.common.StarDatabaseMetadata;
import edu.iu.sci2.database.star.extract.common.query.QueryConstructor;

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
	private String edgeNonAggregateCoreColumnsForQuery;
	private String edgeNonAggregateCoreTableNameColumnsForGroupBy;

	public CoOccurrenceNetworkQueryConstructor(
			String leafTableFieldName,
			String headerGroupName,
			String nodeAttributeFunctionGroupName,
			String nodeCoreEntityColumnGroupName,
			String nodeResultNameGroupName,
			String edgeAttributeFunctionGroupName,
			String edgeCoreEntityColumnGroupName,
			String edgeResultNameGroupName,
			DataModel model,
			StarDatabaseMetadata metadata) {
		super(
			headerGroupName,
			nodeAttributeFunctionGroupName,
			nodeCoreEntityColumnGroupName,
			nodeResultNameGroupName,
			edgeAttributeFunctionGroupName,
			edgeCoreEntityColumnGroupName,
			edgeResultNameGroupName,
			model,
			metadata);
		this.leafTableName =
			(String) model.getGroup(headerGroupName).getField(leafTableFieldName).getValue();
		this.edgeNonAggregateCoreColumnsForQuery =
			formCoreColumnsQuerySection(getEdgeNonAggregatedCoreColumns());
		this.edgeNonAggregateCoreTableNameColumnsForGroupBy =
			formCoreColumnsForGroupByQuerySection(getEdgeNonAggregatedCoreColumns());
	}

	@Override
	public StringTemplateGroup getAggregatesStringTemplateGroup() {
		return AGGREGATES_GROUP;
	}

	@Override
	public StringTemplateGroup getNoAggregatesStringTemplateGroup() {
		return NO_AGGREGATES_GROUP;
	}

	@Override
	public Map<String, String> formNodeQueryWithAggregatesStringTemplateArguments(
			Map<String, String> arguments) {
		arguments.put("leafTableName", this.leafTableName);
		arguments.put("coreTableName", getCoreTableName());
		arguments.put("aggregates", getNodeAggregatesForQuery());

		return arguments;
	}

	@Override
	public Map<String, String> formNodeQueryWithoutAggregatesStringTemplateArguments(
			Map<String, String> arguments) {
		arguments.put("leafTableName", this.leafTableName);
		arguments.put("coreTableName", getCoreTableName());

		return arguments;
	}

	@Override
	public Map<String, String> formEdgeQueryWithAggregatesStringTemplateArguments(
			Map<String, String> arguments) {
		arguments.put("leafTableName", this.leafTableName);
		arguments.put("coreTableName", getCoreTableName());
		arguments.put(
			"nonAggregateCoreTableNameColumns", this.edgeNonAggregateCoreColumnsForQuery);
		arguments.put(
			"nonAggregateCoreTableNameColumnsForGroupBy",
			this.edgeNonAggregateCoreTableNameColumnsForGroupBy);
		arguments.put("aggregates", getEdgeAggregatesForQuery());

		return arguments;
	}

	public Map<String, String> formEdgeQueryWithoutAggregatesStringTemplateArguments(
			Map<String, String> arguments) {
		arguments.put("leafTableName", this.leafTableName);
		arguments.put("coreTableName", getCoreTableName());
		arguments.put("coreTableNameColumns", this.edgeNonAggregateCoreTableNameColumnsForGroupBy);

		return arguments;
	}

	@Override
	public boolean isDirected() {
		return false;
	}
}