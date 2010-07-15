package edu.iu.sci2.database.star.extract.network.query;

import org.cishell.utilities.swt.model.GUIModel;

public interface QueryConstructor {
	public String constructNodeQuery(GUIModel model);
	public String constructEdgeQuery(GUIModel model);
	public String getNodeIDColumn(GUIModel model);
	public String getSourceNodeName(GUIModel model);
	public String getTargetNodeName(GUIModel model);
	public boolean isDirected(GUIModel model);
}