package edu.iu.sci2.database.star.extract.network.guibuilder;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.cishell.utilities.ArrayListUtilities;
import org.cishell.utility.datastructure.ObjectContainer;
import org.cishell.utility.datastructure.datamodel.exception.UniqueNameException;
import org.cishell.utility.datastructure.datamodel.field.validation.FieldValidator;
import org.cishell.utility.swt.GUIBuilderUtilities;
import org.cishell.utility.swt.GUICanceledException;
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

import com.google.common.collect.Sets;

import edu.iu.sci2.database.star.extract.common.StarDatabaseDescriptor;
import edu.iu.sci2.database.star.extract.common.guibuilder.DisplayErrorMessagesValidationAction;
import edu.iu.sci2.database.star.extract.common.guibuilder.GUIBuilder;
import edu.iu.sci2.database.star.extract.common.guibuilder.attribute.AttributeListWidget;
import edu.iu.sci2.database.star.extract.common.guibuilder.attribute.AttributeWidgetProperties;

public class TwoLeafTableNetworkGUIBuilder extends GUIBuilder {
	private String instructionsLabelText;
	private String tutorialURL;
	private String tutorialDisplayURL;
	private int instructionsLabelHeight;
	private String sourceLeafFieldLabel;
	private String sourceLeafFieldName;
	private String targetLeafFieldLabel;
	private String targetLeafFieldName;
	private boolean includeCoreTableInLeafSelectors;
	private int defaultAggregateWidgetCount;
	private boolean allowCountDistinctFor1;
	private boolean allowCountDistinctFor2;

	public TwoLeafTableNetworkGUIBuilder(
			StarDatabaseDescriptor databaseDescriptor,
			String instructionsLabelText,
			String tutorialURL,
			String tutorialDisplayURL,
			int instructionsLabelHeight,
			String sourceLeafFieldLabel,
			String sourceLeafFieldName,
			String targetLeafFieldLabel,
			String targetLeafFieldName,
			boolean includeCoreTableInLeafSelectors,
			int defaultAggregateWidgetCount,
			boolean allowCountDistinctFor1,
			boolean allowCountDistinctFor2) {
		super(databaseDescriptor);
		this.instructionsLabelText = instructionsLabelText;
		this.tutorialURL = tutorialURL;
		this.tutorialDisplayURL = tutorialDisplayURL;
		this.instructionsLabelHeight = instructionsLabelHeight;
		this.sourceLeafFieldLabel = sourceLeafFieldLabel;
		this.sourceLeafFieldName = sourceLeafFieldName;
		this.targetLeafFieldLabel = targetLeafFieldLabel;
		this.targetLeafFieldName = targetLeafFieldName;
		this.includeCoreTableInLeafSelectors = includeCoreTableInLeafSelectors;
		this.defaultAggregateWidgetCount = defaultAggregateWidgetCount;
		this.allowCountDistinctFor1 = allowCountDistinctFor1;
		this.allowCountDistinctFor2 = allowCountDistinctFor2;
	}

	@Override
	public String attributeFieldValidator1BaseName() {
		return NODE_ATTRIBUTE_FIELD_VALIDATOR_BASE_NAME;
	}

	@Override
	public String attributeFieldValidator2BaseName() {
		return EDGE_ATTRIBUTE_FIELD_VALIDATOR_BASE_NAME;
	}

