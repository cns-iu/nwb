package edu.iu.sci2.database.star.extract.network.guibuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.cishell.utilities.ArrayListUtilities;
import org.cishell.utilities.CollectionUtilities;
import org.cishell.utility.datastructure.ObjectContainer;
import org.cishell.utility.datastructure.datamodel.DataModel;
import org.cishell.utility.datastructure.datamodel.exception.ModelValidationException;
import org.cishell.utility.datastructure.datamodel.exception.UniqueNameException;
import org.cishell.utility.datastructure.datamodel.field.DataModelField;
import org.cishell.utility.datastructure.datamodel.field.FieldValidationRule;
import org.cishell.utility.swt.GUIBuilderUtilities;
import org.cishell.utility.swt.GUICanceledException;
import org.cishell.utility.swt.SWTUtilities;
import org.cishell.utility.swt.WidgetConstructionException;
import org.cishell.utility.swt.model.SWTModel;
import org.cishell.utility.swt.model.SWTModelField;
import org.cishell.utility.swt.model.datasynchronizer.DropDownDataSynchronizer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import edu.iu.sci2.database.star.extract.network.StarDatabaseDescriptor;
import edu.iu.sci2.database.star.extract.network.guibuilder.attribute.AttributeListWidget;

public class DirectedNetworkGUIBuilder extends GUIBuilder {
	public static final String INSTRUCTIONS_LABEL_TEXT =
		"Choose the entity tables that extract your bipartite network should be " +
		"extracted between.\n" +
		"Then, setup any node and edge attributes you want on your resulting network.\n" +
		"For more information see the Sci2 tutorial at: ";
	public static final String TUTORIAL_URL =
		"https://nwb.slis.indiana.edu/community/" +
		"?n=Sci2Algorithm.GenericCSVExtractBipartiteNetwork";
	public static final String TUTORIAL_DISPLAY_URL = "Sci2 Tutorial";

	public static final String SOURCE_LEAF_FIELD_LABEL =
		"Choose the Source for your bipartite network extraction: ";
	public static final String TARGET_LEAF_FIELD_LABEL =
		"Choose the Target for your bipartite network extraction: ";

	public static final String SOURCE_LEAF_FIELD_NAME = "sourceLeafEntity";
	public static final String TARGET_LEAF_FIELD_NAME = "targetLeafEntity";

