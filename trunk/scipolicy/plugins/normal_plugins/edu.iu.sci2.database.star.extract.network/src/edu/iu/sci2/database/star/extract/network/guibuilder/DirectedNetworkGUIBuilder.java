package edu.iu.sci2.database.star.extract.network.guibuilder;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.cishell.utilities.ArrayListUtilities;
import org.cishell.utilities.CollectionUtilities;
import org.cishell.utilities.ObjectContainer;
import org.cishell.utilities.swt.GUIBuilderUtilities;
import org.cishell.utilities.swt.GUICanceledException;
import org.cishell.utilities.swt.SWTUtilities;
import org.cishell.utilities.swt.model.GUIModel;
import org.cishell.utilities.swt.model.GUIModelField;
import org.cishell.utilities.swt.model.datasynchronizer.DropDownDataSynchronizer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
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

public class DirectedNetworkGUIBuilder extends GUIBuilder {
	public static final String INSTRUCTIONS_LABEL_TEXT = "INSERT INSTRUCTIONS HERE: ";
	public static final String TUTORIAL_URL =
		"https://nwb.slis.indiana.edu/community/?n=Sci2Algorithm.ExtractStarDirectedNetwork";
	public static final String TUTORIAL_DISPLAY_URL = "INSERT INSTRUCTIONS HERE";

	public static final String SOURCE_LEAF_FIELD_LABEL =
		"Choose the Source for your directed network extraction: ";
	public static final String TARGET_LEAF_FIELD_LABEL =
		"Choose the Target for your directed network extraction: ";

	public static final String SOURCE_LEAF_FIELD_NAME = "sourceLeafEntity";
	public static final String TARGET_LEAF_FIELD_NAME = "targetLeafEntity";