	@Override
	@SuppressWarnings("unchecked")	// Arrays.asList creating genericly-typed arrays.
	public SWTModel createGUI(
			String windowTitle,
			int windowWidth,
			int windowHeight,
			StarDatabaseDescriptor databaseDescriptor)
			throws GUICanceledException, UniqueNameException {
		final String coreEntityHumanReadableName =
			databaseDescriptor.getCoreTableDescriptor().getCoreEntityHumanReadableName();
		SWTModel model = new SWTModel(SWT.NONE);
		model.createGroup(ATTRIBUTE_FUNCTION_GROUP1_NAME);
		model.createGroup(CORE_ENTITY_COLUMN_GROUP1_NAME);
		model.createGroup(ATTRIBUTE_NAME_GROUP1_NAME);
		model.createGroup(ATTRIBUTE_FUNCTION_GROUP2_NAME);
		model.createGroup(CORE_ENTITY_COLUMN_GROUP2_NAME);
		model.createGroup(ATTRIBUTE_NAME_GROUP2_NAME);

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

		// Create the instructions/error message label.

    	StyledText instructionsLabel =
    		createInstructionsLabel(instructionsArea, instructionsLabelHeight);
    	DisplayErrorMessagesValidationAction displayErrorMessagesValidationAction =
    		new DisplayErrorMessagesValidationAction(
    			instructionsLabel,
    			this.instructionsLabelText,
    			this.tutorialURL,
    			this.tutorialDisplayURL);

		// Create the options for the Source and Target selection fields.

		List<String> allOptions =
			ArrayListUtilities.copyAndSort(databaseDescriptor.getLeafTableNames());
		Map<String, String> allOptionsByLabels;

		if (this.includeCoreTableInLeafSelectors) {
			allOptions = ArrayListUtilities.unionCollectionsAsList(
				Arrays.asList(coreEntityHumanReadableName), allOptions, null);
			allOptionsByLabels = databaseDescriptor.createTableNameOptionsWithCore();
		} else {
			allOptionsByLabels = databaseDescriptor.createTableNameOptionsWithoutCore();
		}

		// Create and setup the Source and Target selection fields.

		SWTModelField<String, Combo, DropDownDataSynchronizer<String>> sourceLeafField =
			createLeafSelectionField(
				this.sourceLeafFieldLabel,
				HEADER_GROUP_NAME,
				this.sourceLeafFieldName,
				0,
				allOptions,
				allOptionsByLabels,
				databaseDescriptor,
				model,
				headerGroup,
				this.allValidators,
				Sets.newHashSet(this.leafSelectorFieldValidator),
				displayErrorMessagesValidationAction);
		SWTModelField<String, Combo, DropDownDataSynchronizer<String>> targetLeafField =
			createLeafSelectionField(
				this.targetLeafFieldLabel,
				HEADER_GROUP_NAME,
				this.targetLeafFieldName,
				1,
				allOptions,
				allOptionsByLabels,
				databaseDescriptor,
				model,
				headerGroup,
				this.allValidators,
				Sets.newHashSet(this.leafSelectorFieldValidator),
				displayErrorMessagesValidationAction);

		/*
		 * Make it so only one of the Source and Target selection fields can be the core
		 *  entity option.
		 */

		// Create the widget that allows users to specify node and edge aggregate fields.

		Map<String, String> aggregateOptions = createAggregateOptions(databaseDescriptor);

		AttributeListWidget nodeAggregatesWidget =
			createAggregateWidget(new AttributeWidgetProperties(
				model,
				ATTRIBUTE_FUNCTION_GROUP1_NAME,
				CORE_ENTITY_COLUMN_GROUP1_NAME,
				aggregateOptions.keySet(),
				aggregateOptions,
				ATTRIBUTE_NAME_GROUP1_NAME,
				Sets.newHashSet(this.aggregateFunctionValidator1),
				null,//TODO: column validators
				Sets.newHashSet(this.attributeNameValidator1),
				this.allValidators,
				this.disableFinishedButtonAction,
				displayErrorMessagesValidationAction,
				this.allowCountDistinctFor1),
			NODE_TYPE,
			nodeAggregatesGroup);
		AttributeListWidget edgeAggregatesWidget =
			createAggregateWidget(new AttributeWidgetProperties(
				model,
				ATTRIBUTE_FUNCTION_GROUP2_NAME,
				CORE_ENTITY_COLUMN_GROUP2_NAME,
				aggregateOptions.keySet(),
				aggregateOptions,
				ATTRIBUTE_NAME_GROUP2_NAME,
				Sets.newHashSet(this.aggregateFunctionValidator2),
				null,//TODO: column validators
				Sets.newHashSet(this.attributeNameValidator2),
				this.allValidators,
				this.disableFinishedButtonAction,
				displayErrorMessagesValidationAction,
				this.allowCountDistinctFor2),
			EDGE_TYPE,
			edgeAggregatesGroup);

		// Create the finished button so the user can actually execute the resulting queries.

		final ObjectContainer<Boolean> userFinished = new ObjectContainer<Boolean>(false);
		this.finishedButton = createFinishedButton(footerGroup, 1, userFinished);

		/* Fill the aggregate widgets with some aggregate fields by default (for the user's ease).
		 * (This has to be done after the finished button is created because the components we're
		 * about to create validate upon creation, and to validate the finished button must exist.
		 */

		try {
			for (int ii = 0; ii < this.defaultAggregateWidgetCount; ii++) {
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

		sourceLeafField.validate();
		targetLeafField.validate();

		// Run the GUI and return the model with the data that the user entered.

		runGUI(display, shell, windowHeight);

		if ((userFinished.object == false) && (exceptionThrown.object != null)) {
    		throw new GUICanceledException(
    			exceptionThrown.object.getMessage(), exceptionThrown.object);
    	}

		return model;
	}

	private SWTModelField<
			String, Combo, DropDownDataSynchronizer<String>> createLeafSelectionField(
				String labelText,
				String groupName,
				String fieldName,
				int selectedIndex,
				Collection<String> allOptionLabels,
				Map<String, String> allOptionValuesByLabels,
				StarDatabaseDescriptor databaseDescriptor,
				SWTModel model,
				Composite parent,
				Collection<FieldValidator<String>> allValidators,
				Collection<FieldValidator<String>> validators,
				DisplayErrorMessagesValidationAction displayErrorMessagesValidationAction)
				throws UniqueNameException {
		Label label = new Label(parent, SWT.READ_ONLY | SWT.WRAP);
		label.setLayoutData(createLeafSelectionFieldLabelLayoutData());
		label.setText(labelText);

		SWTModelField<String, Combo, DropDownDataSynchronizer<String>> leafField =
			model.addDropDown(
				fieldName,
				"",
				groupName,
				selectedIndex,
				allOptionLabels,
				allOptionValuesByLabels,
				parent,
				SWT.BORDER | SWT.READ_ONLY);
		leafField.getWidget().setLayoutData(createLeafSelectionFieldLayoutData());
		leafField.addValidators(validators);
		leafField.addOtherValidators(allValidators);
		leafField.addValidationAction(this.disableFinishedButtonAction);
		leafField.addValidationAction(displayErrorMessagesValidationAction);

		return leafField;
	}

	private static GridData createLeafSelectionFieldLabelLayoutData() {
		GridData layoutData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		layoutData.widthHint = LEAF_SELECTOR_LABEL_WIDTH;

		return layoutData;
	}

	private static GridData createLeafSelectionFieldLayoutData() {
		GridData layoutData = new GridData(SWT.LEFT, SWT.CENTER, false, true);
		layoutData.widthHint = LEAF_SELECTOR_WIDTH;

		return layoutData;
	}
}