package edu.iu.sci2.database.star.extract.network.guibuilder;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.cishell.service.database.Database;
import org.cishell.utilities.MapUtilities;
import org.cishell.utilities.swt.model.GUIModel;
import org.cishell.utilities.swt.model.GUIModelField;
import org.cishell.utilities.swt.model.datasynchronizer.DropDownDataSynchronizer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

import edu.iu.cns.shared.utilities.swt.GUIBuilderUtilities;
import edu.iu.sci2.database.star.extract.network.CoreTableDescriptor;
import edu.iu.sci2.database.star.extract.network.LeafTableDescriptor;
import edu.iu.sci2.database.star.extract.network.StarDatabase;

public class GUIBuilder {
	public static final int WINDOW_WIDTH = 800;
	public static final int WINDOW_HEIGHT = 800;

	public static final String LEAF_FIELD_LABEL =
		"Choose the Leaf column to extract the co-occurrence network on: ";
	public static final String HEADER_GROUP_TEXT = "";
	public static final String NODE_AGGREGATES_GROUP_TEXT = "Node Aggregates";
	public static final String EDGE_AGGREGATES_GROUP_TEXT = "Edge Aggregates";

	public static final String LEAF_FIELD_NAME = "leafEntity";
	public static final String NODE_AGGREGATES_GROUP_BASE_NAME = "nodeAggregates.";
	public static final String EDGE_AGGREGATES_GROUP_BASE_NAME = "edgeAggregates.";
	public static final String AGGREGATE_TYPE_BASE_NAME = "aggregateType.";
	public static final String AGGREGATED_FIELD_BASE_NAME = "aggregatedField.";
	public static final String AGGREGATE_NAME_FIELD_BASE_NAME = "aggregateName.";

	public static GUIModel createGUI(
			String windowTitle, int windowWidth, int windowHeight, StarDatabase starDatabase) {
		GUIModel model = new GUIModel();

		Display display = GUIBuilderUtilities.createDisplay();
		Shell shell = GUIBuilderUtilities.createShell(
			display, windowTitle, windowWidth, windowHeight, 1, false);
		Group headerGroup = createHeaderGroup(shell);
		Group nodeAggregatesGroup = createNodeAggregatesGroup(shell, model);
		Group edgeAggregatesGroup = createEdgeAggregatesGroup(shell, model);

		GUIModelField<String, Combo, DropDownDataSynchronizer> leafField =
			createLeafSelectionField(headerGroup, starDatabase, model);

		AggregateTable nodeAggregatesTable = createTable(nodeAggregatesGroup, model);
		AggregateTable edgeAggregatesTable = createTable(edgeAggregatesGroup, model);

		runGUI(display, shell, windowHeight);

		return model;
	}

	private static void runGUI(
			Display display,
			Shell shell,
			int windowHeight) {
		GUIBuilderUtilities.openShell(shell, windowHeight, true);
    	GUIBuilderUtilities.swtLoop(display, shell);
	}

	private static Group createHeaderGroup(Composite parent) {
		Group headerGroup = new Group(parent, SWT.NONE);
		headerGroup.setLayoutData(createHeaderGroupLayoutData());
		headerGroup.setLayout(createHeaderGroupLayout());
		headerGroup.setText(HEADER_GROUP_TEXT);

		return headerGroup;
	}

	private static GridData createHeaderGroupLayoutData() {
		GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, true);
		layoutData.horizontalSpan = 2;

		return layoutData;
	}

	private static GridLayout createHeaderGroupLayout() {
		GridLayout layout = new GridLayout(2, false);

		return layout;
	}

	private static Group createNodeAggregatesGroup(Composite parent, GUIModel model) {
		Group nodeAggregatesGroup = new Group(parent, SWT.SHADOW_ETCHED_IN);
		nodeAggregatesGroup.setLayoutData(createNodeAggregatesGroupLayoutData());
		nodeAggregatesGroup.setLayout(createNodeAggregatesGroupLayout());
		nodeAggregatesGroup.setText(NODE_AGGREGATES_GROUP_TEXT);

		return nodeAggregatesGroup;
	}

	private static GridData createNodeAggregatesGroupLayoutData() {
		GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, true);
		layoutData.horizontalSpan = 2;

		return layoutData;
	}

	private static GridLayout createNodeAggregatesGroupLayout() {
		GridLayout layout = new GridLayout(1, false);

		return layout;
	}

	private static Group createEdgeAggregatesGroup(Composite parent, GUIModel model) {
		Group edgeAggregatesGroup = new Group(parent, SWT.SHADOW_ETCHED_IN);
		edgeAggregatesGroup.setLayoutData(createEdgeAggregatesGroupLayoutData());
		edgeAggregatesGroup.setLayout(createEdgeAggregatesGroupLayout());
		edgeAggregatesGroup.setText(EDGE_AGGREGATES_GROUP_TEXT);

		return edgeAggregatesGroup;
	}

	private static GridData createEdgeAggregatesGroupLayoutData() {
		GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, true);
		layoutData.horizontalSpan = 2;

		return layoutData;
	}

	private static GridLayout createEdgeAggregatesGroupLayout() {
		GridLayout layout = new GridLayout(1, false);

		return layout;
	}

	private static GUIModelField<String, Combo, DropDownDataSynchronizer> createLeafSelectionField(
			Composite parent, StarDatabase starDatabase, GUIModel model) {
		Label label = new Label(parent, SWT.READ_ONLY);
		label.setLayoutData(createLeafSelectionFieldLabelLayoutData());
		label.setText(LEAF_FIELD_LABEL);

		Collection<String> columnNames = starDatabase.getCoreTableDescriptor().getColumnNames();
		GUIModelField<String, Combo, DropDownDataSynchronizer> leafField =
			model.addSingleSelectionDropDown(
			LEAF_FIELD_NAME,
			0,
			columnNames,
			MapUtilities.mirror(columnNames),
			parent,
			SWT.BORDER | SWT.DROP_DOWN | SWT.READ_ONLY);
		leafField.getWidget().setLayoutData(createLeafSelectionFieldLayoutData());

		return leafField;
	}

	private static GridData createLeafSelectionFieldLabelLayoutData() {
		GridData layoutData = new GridData(SWT.LEFT, SWT.CENTER, false, false);

		return layoutData;
	}

	private static GridData createLeafSelectionFieldLayoutData() {
		GridData layoutData = new GridData(SWT.FILL, SWT.CENTER, true, true);

		return layoutData;
	}

	private static AggregateTable createTable(Composite parent, GUIModel model) {
		AggregateTable table = new AggregateTable(model, parent, createTableLayoutData());

		return table;
	}

	private static GridData createTableLayoutData() {
		GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, true);

		return layoutData;
	}

	public static void main(String[] arguments) {
		Database database = null;
		Map<String, String> columnNamesToTypes = createColumnNamesToTypes();
		CoreTableDescriptor coreTableDescriptor = new CoreTableDescriptor(columnNamesToTypes);
		Map<String, LeafTableDescriptor> leafTableDescriptorsByName =
			new HashMap<String, LeafTableDescriptor>();
		StarDatabase starDatabase =
			new StarDatabase(database, coreTableDescriptor, leafTableDescriptorsByName);

		createGUI("Extract Co-Occurrence Network", WINDOW_WIDTH, WINDOW_HEIGHT, starDatabase);
	}

	private static Map<String, String> createColumnNamesToTypes() {
		Map<String, String> columnNamesToTypes = new HashMap<String, String>();
		columnNamesToTypes.put("CITES", "INTEGER");

		return columnNamesToTypes;
	}
}