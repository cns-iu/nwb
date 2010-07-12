package edu.iu.sci2.database.star.extract.network.guibuilder;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.cishell.service.database.Database;
import org.cishell.utilities.MapUtilities;
import org.cishell.utilities.swt.GUIBuilderUtilities;
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

import edu.iu.sci2.database.star.extract.network.CoreTableDescriptor;
import edu.iu.sci2.database.star.extract.network.LeafTableDescriptor;
import edu.iu.sci2.database.star.extract.network.StarDatabase;
import edu.iu.sci2.database.star.extract.network.guibuilder.attribute.AttributeListWidget;

public class SingleLeafGUIBuilder extends GUIBuilder {
	public static final String LEAF_FIELD_LABEL =
		"Choose the Leaf column to extract the co-occurrence network on: ";
	public static final String HEADER_GROUP_TEXT = "";

	public GUIModel createGUI(
			String windowTitle, int windowWidth, int windowHeight, StarDatabase starDatabase) {
		GUIModel model = new GUIModel();

		Display display = GUIBuilderUtilities.createDisplay();
		Shell shell = GUIBuilderUtilities.createShell(
			display, windowTitle, windowWidth, windowHeight, 1, false);
		Group headerGroup = createHeaderGroup(shell);
		Group nodeAggregatesGroup = createNodeAggregatesGroup(shell, model);
		Group edgeAggregatesGroup = createEdgeAggregatesGroup(shell, model);

		@SuppressWarnings("unused")
		GUIModelField<String, Combo, DropDownDataSynchronizer> leafField =
			createLeafSelectionField(headerGroup, starDatabase, model);

		AttributeListWidget nodeAggregatesTable = createAggregateList(
			model,
			NODE_ATTRIBUTE_FUNCTION_BASE_NAME,
			NODE_CORE_ENTITY_COLUMN_BASE_NAME,
			starDatabase.getCoreTableDescriptor().getColumnNames(),
			NODE_RESULT_BASE_NAME,
			NODE_TYPE,
			nodeAggregatesGroup);
		AttributeListWidget edgeAggregatesTable = createAggregateList(
			model,
			EDGE_ATTRIBUTE_FUNCTION_BASE_NAME,
			EDGE_CORE_ENTITY_COLUMN_BASE_NAME,
			starDatabase.getCoreTableDescriptor().getColumnNames(),
			EDGE_RESULT_BASE_NAME,
			EDGE_TYPE,
			edgeAggregatesGroup);

		for (int ii = 0; ii < DEFAULT_AGGREGATE_WIDGET_COUNT; ii++) {
			nodeAggregatesTable.addComponent(SWT.NONE, null);
			edgeAggregatesTable.addComponent(SWT.NONE, null);
		}

		runGUI(display, shell, windowHeight);

		return model;
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
		nodeAggregatesGroup.setText(NODE_ATTRIBUTES_GROUP_TEXT);

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
		edgeAggregatesGroup.setText(EDGE_ATTRIBUTES_GROUP_TEXT);

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

		Collection<String> columnNames = starDatabase.getLeafTableNames();
		GUIModelField<String, Combo, DropDownDataSynchronizer> leafField =
			model.addSingleSelectionDropDown(
			LEAF_FIELD_NAME,
			0,
			columnNames,
			MapUtilities.mirror(columnNames),
			parent,
			SWT.BORDER | SWT.READ_ONLY);
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

	private static AttributeListWidget createAggregateList(
			GUIModel model,
			String aggregateFunctionBaseName,
			String coreEntityColumnName,
			Collection<String> coreEntityColumns,
			String resultColumnLabelBaseName,
			String type,
			Composite parent) {
		AttributeListWidget aggregateList = new AttributeListWidget(
			model,
			aggregateFunctionBaseName,
			coreEntityColumnName,
			coreEntityColumns,
			resultColumnLabelBaseName,
			type,
			parent);
		aggregateList.setLayoutData(createAggregateListLayoutData());

		return aggregateList;
	}

	private static GridData createAggregateListLayoutData() {
		GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, true);
		layoutData.heightHint = 300;

		return layoutData;
	}

	public static void main(String[] arguments) {
		Database database = null;
		Map<String, String> columnNamesToTypes = createColumnNamesToTypes();
		CoreTableDescriptor coreTableDescriptor = new CoreTableDescriptor(columnNamesToTypes);
		Map<String, LeafTableDescriptor> leafTableDescriptorsByName =
			createLeafTableDescriptorsByName();
		StarDatabase starDatabase =
			new StarDatabase(database, coreTableDescriptor, leafTableDescriptorsByName);
		SingleLeafGUIBuilder guiBuilder = new SingleLeafGUIBuilder();

		guiBuilder.createGUI(
			"Extract Co-Occurrence Network", WINDOW_WIDTH, WINDOW_HEIGHT, starDatabase);
	}

	private static Map<String, String> createColumnNamesToTypes() {
		Map<String, String> columnNamesToTypes = new HashMap<String, String>();
		columnNamesToTypes.put("CITES", "INTEGER");
		columnNamesToTypes.put("YEAR", "INTEGER");

		return columnNamesToTypes;
	}

	private static Map<String, LeafTableDescriptor> createLeafTableDescriptorsByName() {
		String[] types = new String[] { "STRING", "INTEGER", "DOUBLE" };

		Map<String, LeafTableDescriptor> leafTableDescriptorsByName =
			new HashMap<String, LeafTableDescriptor>();

		for (int ii = 0; ii < 10; ii++) {
			String name = "Leaf " + ii;
			leafTableDescriptorsByName.put(
				name, new LeafTableDescriptor(name, types[ii % types.length]));
		}

		return leafTableDescriptorsByName;
	}
}