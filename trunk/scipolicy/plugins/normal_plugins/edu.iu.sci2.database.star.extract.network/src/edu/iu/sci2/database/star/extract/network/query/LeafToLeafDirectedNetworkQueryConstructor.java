package edu.iu.sci2.database.star.extract.network.query;

import org.antlr.stringtemplate.StringTemplateGroup;
import org.cishell.utilities.swt.model.GUIModel;

public class LeafToLeafDirectedNetworkQueryConstructor extends QueryConstructor {
	public static final String LEAF_TO_LEAF_WITH_AGGREGATES_STRING_TEMPLATE_FILE_PATH =
		"leaf_leaf_aggregates.st";
	public static final String LEAF_TO_LEAF_WITHOUT_AGGREGATES_STRING_TEMPLATE_FILE_PATH =
		"leaf_leaf_no_aggregates.st";

	public static final StringTemplateGroup AGGREGATES_GROUP =
		loadTemplate(LEAF_TO_LEAF_WITH_AGGREGATES_STRING_TEMPLATE_FILE_PATH);
	public static final StringTemplateGroup NO_AGGREGATES_GROUP =
		loadTemplate(LEAF_TO_LEAF_WITHOUT_AGGREGATES_STRING_TEMPLATE_FILE_PATH);

	public String constructNodeQuery(GUIModel model) {
		return null;
	}

	public String constructEdgeQuery(GUIModel model) {
		return null;
	}

	public String getNodeIDColumn(GUIModel model) {
		return null;
	}

	public String getSourceNodeName(GUIModel model) {
		return null;
	}

	public String getTargetNodeName(GUIModel model) {
		return null;
	}

	public boolean isDirected(GUIModel model) {
		return false;
	}
}