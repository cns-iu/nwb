package edu.iu.sci2.database.star.extract.network.guibuilder;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cishell.service.database.Database;
import org.cishell.utilities.ArrayListUtilities;
import org.cishell.utilities.CollectionUtilities;
import org.cishell.utilities.MapUtilities;
import org.cishell.utilities.swt.GUIBuilderUtilities;
import org.cishell.utilities.swt.SWTUtilities;
import org.cishell.utilities.swt.model.GUIModel;
import org.cishell.utilities.swt.model.GUIModelField;
import org.cishell.utilities.swt.model.datasynchronizer.DropDownDataSynchronizer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
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

public class DirectedNetworkGUIBuilder extends GUIBuilder {
	public static final String INSTRUCTIONS_LABEL_TEXT = "INSERT INSTRUCTIONS HERE: ";
	public static final String TUTORIAL_URL =
		"https://nwb.slis.indiana.edu/community/?n=Sci2Algorithm.ExtractStarDirectedNetwork";
	public static final String TUTORIAL_DISPLAY_URL = "INSERT INSTRUCTIONS HERE";

	public static final String SOURCE_LEAF_FIELD_LABEL =
		"Choose the Source for your directed network extraction: ";
	public static final String TARGET_LEAF_FIELD_LABEL =
		"Choose the Target for your directed network extraction: ";
	public static final String HEADER_GROUP_TEXT = "";

	public static final String CORE_OPTION = "Core Entity";
	public static final Collection<String> OPTIONS_TO_ADD_TO_FRONT_OF_LEAF_WIDGET =
		Collections.unmodifiableList(Arrays.asList(CORE_OPTION));

