package edu.iu.sci2.database.star.extract.network.query;

import java.util.Map;

import org.antlr.stringtemplate.StringTemplateGroup;
import org.cishell.utility.datastructure.datamodel.DataModel;

import edu.iu.sci2.database.star.extract.common.StarDatabaseDescriptor;

public class CoOccurrenceNetworkQueryConstructor extends BasicNetworkQueryConstructor {
	public static final String CO_OCCURRENCE_STRING_TEMPLATE_FILE_PATH = "co_occurrence.st";

	public static final StringTemplateGroup STRING_TEMPLATE_GROUP =
		loadTemplate(CO_OCCURRENCE_STRING_TEMPLATE_FILE_PATH);

	private String leafTableName;

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
			StarDatabaseDescriptor databaseDescriptor) {
		super(
			headerGroupName,
			nodeAttributeFunctionGroupName,
			nodeCoreEntityColumnGroupName,
			nodeResultNameGroupName,
			edgeAttributeFunctionGroupName,
			edgeCoreEntityColumnGroupName,
			edgeResultNameGroupName,
			model,
			databaseDescriptor);
		this.leafTableName =
			(String) model.getGroup(headerGroupName).getField(leafTableFieldName).getValue();
	}

	@Override
	public StringTemplateGroup getStringTemplateGroup() {
		return STRING_TEMPLATE_GROUP;
	}

	@Override
	public Map<String, Object> formNodeQueryStringTemplateArguments(
			Map<String, Object> arguments) {
		arguments.put("leafTableName", this.leafTableName);
		arguments.put("coreTableName", getCoreTableName());
		arguments.put("coreAggregates", getNodeAggregateElements());
		arguments.put("leafTableAggregates", getNodeLeafTableAggregateElements());
		arguments.put("leafTableAggregates_Joins", getNodeLeafTableAggregateJoinElements());

		return arguments;
	}

	@Override
	public Map<String, Object> formEdgeQueryStringTemplateArguments(
			Map<String, Object> arguments) {
		arguments.put("leafTableName", this.leafTableName);
		arguments.put("coreTableName", getCoreTableName());
		arguments.put("coreAggregates", getEdgeAggregateElements());
		arguments.put("leafTableAggregates", getEdgeLeafTableAggregateElements());
		arguments.put("leafTableAggregates_Joins", getEdgeLeafTableAggregateJoinElements());

		return arguments;
	}

	@Override
	public boolean isDirected() {
		return false;
	}
}