	public GUIModel createGUI(
			String windowTitle,
			int windowWidth,
			int windowHeight,
			StarDatabaseDescriptor databaseDescriptor) throws GUICanceledException {
		// TODO: Verify that databaseDescriptor is valid for us.

		final String coreEntityHumanReadableName =
			databaseDescriptor.getCoreTableDescriptor().getCoreEntityHumanReadableName();
		final String coreEntityTableName =
			databaseDescriptor.getCoreTableDescriptor().getCoreEntityTableName();
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

		// Create the options for the Source and Target selection fields.

		List<String> allOptionsExceptCore =
			ArrayListUtilities.copyAndSort(databaseDescriptor.getLeafTableNames());
		Map<String, String> allOptionsByLabelsExceptCore =
			databaseDescriptor.getTableNameOptionsWithoutCore();
		List<String> allOptions =
			ArrayListUtilities.copyAndSort(databaseDescriptor.getLeafTableNames());
		allOptions = ArrayListUtilities.unionCollectionsAsList(
			Arrays.asList(coreEntityHumanReadableName),
			allOptions,
			null);
		Map<String, String> allOptionsByLabels = databaseDescriptor.getTableNameOptionsWithCore();

		// Create and setup the Source and Target selection fields.

		GUIModelField<String, Combo, DropDownDataSynchronizer> sourceLeafField =
			createLeafSelectionField(
				SOURCE_LEAF_FIELD_LABEL,
				HEADER_GROUP_NAME,
				SOURCE_LEAF_FIELD_NAME,
				allOptions,
				allOptionsByLabels,
				databaseDescriptor,
				model,
				headerGroup);
		GUIModelField<String, Combo, DropDownDataSynchronizer> targetLeafField =
			createLeafSelectionField(
				TARGET_LEAF_FIELD_LABEL,
				HEADER_GROUP_NAME,
				TARGET_LEAF_FIELD_NAME,
				allOptions,
				allOptionsByLabels,
				databaseDescriptor,
				model,
				headerGroup);

		/*
		 * Make it so only one of the Source and Target selection fields can be the core
		 *  entity option.
		 */

		sourceLeafField.getWidget().addSelectionListener(createLeafSelectionListener(
			allOptionsExceptCore,
			allOptionsByLabelsExceptCore,
			allOptions,
			allOptionsByLabels,
			coreEntityHumanReadableName,
			coreEntityTableName,
			sourceLeafField,
			targetLeafField));
		targetLeafField.getWidget().addSelectionListener(createLeafSelectionListener(
			allOptionsExceptCore,
			allOptionsByLabelsExceptCore,
			allOptions,
			allOptionsByLabels,
			coreEntityHumanReadableName,
			coreEntityTableName,
			targetLeafField,
			sourceLeafField));

		// Create the widget that allows users to specify node and edge aggregate fields.

		AttributeListWidget nodeAggregatesWidget = createAggregateWidget(
			model,
			NODE_ATTRIBUTE_FUNCTION_GROUP_NAME,
			NODE_CORE_ENTITY_COLUMN_GROUP_NAME,
			databaseDescriptor.getCoreTableDescriptor().getColumnNames(),
			databaseDescriptor.getCoreTableDescriptor().getColumnNamesByLabels(),
			NODE_RESULT_NAME_GROUP_NAME,
			NODE_TYPE,
			nodeAggregatesGroup);
		AttributeListWidget edgeAggregatesWidget = createAggregateWidget(
			model,
			EDGE_ATTRIBUTE_FUNCTION_GROUP_NAME,
			EDGE_CORE_ENTITY_COLUMN_GROUP_NAME,
			databaseDescriptor.getCoreTableDescriptor().getColumnNames(),
			databaseDescriptor.getCoreTableDescriptor().getColumnNamesByLabels(),
			EDGE_RESULT_NAME_GROUP_NAME,
			EDGE_TYPE,
			edgeAggregatesGroup);

		// Create the finished button so the user can actually execute the resulting queries.

		final ObjectContainer<Boolean> userFinished = new ObjectContainer<Boolean>(false);
		@SuppressWarnings("unused")
		Button finishedButton = createFinishedButton(footerGroup, 1, userFinished);

		// Fill the aggregate widgets with some aggregate fields by default (for the user's ease).

		for (int ii = 0; ii < DEFAULT_AGGREGATE_WIDGET_COUNT; ii++) {
			nodeAggregatesWidget.addComponent(SWT.NONE, null);
			edgeAggregatesWidget.addComponent(SWT.NONE, null);
		}

		// Set the GUI up to be cancelable.

		final ObjectContainer<GUICanceledException> exceptionThrown =
			new ObjectContainer<GUICanceledException>();
		GUIBuilderUtilities.setCancelable(shell, exceptionThrown);

		// Run the GUI and return the model with the data that the user entered.

		runGUI(display, shell, windowHeight);

		if ((userFinished.object == false) && (exceptionThrown.object != null)) {
    		throw new GUICanceledException(
    			exceptionThrown.object.getMessage(), exceptionThrown.object);
    	}

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
			String labelText,
			String groupName,
			String fieldName,
			Collection<String> allOptionLabels,
			Map<String, String> allOptionValuesByLabels,
			StarDatabaseDescriptor databaseDescriptor,
			GUIModel model,
			Composite parent) {
		Label label = new Label(parent, SWT.READ_ONLY);
		label.setLayoutData(createLeafSelectionFieldLabelLayoutData());
		label.setText(labelText);

		GUIModelField<String, Combo, DropDownDataSynchronizer> leafField = model.addDropDown(
			groupName,
			fieldName,
			0,
			allOptionLabels,
			allOptionValuesByLabels,
			parent,
			SWT.BORDER | SWT.READ_ONLY);
		leafField.getWidget().setLayoutData(createLeafSelectionFieldLayoutData());
		leafField.setValue(
			allOptionValuesByLabels.get(CollectionUtilities.get(allOptionLabels, 1)));

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

	private static SelectionListener createLeafSelectionListener(
			final List<String> allOptionsExceptCore,
			final Map<String, String> allOptionsByLabelsExceptCore,
			final List<String> allOptions,
			final Map<String, String> allOptionsByLabels,
			final String coreEntityHumanReadableName,
			final String coreEntityTableName,
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
				if (coreEntityTableName.equals(sourceLeafField.getValue())) {
					final String targetLeafFieldValue = targetLeafField.getValue();
					targetLeafField.getDataSynchronizer().setOptions(
						allOptionsExceptCore, allOptionsByLabelsExceptCore);
					targetLeafField.setValue(targetLeafFieldValue);
				} else if (coreEntityTableName.equals(sourceLeafField.getPreviousValue())) {
					final String targetLeafFieldValue = targetLeafField.getValue();
					targetLeafField.getDataSynchronizer().setOptions(
						allOptions, allOptionsByLabels);
					targetLeafField.setValue(targetLeafFieldValue);
				}
			}
		};
	}
}