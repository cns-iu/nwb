package edu.iu.sci2.database.star.extract.network.query;

import java.util.Map;

import org.antlr.stringtemplate.StringTemplateGroup;
import org.cishell.utilities.StringUtilities;
import org.cishell.utility.swt.model.SWTModel;

import edu.iu.sci2.database.star.common.StarDatabaseMetadata;

public class LeafToLeafDirectedNetworkQueryConstructor extends QueryConstructor {
	public static final String LEAF_TO_LEAF_WITH_AGGREGATES_STRING_TEMPLATE_FILE_PATH =
		"leaf_leaf_aggregates.st";
	public static final String LEAF_TO_LEAF_WITHOUT_AGGREGATES_STRING_TEMPLATE_FILE_PATH =
		"leaf_leaf_no_aggregates.st";

	public static final StringTemplateGroup AGGREGATES_GROUP =
		loadTemplate(LEAF_TO_LEAF_WITH_AGGREGATES_STRING_TEMPLATE_FILE_PATH);
	public static final StringTemplateGroup NO_AGGREGATES_GROUP =
		loadTemplate(LEAF_TO_LEAF_WITHOUT_AGGREGATES_STRING_TEMPLATE_FILE_PATH);

	private String leafTable1Name;
	private String leafTable2Name;

	public LeafToLeafDirectedNetworkQueryConstructor(
			String leafTable1Name,
			String leafTable2Name,
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

		this.leafTable1Name = leafTable1Name;
		this.leafTable2Name = leafTable2Name;
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

		String leafTable1Name_ForEntityType = StringUtilities.toSentenceCase(this.leafTable1Name);
		String leafTable2Name_ForEntityType = StringUtilities.toSentenceCase(this.leafTable2Name);

		String coreTableName = getCoreTableName();

		int idSize =
			Math.max(
				leafTable1Name_ForEntityType.length(), leafTable2Name_ForEntityType.length()) +
			ID_CHARACTER_COUNT;
		int entityTypeSize =
			Math.max(leafTable1Name_ForEntityType.length(), leafTable2Name_ForEntityType.length());
		int labelSize = MAXIMUM_LABEL_SIZE;

		// Set the leaf table name arguments.

		arguments.put("leafTable1Name", this.leafTable1Name);
		arguments.put("leafTable1Name_ForEntityType", leafTable1Name_ForEntityType);
		arguments.put("leafTable2Name", this.leafTable2Name);
		arguments.put("leafTable2Name_ForEntityType", leafTable2Name_ForEntityType);

		// Set the core table name arguments.

		arguments.put("coreTableName", coreTableName);

		// Set the aggregate arguments.

		arguments.put("aggregates", getNodeAggregatesForQuery());

		// Set the character sizing arguments.

		arguments.put("idSize", "" + idSize);
		arguments.put("entityTypeSize", "" + entityTypeSize);
		arguments.put("labelSize", "" + labelSize);

		return arguments;
	}

	@Override
	public Map<String, String> formNodeQueryWithoutAggregatesStringTemplateArguments(
			Map<String, String> arguments) {
		// Prepare arguments for the String Template.

		String leafTable1Name_ForEntityType = StringUtilities.toSentenceCase(this.leafTable1Name);
		String leafTable2Name_ForEntityType = StringUtilities.toSentenceCase(this.leafTable2Name);

		String coreTableName = getCoreTableName();

		int idSize =
			Math.max(
				leafTable1Name_ForEntityType.length(), leafTable2Name_ForEntityType.length()) +
			ID_CHARACTER_COUNT;
		int entityTypeSize =
			Math.max(leafTable1Name_ForEntityType.length(), leafTable2Name_ForEntityType.length());
		int labelSize = MAXIMUM_LABEL_SIZE;

		// Set the leaf table name arguments.

		arguments.put("leafTable1Name", this.leafTable1Name);
		arguments.put("leafTable1Name_ForEntityType", leafTable1Name_ForEntityType);
		arguments.put("leafTable2Name", this.leafTable2Name);
		arguments.put("leafTable2Name_ForEntityType", leafTable2Name_ForEntityType);

		// Set the core table name arguments.

		arguments.put("coreTableName", coreTableName);

		// Set the character sizing arguments.

		arguments.put("idSize", "" + idSize);
		arguments.put("entityTypeSize", "" + entityTypeSize);
		arguments.put("labelSize", "" + labelSize);

		return arguments;
	}

	@Override
	public Map<String, String> formEdgeQueryWithAggregatesStringTemplateArguments(
			Map<String, String> arguments) {
		// Prepare arguments for the String Template.

		String leafTable1Name_ForEntityType = StringUtilities.toSentenceCase(this.leafTable1Name);
		String leafTable2Name_ForEntityType = StringUtilities.toSentenceCase(this.leafTable2Name);

		String coreTableName = getCoreTableName();

		int idSize =
			Math.max(
				leafTable1Name_ForEntityType.length(), leafTable2Name_ForEntityType.length()) +
			ID_CHARACTER_COUNT;

		// Set the leaf table name arguments.

		arguments.put("leafTable1Name", this.leafTable1Name);
		arguments.put("leafTable1Name_ForEntityType", leafTable1Name_ForEntityType);
		arguments.put("leafTable2Name", this.leafTable2Name);
		arguments.put("leafTable2Name_ForEntityType", leafTable2Name_ForEntityType);

		// Set the core table name arguments.

		arguments.put("coreTableName", coreTableName);

		// Set the aggregate arguments.

		arguments.put("aggregates", getEdgeAggregatesForQuery());

		// Set the character sizing arguments.

		arguments.put("idSize", "" + idSize);

		return arguments;
	}

	public Map<String, String> formEdgeQueryWithoutAggregatesStringTemplateArguments(
			Map<String, String> arguments) {
		// Prepare arguments for the String Template.

		String leafTable1Name_ForEntityType = StringUtilities.toSentenceCase(this.leafTable1Name);
		String leafTable2Name_ForEntityType = StringUtilities.toSentenceCase(this.leafTable2Name);

		String coreTableName = getCoreTableName();

		int idSize =
			Math.max(
				leafTable1Name_ForEntityType.length(),
				leafTable2Name_ForEntityType.length()) +
				ID_CHARACTER_COUNT;

		// Set the leaf table name arguments.

		arguments.put("leafTable1Name", this.leafTable1Name);
		arguments.put("leafTable1Name_ForEntityType", leafTable1Name_ForEntityType);
		arguments.put("leafTable2Name", this.leafTable2Name);
		arguments.put("leafTable2Name_ForEntityType", leafTable2Name_ForEntityType);

		// Set the core table name arguments.

		arguments.put("coreTableName", coreTableName);

		// Set the character sizing arguments.

		arguments.put("idSize", "" + idSize);

		return arguments;
	}

	@Override
	public boolean isDirected() {
		return false;
	}
}