package edu.iu.sci2.database.star.extract.network.query;

import java.util.Map;

import org.antlr.stringtemplate.StringTemplateGroup;
import org.cishell.utilities.StringUtilities;
import org.cishell.utility.swt.model.SWTModel;

import edu.iu.sci2.database.star.common.StarDatabaseMetadata;
import edu.iu.sci2.database.star.extract.common.query.QueryConstructor;

public class CoreToLeafDirectedNetworkQueryConstructor extends QueryConstructor {
	public static final String CORE_TO_LEAF_WITH_AGGREGATES_STRING_TEMPLATE_FILE_PATH =
		"core_leaf_aggregates.st";
	public static final String CORE_TO_LEAF_WITHOUT_AGGREGATES_STRING_TEMPLATE_FILE_PATH =
		"core_leaf_no_aggregates.st";

	public static final StringTemplateGroup AGGREGATES_GROUP =
		loadTemplate(CORE_TO_LEAF_WITH_AGGREGATES_STRING_TEMPLATE_FILE_PATH);
	public static final StringTemplateGroup NO_AGGREGATES_GROUP =
		loadTemplate(CORE_TO_LEAF_WITHOUT_AGGREGATES_STRING_TEMPLATE_FILE_PATH);

	private String leafTableName;
	private String coreColumnsForQuery;
	private String emptyCoreTableNameColumns;
	private String coreTableNameColumnsForGroupBy;

	public CoreToLeafDirectedNetworkQueryConstructor(
			String leafTableName,
			String headerGroupName,
			String nodeAttributeFunctionGroupName,
			String nodeCoreEntityColumnGroupName,
			String nodeResultNameGroupName,
			String edgeAttributeFunctionGroupName,
			String edgeCoreEntityColumnGroupName,
			String edgeResultNameGroupName,
			SWTModel model,
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
		this.leafTableName = leafTableName;
		this.coreColumnsForQuery =
			formCoreColumnsQuerySection(getNodeNonAggregatedCoreColumns());
		this.emptyCoreTableNameColumns =
			formEmptyCoreColumnsQuerySection(getNodeNonAggregatedCoreColumns());
		this.coreTableNameColumnsForGroupBy =
			formCoreColumnsForGroupByQuerySection(getNodeNonAggregatedCoreColumns());
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
		// Prepare arguments for the String Template.

		String leafTableName_ForEntityType = StringUtilities.toSentenceCase(this.leafTableName);

		String coreTableName = getCoreTableName();
		String coreTableName_ForEntityType = StringUtilities.toSentenceCase(coreTableName);

		int idSize =
			Math.max(this.leafTableName.length(), coreTableName.length()) + ID_CHARACTER_COUNT;
		int entityTypeSize =
			Math.max(leafTableName_ForEntityType.length(), coreTableName_ForEntityType.length());
		int labelSize = MAXIMUM_LABEL_SIZE;

		// Set the arguments.

		arguments.put("leafTableName", this.leafTableName);
		arguments.put("leafTableName_ForEntityType", leafTableName_ForEntityType);
		arguments.put("coreTableName", coreTableName);
		arguments.put("coreTableName_ForEntityType", coreTableName_ForEntityType);
		arguments.put("coreTableNameColumns", this.coreColumnsForQuery);
		arguments.put("emptyCoreTableNameColumns", this.emptyCoreTableNameColumns);
		arguments.put("coreTableNameColumnsForGroupBy", this.coreTableNameColumnsForGroupBy);
		arguments.put("aggregates", getNodeAggregatesForQuery());
		arguments.put("emptyAggregates", getEmptyNodeAggregatesForQuery());
		arguments.put("idSize", "" + idSize);
		arguments.put("entityTypeSize", "" + entityTypeSize);
		arguments.put("labelSize", "" + labelSize);

		return arguments;
	}

	@Override
	public Map<String, String> formNodeQueryWithoutAggregatesStringTemplateArguments(
			Map<String, String> arguments) {
		// Prepare arguments for the String Template.

		String leafTableName_ForEntityType = StringUtilities.toSentenceCase(this.leafTableName);

		String coreTableName = getCoreTableName();
		String coreTableName_ForEntityType = StringUtilities.toSentenceCase(coreTableName);

		int idSize =
			Math.max(this.leafTableName.length(), coreTableName.length()) + ID_CHARACTER_COUNT;
		int entityTypeSize =
			Math.max(leafTableName_ForEntityType.length(), coreTableName_ForEntityType.length());
		int labelSize = MAXIMUM_LABEL_SIZE;

		// Set the arguments.

		arguments.put("leafTableName", this.leafTableName);
		arguments.put("leafTableName_ForEntityType", leafTableName_ForEntityType);
		arguments.put("coreTableName", coreTableName);
		arguments.put("coreTableName_ForEntityType", coreTableName_ForEntityType);
		arguments.put("coreTableNameColumns", this.coreColumnsForQuery);
		arguments.put("emptyCoreTableNameColumns", this.emptyCoreTableNameColumns);
		arguments.put("idSize", "" + idSize);
		arguments.put("entityTypeSize", "" + entityTypeSize);
		arguments.put("labelSize", "" + labelSize);

		return arguments;
	}

	@Override
	public Map<String, String> formEdgeQueryWithAggregatesStringTemplateArguments(
			Map<String, String> arguments) {
		// Prepare arguments for the String Template.

		String leafTableName_ForEntityType = StringUtilities.toSentenceCase(this.leafTableName);

		String coreTableName = getCoreTableName();
		String coreTableName_ForEntityType = StringUtilities.toSentenceCase(coreTableName);

		int idSize =
			Math.max(this.leafTableName.length(), coreTableName.length()) + ID_CHARACTER_COUNT;

		// Set the arguments.

		arguments.put("leafTableName", this.leafTableName);
		arguments.put("leafTableName_ForEntityType", leafTableName_ForEntityType);
		arguments.put("coreTableName", coreTableName);
		arguments.put("coreTableName_ForEntityType", coreTableName_ForEntityType);
		arguments.put("aggregates", getEdgeAggregatesForQuery());
		arguments.put("idSize", "" + idSize);

		return arguments;
	}

	public Map<String, String> formEdgeQueryWithoutAggregatesStringTemplateArguments(
			Map<String, String> arguments) {
		// Prepare arguments for the String Template.

		String leafTableName_ForEntityType = StringUtilities.toSentenceCase(this.leafTableName);

		String coreTableName = getCoreTableName();
		String coreTableName_ForEntityType = StringUtilities.toSentenceCase(coreTableName);

		int idSize =
			Math.max(this.leafTableName.length(), coreTableName.length()) + ID_CHARACTER_COUNT;

		// Set the arguments.

		arguments.put("leafTableName", this.leafTableName);
		arguments.put("leafTableName_ForEntityType", leafTableName_ForEntityType);
		arguments.put("coreTableName", coreTableName);
		arguments.put("coreTableName_ForEntityType", coreTableName_ForEntityType);
		arguments.put("idSize", "" + idSize);

		return arguments;
	}

	@Override
	public boolean isDirected() {
		return true;
	}
}