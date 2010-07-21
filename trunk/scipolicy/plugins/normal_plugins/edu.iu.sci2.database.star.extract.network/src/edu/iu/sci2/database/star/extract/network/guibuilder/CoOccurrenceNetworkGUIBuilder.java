package edu.iu.sci2.database.star.extract.network.guibuilder;

import java.util.Collection;
import java.util.Map;

import org.cishell.utilities.MapUtilities;
import org.cishell.utilities.swt.GUIBuilderUtilities;
import org.cishell.utilities.swt.SWTUtilities;
import org.cishell.utilities.swt.model.GUIModel;
import org.cishell.utilities.swt.model.GUIModelField;
import org.cishell.utilities.swt.model.datasynchronizer.DropDownDataSynchronizer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import edu.iu.sci2.database.star.extract.network.StarDatabaseDescriptor;
import edu.iu.sci2.database.star.extract.network.guibuilder.attribute.AttributeListWidget;

public class CoOccurrenceNetworkGUIBuilder extends GUIBuilder {
	public static final String INSTRUCTIONS_LABEL_TEXT = "INSERT INSTRUCTIONS HERE: ";
	public static final String TUTORIAL_URL =
		"https://nwb.slis.indiana.edu/community/?n=Sci2Algorithm.ExtractStarCoOccurrenceNetwork";
	public static final String TUTORIAL_DISPLAY_URL = "INSERT INSTRUCTIONS HERE";

	public static final String LEAF_FIELD_LABEL =
		"Choose the Leaf column to extract the co-occurrence network on: ";
	public static final String HEADER_GROUP_TEXT = "";

	public static final String LEAF_FIELD_NAME = "leafEntity";

	public GUIModel createGUI(
			String windowTitle,
			int windowWidth,
			int windowHeight,
			StarDatabaseDescriptor databaseDescriptor) {
		// TODO: Verify that databaseDescriptor is valid for us.

		GUIModel model = new GUIModel();

		/* Create the GUI shell, and set up its basic structure (header, node aggregates,
		 *  edge aggregates).
		 */

		Display display = GUIBuilderUtilities.createDisplay();
		Shell shell = GUIBuilderUtilities.createShell(
			display, windowTitle, windowWidth, windowHeight, 1, false);
		Group headerGroup = createHeaderGroup(shell);
		Group nodeAggregatesGroup = createAggregatesGroup(shell, NODE_ATTRIBUTES_GROUP_TEXT);
		Group edgeAggregatesGroup = createAggregatesGroup(shell, EDGE_ATTRIBUTES_GROUP_TEXT);
		Group footerGroup = createFooterGroup(shell);

		@SuppressWarnings("unused")
    	StyledText instructionsLabel = createInstructionsLabel(headerGroup);

		// Create and setup the Leaf selection field.

		@SuppressWarnings("unused")
		GUIModelField<String, Combo, DropDownDataSynchronizer> leafField =
			createLeafSelectionField(headerGroup, databaseDescriptor, model);

		// Create the widget that allows users to specify node and edge aggregate fields.

		AttributeListWidget nodeAggregatesTable = createAggregateWidget(
			model,
			NODE_ATTRIBUTE_FUNCTION_GROUP_NAME,
			NODE_CORE_ENTITY_COLUMN_GROUP_NAME,
			databaseDescriptor.getCoreTableDescriptor().getColumnNames(),
			databaseDescriptor.getCoreTableDescriptor().getColumnNamesByLabels(),
			NODE_RESULT_NAME_GROUP_NAME,
			NODE_TYPE,
			nodeAggregatesGroup);
		AttributeListWidget edgeAggregatesTable = createAggregateWidget(
			model,
			EDGE_ATTRIBUTE_FUNCTION_GROUP_NAME,
			EDGE_CORE_ENTITY_COLUMN_GROUP_NAME,
			databaseDescriptor.getCoreTableDescriptor().getColumnNames(),
			databaseDescriptor.getCoreTableDescriptor().getColumnNamesByLabels(),
			EDGE_RESULT_NAME_GROUP_NAME,
			EDGE_TYPE,
			edgeAggregatesGroup);

		// Create the finished button so the user can actually execute the resulting queries.

		@SuppressWarnings("unused")
		Button finishedButton = createFinishedButton(footerGroup, 1);

		// Fill the aggregate widgets with some aggregate fields by default (for the user's ease).

		for (int ii = 0; ii < DEFAULT_AGGREGATE_WIDGET_COUNT; ii++) {
			nodeAggregatesTable.addComponent(SWT.NONE, null);
			edgeAggregatesTable.addComponent(SWT.NONE, null);
		}

		// Run the GUI and return the model with the data that the user entered.

		runGUI(display, shell, windowHeight);

		return model;
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
			Composite parent, StarDatabaseDescriptor databaseDescriptor, GUIModel model) {
		Label label = new Label(parent, SWT.READ_ONLY);
		label.setLayoutData(createLeafSelectionFieldLabelLayoutData());
		label.setText(LEAF_FIELD_LABEL);

		Collection<String> columnNames = databaseDescriptor.getLeafTableNames();
		Map<String, String> columnOptions = databaseDescriptor.getTableNameOptionsWithoutCore();
		GUIModelField<String, Combo, DropDownDataSynchronizer> leafField =
			model.addDropDown(
			HEADER_GROUP_NAME,
			LEAF_FIELD_NAME,
			0,
			columnNames,
			columnOptions,
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
}