	public SWTModel createGUI(
			String windowTitle,
			int windowWidth,
			int windowHeight,
			StarDatabaseDescriptor databaseDescriptor)
			throws GUICanceledException, UniqueNameException {
		// TODO: Verify that databaseDescriptor is valid for us.

		final String coreEntityHumanReadableName =
			databaseDescriptor.getCoreTableDescriptor().getCoreEntityHumanReadableName();
//		final String coreEntityTableName =
//			databaseDescriptor.getCoreTableDescriptor().getCoreEntityTableName();
		SWTModel model = new SWTModel(SWT.NONE);

		/* Create the GUI shell, and set up its basic structure (header, node aggregates,
		 *  edge aggregates).
		 */

		Display display = GUIBuilderUtilities.createDisplay();
		Shell shell = GUIBuilderUtilities.createShell(
			display, windowTitle, windowWidth, windowHeight, 1, false);
		Composite instructionsArea = createInstructionsArea(shell);
		Group headerGroup = createHeaderGroup(shell);
		Group nodeAggregatesGroup = createAggregatesGroup(shell, NODE_ATTRIBUTES_GROUP_TEXT);
		Group edgeAggregatesGroup = createAggregatesGroup(shell, EDGE_ATTRIBUTES_GROUP_TEXT);
		Group footerGroup = createFooterGroup(shell);

    	createInstructionsLabel(instructionsArea);

		// Create the options for the Source and Target selection fields.

//		List<String> allOptionsExceptCore =
//			ArrayListUtilities.copyAndSort(databaseDescriptor.getLeafTableNames());
//		Map<String, String> allOptionsByLabelsExceptCore =
//			databaseDescriptor.getTableNameOptionsWithoutCore();
		List<String> allOptions =
			ArrayListUtilities.copyAndSort(databaseDescriptor.getLeafTableNames());
		allOptions = ArrayListUtilities.unionCollectionsAsList(
			Arrays.asList(coreEntityHumanReadableName),
			allOptions,
			null);
		Map<String, String> allOptionsByLabels = databaseDescriptor.getTableNameOptionsWithCore();

		// Create and setup the Source and Target selection fields.

		SWTModelField<String, Combo, DropDownDataSynchronizer<String>> sourceLeafField =
			createLeafSelectionField(
				SOURCE_LEAF_FIELD_LABEL,
				HEADER_GROUP_NAME,
				SOURCE_LEAF_FIELD_NAME,
				allOptions,
				allOptionsByLabels,
				databaseDescriptor,
				model,
				headerGroup);
		this.disableFinishedButtonAction.addField(sourceLeafField, true);
		SWTModelField<String, Combo, DropDownDataSynchronizer<String>> targetLeafField =
			createLeafSelectionField(
				TARGET_LEAF_FIELD_LABEL,
				HEADER_GROUP_NAME,
				TARGET_LEAF_FIELD_NAME,
				allOptions,
				allOptionsByLabels,
				databaseDescriptor,
				model,
				headerGroup);
		this.disableFinishedButtonAction.addField(sourceLeafField, true);

		/*
		 * Make it so only one of the Source and Target selection fields can be the core
		 *  entity option.
		 */

//		sourceLeafField.getWidget().addSelectionListener(createLeafSelectionListener(
//			allOptionsExceptCore,
//			allOptionsByLabelsExceptCore,
//			allOptions,
//			allOptionsByLabels,
//			coreEntityHumanReadableName,
//			coreEntityTableName,
//			sourceLeafField,
//			targetLeafField));
//		targetLeafField.getWidget().addSelectionListener(createLeafSelectionListener(
//			allOptionsExceptCore,
//			allOptionsByLabelsExceptCore,
//			allOptions,
//			allOptionsByLabels,
//			coreEntityHumanReadableName,
//			coreEntityTableName,
//			targetLeafField,
//			sourceLeafField));

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
		this.finishedButton = createFinishedButton(footerGroup, 1, userFinished);

		// Fill the aggregate widgets with some aggregate fields by default (for the user's ease).

		try {
			for (int ii = 0; ii < DEFAULT_AGGREGATE_WIDGET_COUNT; ii++) {
				nodeAggregatesWidget.addComponent(SWT.NONE, null);
				edgeAggregatesWidget.addComponent(SWT.NONE, null);
			}
		} catch (WidgetConstructionException e) {
			throw new RuntimeException(e.getMessage(), e);
		}

		// Set the GUI up to be cancelable.

		final ObjectContainer<GUICanceledException> exceptionThrown =
			new ObjectContainer<GUICanceledException>();
		GUIBuilderUtilities.setCancelable(shell, exceptionThrown);

		// Set up validation for the leaf table selectors.

		LeafTableSelectionValidator leafTableSelectionValidator =
			new LeafTableSelectionValidator(sourceLeafField, targetLeafField);
//		sourceLeafField.addValidator(leafTableSelectionValidator, true, model);
//		targetLeafField.addValidator(leafTableSelectionValidator, true, model);
		sourceLeafField.addValidationRule(leafTableSelectionValidator, false, model);
		targetLeafField.addValidationRule(leafTableSelectionValidator, false, model);
		this.finishedButton.setEnabled(false);

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
			parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
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

	private SWTModelField<
			String, Combo, DropDownDataSynchronizer<String>> createLeafSelectionField(
				String labelText,
				String groupName,
				String fieldName,
				Collection<String> allOptionLabels,
				Map<String, String> allOptionValuesByLabels,
				StarDatabaseDescriptor databaseDescriptor,
				SWTModel model,
				Composite parent) throws UniqueNameException {
		Label label = new Label(parent, SWT.READ_ONLY);
		label.setLayoutData(createLeafSelectionFieldLabelLayoutData());
		label.setText(labelText);

		SWTModelField<String, Combo, DropDownDataSynchronizer<String>> leafField =
			model.addDropDown(
				fieldName,
				"",
				groupName,
				0,
				allOptionLabels,
				allOptionValuesByLabels,
				parent,
				SWT.BORDER | SWT.READ_ONLY);
		leafField.getWidget().setLayoutData(createLeafSelectionFieldLayoutData());
		leafField.setValue(
			allOptionValuesByLabels.get(CollectionUtilities.get(allOptionLabels, 1)));
		leafField.addValidationAction(this.disableFinishedButtonAction);

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

//	private static SelectionListener createLeafSelectionListener(
//			final List<String> allOptionsExceptCore,
//			final Map<String, String> allOptionsByLabelsExceptCore,
//			final List<String> allOptions,
//			final Map<String, String> allOptionsByLabels,
//			final String coreEntityHumanReadableName,
//			final String coreEntityTableName,
//			final SWTModelField<String, Combo, DropDownDataSynchronizer<String>> sourceLeafField,
//			final SWTModelField<String, Combo, DropDownDataSynchronizer<String>> targetLeafField) {
//		return new SelectionListener() {
//			public void widgetDefaultSelected(SelectionEvent event) {
//				selected(event);
//			}
//
//			public void widgetSelected(SelectionEvent event) {
//				selected(event);
//			}
//
//			private void selected(SelectionEvent event) {
//				if (coreEntityTableName.equals(sourceLeafField.getValue())) {
//					final String targetLeafFieldValue = targetLeafField.getValue();
//					targetLeafField.getDataSynchronizer().setOptions(
//						allOptionsExceptCore, allOptionsByLabelsExceptCore);
//					targetLeafField.setValue(targetLeafFieldValue);
//				} else if (coreEntityTableName.equals(sourceLeafField.getPreviousValue())) {
//					final String targetLeafFieldValue = targetLeafField.getValue();
//					targetLeafField.getDataSynchronizer().setOptions(
//						allOptions, allOptionsByLabels);
//					targetLeafField.setValue(targetLeafFieldValue);
//				}
//			}
//		};
//	}

	private class LeafTableSelectionValidator implements FieldValidationRule<String> {
		private SWTModelField<String, Combo, DropDownDataSynchronizer<String>> sourceLeafField;
		private SWTModelField<String, Combo, DropDownDataSynchronizer<String>> targetLeafField;
		private List<DataModelField<?>> alsoValidateFields =
			new ArrayList<DataModelField<?>>(1);

		public LeafTableSelectionValidator(
				SWTModelField<String, Combo, DropDownDataSynchronizer<String>> sourceLeafField,
				SWTModelField<String, Combo, DropDownDataSynchronizer<String>> targetLeafField) {
			this.sourceLeafField = sourceLeafField;
			this.targetLeafField = targetLeafField;
		}

		public void validateField(DataModelField<String> field, DataModel model)
				throws ModelValidationException {
			if (this.sourceLeafField.getValue().equals(this.targetLeafField.getValue())) {
//				if (field == this.sourceLeafField) {
//					this.alsoValidateFields.set(0, this.targetLeafField)
//				}

				String format =
					"The Source and Target leaf tables must differ for this type of network " +
					"extraction.  They both have the '%s' table selected.";
				String exceptionMessage = String.format(format, this.sourceLeafField.getValue());
				throw new ModelValidationException(exceptionMessage);
			}
		}

		public void fieldDisposed(DataModelField<String> field) {
		}
	}
}