	public GUIModel createGUI(
			String windowTitle, int windowWidth, int windowHeight, StarDatabase starDatabase) {
		// TODO: Verify that starDatabase is valid for us.

		GUIModel model = new GUIModel();

		/* Create the GUI shell, and set up its basic structure (header, node aggregates,
		 *  edge aggregates).
		 */

		Display display = GUIBuilderUtilities.createDisplay();
		Shell shell = GUIBuilderUtilities.createShell(
			display, windowTitle, windowWidth, windowHeight, 1, false);
		Group headerGroup = createHeaderGroup(shell);
		Group nodeAggregatesGroup = createNodeAggregatesGroup(shell, model);
		Group edgeAggregatesGroup = createEdgeAggregatesGroup(shell, model);

		@SuppressWarnings("unused")
    	StyledText instructionsLabel = createInstructionsLabel(headerGroup);

		// Create the options for the Source and Target selection fields.

		List<String> allOptionsExceptCore = starDatabase.getLeafTableNames();
		Collections.sort(allOptionsExceptCore);
		Map<String, String> allOptionsByLabelsExceptCore =
			MapUtilities.mirror(allOptionsExceptCore);
		List<String> allOptions = ArrayListUtilities.unionCollectionsAsList(
			OPTIONS_TO_ADD_TO_FRONT_OF_LEAF_WIDGET,
			starDatabase.getLeafTableNames(),
			null);
		Collections.sort(allOptions);
		Map<String, String> allOptionsByLabels = MapUtilities.mirror(allOptions);

		// Create and setup the Source and Target selection fields.

		GUIModelField<String, Combo, DropDownDataSynchronizer> sourceLeafField =
			createLeafSelectionField(
				SOURCE_LEAF_FIELD_LABEL,
				SOURCE_LEAF_FIELD_NAME,
				allOptions,
				allOptionsByLabels,
				starDatabase,
				model,
				headerGroup);

		GUIModelField<String, Combo, DropDownDataSynchronizer> targetLeafField =
			createLeafSelectionField(
				TARGET_LEAF_FIELD_LABEL,
				TARGET_LEAF_FIELD_NAME,
				allOptions,
				allOptionsByLabels,
				starDatabase,
				model,
				headerGroup);

		AttributeListWidget nodeAggregatesWidget = createAggregateWidget(
			model,
			NODE_ATTRIBUTE_FUNCTION_BASE_NAME,
			NODE_CORE_ENTITY_COLUMN_BASE_NAME,
			starDatabase.getCoreTableDescriptor().getColumnNames(),
			NODE_RESULT_BASE_NAME,
			NODE_TYPE,
			nodeAggregatesGroup);
		AttributeListWidget edgeAggregatesWidget = createAggregateWidget(
			model,
			EDGE_ATTRIBUTE_FUNCTION_BASE_NAME,
			EDGE_CORE_ENTITY_COLUMN_BASE_NAME,
			starDatabase.getCoreTableDescriptor().getColumnNames(),
			EDGE_RESULT_BASE_NAME,
			EDGE_TYPE,
			edgeAggregatesGroup);

		for (int ii = 0; ii < DEFAULT_AGGREGATE_WIDGET_COUNT; ii++) {
			nodeAggregatesWidget.addComponent(SWT.NONE, null);
			edgeAggregatesWidget.addComponent(SWT.NONE, null);
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

	private static StyledText createInstructionsLabel(Composite parent) {
		StyledText instructionsLabel =
			new StyledText(parent, SWT.LEFT | SWT.READ_ONLY | SWT.WRAP);
		instructionsLabel.setBackground(
			parent.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		instructionsLabel.setLayoutData(createInstructionsLabelLayoutData());
		instructionsLabel.getCaret().setVisible(false);

		SWTUtilities.styledPrint(
			instructionsLabel,
			INSTRUCTIONS_LABEL_TEXT,
			parent.getDisplay().getSystemColor(SWT.COLOR_BLACK),
			SWT.NORMAL);
		SWTUtilities.printURL(
			parent,
			instructionsLabel,
			TUTORIAL_URL,
			TUTORIAL_DISPLAY_URL,
			parent.getDisplay().getSystemColor(SWT.COLOR_BLUE),
			SWT.BOLD);

		return instructionsLabel;
	}

	private static GridData createInstructionsLabelLayoutData() {
		GridData layoutData = new GridData(SWT.FILL, SWT.TOP, true, false);
		layoutData.horizontalSpan = 2;

		return layoutData;
	}

	private static GUIModelField<String, Combo, DropDownDataSynchronizer> createLeafSelectionField(
			String labelText,
			String fieldName,
			Collection<String> allOptions,
			Map<String, String> allOptionsByLabels,
			StarDatabase starDatabase,
			GUIModel model,
			Composite parent) {
		Label label = new Label(parent, SWT.READ_ONLY);
		label.setLayoutData(createLeafSelectionFieldLabelLayoutData());
		label.setText(labelText);

		GUIModelField<String, Combo, DropDownDataSynchronizer> leafField = model.addDropDown(
			fieldName, 0, allOptions, allOptionsByLabels, parent, SWT.BORDER | SWT.READ_ONLY);
		leafField.getWidget().setLayoutData(createLeafSelectionFieldLayoutData());
		leafField.setValue(CollectionUtilities.get(allOptions, 1));

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

	// TODO:
	private static SelectionListener createLeafSelectionListener(
			final Collection<String> allOptionsExceptCore,
			final Map<String, String> allOptionsByLabelsExceptCore,
			final Collection<String> allOptions,
			final Map<String, String> allOptionsByLabels,
			final GUIModelField<String, Combo, DropDownDataSynchronizer> sourceLeafField,
			final GUIModelField<String, Combo, DropDownDataSynchronizer> targetLeafField) {
		return new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent event) {
				selected(event);
			}

			public void widgetSelected(SelectionEvent event) {
				selected(event);
			}

			private void selected(SelectionEvent event) {
			}
		};
	}

	private static AttributeListWidget createAggregateWidget(
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
		DirectedNetworkGUIBuilder guiBuilder = new DirectedNetworkGUIBuilder();

		guiBuilder.createGUI(
			"Extract Directed Network", WINDOW_WIDTH, WINDOW_HEIGHT, starDatabase);
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