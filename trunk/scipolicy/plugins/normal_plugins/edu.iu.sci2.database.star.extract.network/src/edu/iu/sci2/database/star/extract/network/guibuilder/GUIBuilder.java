package edu.iu.sci2.database.star.extract.network.guibuilder;

import org.cishell.utilities.swt.GUIBuilderUtilities;
import org.cishell.utilities.swt.model.GUIModel;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import edu.iu.sci2.database.star.extract.network.StarDatabase;

public abstract class GUIBuilder {
	public static final int WINDOW_WIDTH = 800;
	public static final int WINDOW_HEIGHT = 800;

	public static final int DEFAULT_AGGREGATE_WIDGET_COUNT = 3;

	public static final String NODE_ATTRIBUTES_GROUP_TEXT = "Node Attributes";
	public static final String EDGE_ATTRIBUTES_GROUP_TEXT = "Edge Attributes";
	public static final String NODE_TYPE = "Node";
	public static final String EDGE_TYPE = "Edge";

	public static final String LEAF_FIELD_NAME = "leafEntity";
	public static final String NODE_ATTRIBUTES_GROUP_BASE_NAME = "nodeAttributes.";
	public static final String EDGE_ATTRIBUTES_GROUP_BASE_NAME = "edgeAttributes.";
	public static final String ATTRIBUTE_FUNCTION_BASE_NAME = "attributeFunction.";
	public static final String CORE_ENTITY_COLUMN_BASE_NAME = "coreEntityColumn.";
	public static final String RESULT_NAME_FIELD_BASE_NAME = "result.";

	public static final String NODE_ATTRIBUTE_FUNCTION_BASE_NAME =
		NODE_ATTRIBUTES_GROUP_BASE_NAME + ATTRIBUTE_FUNCTION_BASE_NAME;
	public static final String NODE_CORE_ENTITY_COLUMN_BASE_NAME =
		NODE_ATTRIBUTES_GROUP_BASE_NAME + CORE_ENTITY_COLUMN_BASE_NAME;
	public static final String NODE_RESULT_BASE_NAME =
		NODE_ATTRIBUTES_GROUP_BASE_NAME + RESULT_NAME_FIELD_BASE_NAME;

	public static final String EDGE_ATTRIBUTE_FUNCTION_BASE_NAME =
		EDGE_ATTRIBUTES_GROUP_BASE_NAME + ATTRIBUTE_FUNCTION_BASE_NAME;
	public static final String EDGE_CORE_ENTITY_COLUMN_BASE_NAME =
		EDGE_ATTRIBUTES_GROUP_BASE_NAME + CORE_ENTITY_COLUMN_BASE_NAME;
	public static final String EDGE_RESULT_BASE_NAME =
		EDGE_ATTRIBUTES_GROUP_BASE_NAME + RESULT_NAME_FIELD_BASE_NAME;

	public abstract GUIModel createGUI(
			String windowTitle, int windowWidth, int windowHeight, StarDatabase starDatabase);

	public static void runGUI(
			Display display,
			Shell shell,
			int windowHeight) {
		GUIBuilderUtilities.openShell(shell, windowHeight, true);
    	GUIBuilderUtilities.swtLoop(display, shell);
	}
}