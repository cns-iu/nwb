package edu.iu.sci2.database.star.extract.network.query;

import org.antlr.stringtemplate.StringTemplateGroup;
import org.cishell.utilities.swt.model.GUIModel;

public class LeafToCoreDirectedNetworkQueryConstructor extends QueryConstructor {
	public static final String LEAF_TO_CORE_WITH_AGGREGATES_STRING_TEMPLATE_FILE_PATH =
		"leaf_core_aggregates.st";
	public static final String LEAF_TO_CORE_WITHOUT_AGGREGATES_STRING_TEMPLATE_FILE_PATH =
		"leaf_core_no_aggregates.st";

	public static final StringTemplateGroup AGGREGATES_GROUP =
		loadTemplate(LEAF_TO_CORE_WITH_AGGREGATES_STRING_TEMPLATE_FILE_PATH);
	public static final StringTemplateGroup NO_AGGREGATES_GROUP =
		loadTemplate(LEAF_TO_CORE_WITHOUT_AGGREGATES_STRING_TEMPLATE_FILE_PATH);

	private String fromTableName;
	private String toTableName;
	private boolean isCoreToLeaf;

	public LeafToCoreDirectedNetworkQueryConstructor(
			String fromTableName, String toTableName, boolean isCoreToLeaf) {
		this.fromTableName = fromTableName;
		this.toTableName = toTableName;
		this.isCoreToLeaf = isCoreToLeaf;
	}

	public String constructNodeQuery(GUIModel model) {
		return null;
	}

	public String constructEdgeQuery(GUIModel model) {
		return null;
	}

	public String getSourceNodeName(GUIModel model) {
		if (this.isCoreToLeaf) {
			return DEFAULT_TARGET_COLUMN;
		} else {
			return DEFAULT_SOURCE_COLUMN;
		}
	}

	public String getTargetNodeName(GUIModel model) {
		if (!this.isCoreToLeaf) {
			return DEFAULT_TARGET_COLUMN;
		} else {
			return DEFAULT_SOURCE_COLUMN;
		}
	}

	public boolean isDirected(GUIModel model) {
		return true;
	}
}