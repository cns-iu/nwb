package edu.iu.sci2.database.star.extract.network.query;

import java.util.Map;

import org.antlr.stringtemplate.StringTemplateGroup;
import org.cishell.utilities.StringUtilities;
import org.cishell.utility.swt.model.SWTModel;

import edu.iu.sci2.database.star.extract.common.StarDatabaseDescriptor;
import edu.iu.sci2.database.star.extract.common.StarDatabaseExtractionUtilities;

public class LeafToLeafDirectedNetworkQueryConstructor extends BasicNetworkQueryConstructor {
	public static final String LEAF_TO_LEAF_STRING_TEMPLATE_FILE_PATH = "leaf_leaf_bipartite.st";

	public static final StringTemplateGroup STRING_TEMPLATE_GROUP =
		loadTemplate(LEAF_TO_LEAF_STRING_TEMPLATE_FILE_PATH);

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

		this.leafTable1Name = leafTable1Name;
		this.leafTable2Name = leafTable2Name;
	}

	@Override
	public StringTemplateGroup getStringTemplateGroup() {
		return STRING_TEMPLATE_GROUP;
	}

	@Override
	public Map<String, Object> formNodeQueryStringTemplateArguments(
			Map<String, Object> arguments) {
		// Prepare arguments for the String Template.

		String leafTable1Name_ForEntityType = StringUtilities.toSentenceCase(this.leafTable1Name);
		String leafTable2Name_ForEntityType = StringUtilities.toSentenceCase(this.leafTable2Name);

		String coreTableName = getCoreTableName();

		int idSize = Math.max(
			leafTable1Name_ForEntityType.length(),
			leafTable2Name_ForEntityType.length()) +
				StarDatabaseExtractionUtilities.ID_CHARACTER_COUNT;
		int entityTypeSize =
			Math.max(leafTable1Name_ForEntityType.length(), leafTable2Name_ForEntityType.length());
		int labelSize = StarDatabaseExtractionUtilities.MAXIMUM_LABEL_SIZE;

		arguments.put("leafTable1Name", this.leafTable1Name);
		arguments.put("leafTable1Name_ForEntityType", leafTable1Name_ForEntityType);
		arguments.put("leafTable2Name", this.leafTable2Name);
		arguments.put("leafTable2Name_ForEntityType", leafTable2Name_ForEntityType);
		arguments.put("coreTableName", coreTableName);
		arguments.put("coreAggregates", getNodeAggregateElements());
		arguments.put("leafTableAggregates", getNodeLeafTableAggregateElements());
		arguments.put("leafTableAggregates_Joins", getNodeLeafTableAggregateJoinElements());
		arguments.put("idSize", "" + idSize);
		arguments.put("entityTypeSize", "" + entityTypeSize);
		arguments.put("labelSize", "" + labelSize);

		return arguments;
	}

	@Override
	public Map<String, Object> formEdgeQueryStringTemplateArguments(
			Map<String, Object> arguments) {
		// Prepare arguments for the String Template.

		String leafTable1Name_ForEntityType = StringUtilities.toSentenceCase(this.leafTable1Name);
		String leafTable2Name_ForEntityType = StringUtilities.toSentenceCase(this.leafTable2Name);

		String coreTableName = getCoreTableName();

		int idSize = Math.max(
			leafTable1Name_ForEntityType.length(),
			leafTable2Name_ForEntityType.length()) +
				StarDatabaseExtractionUtilities.ID_CHARACTER_COUNT;

		arguments.put("leafTable1Name", this.leafTable1Name);
		arguments.put("leafTable1Name_ForEntityType", leafTable1Name_ForEntityType);
		arguments.put("leafTable2Name", this.leafTable2Name);
		arguments.put("leafTable2Name_ForEntityType", leafTable2Name_ForEntityType);
		arguments.put("coreTableName", coreTableName);
		arguments.put("coreAggregates", getEdgeAggregateElements());
		arguments.put("leafTableAggregates", getEdgeLeafTableAggregateElements());
		arguments.put("leafTableAggregates_Joins", getEdgeLeafTableAggregateJoinElements());
		arguments.put("idSize", "" + idSize);

		return arguments;
	}

	@Override
	public boolean isDirected() {
		return false;
	}
}