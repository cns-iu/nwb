package edu.iu.sci2.database.star.extract.network.query;

import java.util.Collection;
import java.util.Map;

import org.antlr.stringtemplate.StringTemplateGroup;
import org.cishell.utilities.StringUtilities;
import org.cishell.utility.swt.model.SWTModel;

import edu.iu.sci2.database.star.extract.common.StarDatabaseDescriptor;
import edu.iu.sci2.database.star.extract.common.StarDatabaseExtractionUtilities;

public class LeafToCoreBipartiteNetworkQueryConstructor extends BasicNetworkQueryConstructor {
	public static final String CORE_TO_LEAF_STRING_TEMPLATE_FILE_PATH = "leaf_core_bipartite.st";

	public static final StringTemplateGroup STRING_TEMPLATE_GROUP =
		loadTemplate(CORE_TO_LEAF_STRING_TEMPLATE_FILE_PATH);

	private String leafTableName;
	private Collection<String> coreColumnElements;
	private Collection<String> emptyCoreTableColumnNameElements;
	private Collection<String> coreTableColumnNameElementsForGroupBy;

	public LeafToCoreBipartiteNetworkQueryConstructor(
			String leafTableName,
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
		this.leafTableName = leafTableName;
		this.coreColumnElements = StarDatabaseExtractionUtilities.formCoreColumnElements(
			getNodeNonAggregatedCoreColumns());
		this.emptyCoreTableColumnNameElements =
			StarDatabaseExtractionUtilities.formEmptyCoreColumnNameElements(
				getNodeNonAggregatedCoreColumns());
		this.coreTableColumnNameElementsForGroupBy =
			StarDatabaseExtractionUtilities.formCoreColumnNameElementsForGroupBy(
				getNodeNonAggregatedCoreColumns());
	}

	@Override
	public StringTemplateGroup getStringTemplateGroup() {
		return STRING_TEMPLATE_GROUP;
	}

	@Override
	public Map<String, Object> formNodeQueryStringTemplateArguments(
			Map<String, Object> arguments) {
		// Prepare arguments for the String Template.

		String leafTableName_ForEntityType = StringUtilities.toSentenceCase(this.leafTableName);

		String coreTableName = getCoreTableName();
		String coreTableName_ForEntityType = StringUtilities.toSentenceCase(coreTableName);

		int idSize = Math.max(
			this.leafTableName.length(),
			coreTableName.length()) + StarDatabaseExtractionUtilities.ID_CHARACTER_COUNT;
		int entityTypeSize =
			Math.max(leafTableName_ForEntityType.length(), coreTableName_ForEntityType.length());
		int labelSize = StarDatabaseExtractionUtilities.MAXIMUM_LABEL_SIZE;

		// Set the arguments.

		arguments.put("leafTableName", this.leafTableName);
		arguments.put("leafTableName_ForEntityType", leafTableName_ForEntityType);
		arguments.put("coreTableName", coreTableName);
		arguments.put("coreTableName_ForEntityType", coreTableName_ForEntityType);
		arguments.put("coreTableColumnNames", this.coreColumnElements);
		arguments.put("emptyCoreTableColumnNames", this.emptyCoreTableColumnNameElements);
		arguments.put(
			"coreTableColumnNamesForGroupBy", this.coreTableColumnNameElementsForGroupBy);
		arguments.put("coreAggregates", getNodeAggregateElements());
		arguments.put("leafTableAggregates", getNodeLeafTableAggregateElements());
		arguments.put("leafTableAggregates_Joins", getNodeLeafTableAggregateJoinElements());
		arguments.put("emptyCoreAggregates", getEmptyNodeAggregateElements());
		arguments.put("idSize", "" + idSize);
		arguments.put("entityTypeSize", "" + entityTypeSize);
		arguments.put("labelSize", "" + labelSize);

		return arguments;
	}

	@Override
	public Map<String, Object> formEdgeQueryStringTemplateArguments(
			Map<String, Object> arguments) {
		// Prepare arguments for the String Template.

		String leafTableName_ForEntityType = StringUtilities.toSentenceCase(this.leafTableName);

		String coreTableName = getCoreTableName();
		String coreTableName_ForEntityType = StringUtilities.toSentenceCase(coreTableName);

		int idSize = Math.max(
			this.leafTableName.length(),
			coreTableName.length()) + StarDatabaseExtractionUtilities.ID_CHARACTER_COUNT;

		// Set the arguments.

		arguments.put("leafTableName", this.leafTableName);
		arguments.put("leafTableName_ForEntityType", leafTableName_ForEntityType);
		arguments.put("coreTableName", coreTableName);
		arguments.put("coreTableName_ForEntityType", coreTableName_ForEntityType);
		arguments.put("coreAggregates", getEdgeAggregateElements());
		arguments.put("leafTableAggregates", getEdgeLeafTableAggregateElements());
		arguments.put("leafTableAggregates_Joins", getEdgeLeafTableAggregateJoinElements());
		arguments.put("idSize", "" + idSize);

		return arguments;
	}

	public Map<String, Object> formEdgeQueryWithoutAggregatesStringTemplateArguments(
			Map<String, Object> arguments) {
		// Prepare arguments for the String Template.

		String leafTableName_ForEntityType = StringUtilities.toSentenceCase(this.leafTableName);

		String coreTableName = getCoreTableName();
		String coreTableName_ForEntityType = StringUtilities.toSentenceCase(coreTableName);

		int idSize = Math.max(
			this.leafTableName.length(),
			coreTableName.length()) + StarDatabaseExtractionUtilities.ID_CHARACTER_COUNT;

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