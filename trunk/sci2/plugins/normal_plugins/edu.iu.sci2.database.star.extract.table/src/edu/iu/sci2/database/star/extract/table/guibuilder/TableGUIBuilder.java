package edu.iu.sci2.database.star.extract.table.guibuilder;

import java.util.Collection;
import java.util.Map;

import org.cishell.utility.datastructure.ObjectContainer;
import org.cishell.utility.datastructure.datamodel.DataModel;
import org.cishell.utility.datastructure.datamodel.exception.UniqueNameException;
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

import com.google.common.collect.Sets;

import edu.iu.sci2.database.star.extract.common.StarDatabaseDescriptor;
import edu.iu.sci2.database.star.extract.common.guibuilder.DisplayErrorMessagesValidationAction;
import edu.iu.sci2.database.star.extract.common.guibuilder.GUIBuilder;
import edu.iu.sci2.database.star.extract.common.guibuilder.attribute.AttributeListWidget;
import edu.iu.sci2.database.star.extract.common.guibuilder.attribute.AttributeWidgetProperties;

public class TableGUIBuilder extends GUIBuilder {
	public static final String INSTRUCTIONS_LABEL_TEXT =
		"Choose the leaf entity that your extracted data table should pertain to.\n" +
		"Then, setup any attributes you want on your resulting data table.\n" +
		"For more information see the Sci2 tutorial at: ";
	public static final String TUTORIAL_URL =
		"https://nwb.slis.indiana.edu/community/?n=Sci2Algorithm.GenericCSVExtractTable";
	public static final String TUTORIAL_DISPLAY_URL = "Sci2 Tutorial";
	public static final int INSTRUCTIONS_LABEL_HEIGHT = 55;

	public static final String LEAF_FIELD_LABEL =
		"Choose the leaf entity that you want your extracted data table to pertain to: ";
	public static final String HEADER_GROUP_TEXT = "";

	public static final String LEAF_FIELD_NAME = "That Your Data Table Should Pertain To";

	public TableGUIBuilder(StarDatabaseDescriptor databaseDescriptor) {
		super(databaseDescriptor);
	}

	@Override
	public String attributeFieldValidator1BaseName() {
		return TABLE_ATTRIBUTE_FIELD_VALIDATOR_BASE_NAME;
	}

	@Override
	public String attributeFieldValidator2BaseName() {
		return "";
	}

	@SuppressWarnings("unchecked")
	@Override
	public DataModel createGUI(
			String windowTitle,
			int windowWidth,
			int windowHeight,
			StarDatabaseDescriptor databaseDescriptor)
			throws GUICanceledException, UniqueNameException {
		SWTModel model = new SWTModel(SWT.NONE);
		model.createGroup(ATTRIBUTE_FUNCTION_GROUP1_NAME);
		model.createGroup(CORE_ENTITY_COLUMN_GROUP1_NAME);
		model.createGroup(ATTRIBUTE_NAME_GROUP1_NAME);

		// Create the GUI shell, and set up its basic structure (header, aggregates).

		Display display = GUIBuilderUtilities.createDisplay();
		Shell shell = GUIBuilderUtilities.createShell(
			display, windowTitle, windowWidth, windowHeight, 1, false);
		Composite instructionsArea = createInstructionsArea(shell);
		Group headerGroup = createHeaderGroup(shell);
		Group aggregatesGroup = createAggregatesGroup(shell, TABLE_ATTRIBUTES_GROUP_TEXT);
		Group footerGroup = createFooterGroup(shell);

		// Create the instructions/error message label.

		StyledText instructionsLabel =
			createInstructionsLabel(instructionsArea, INSTRUCTIONS_LABEL_HEIGHT);
    	DisplayErrorMessagesValidationAction displayErrorMessagesValidationAction =
    		new DisplayErrorMessagesValidationAction(
    			instructionsLabel,
    			INSTRUCTIONS_LABEL_TEXT,
    			TUTORIAL_URL,
    			TUTORIAL_DISPLAY_URL);

    	// Create and setup the Leaf selection field.

    	createLeafSelectionField(headerGroup, databaseDescriptor, model);

    	// Create the widget that allows users to specify aggregate fields.

    	Map<String, String> aggregateOptions = createAggregateOptions(databaseDescriptor);

		AttributeListWidget tableAggregatesWidget =
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
				true),
			TABLE_TYPE,
			aggregatesGroup);

		// Create the finished button so the user can actually execute the resulting queries.

		final ObjectContainer<Boolean> userFinished = new ObjectContainer<Boolean>(false);
		this.finishedButton = createFinishedButton(footerGroup, 1, userFinished);

		/* Fill the aggregate widgets with some aggregate fields by default (for the user's ease).
		 * (This has to be done after the finished button is created because the components we're
		 * about to create validate upon creation, and to validate the finished button must exist.
		 */

		try {
			for (int ii = 0; ii < DEFAULT_AGGREGATE_WIDGET_COUNT; ii++) {
				tableAggregatesWidget.addComponent(SWT.NONE, null);
			}
		} catch (WidgetConstructionException e) {
			throw new RuntimeException(e.getMessage(), e); // TODO is this outdated?  still need it?
		}

		// Set the GUI up to be cancelable.

		final ObjectContainer<GUICanceledException> exceptionThrown =
			new ObjectContainer<GUICanceledException>();
		GUIBuilderUtilities.setCancelable(shell, exceptionThrown);

		// Run the GUI and return the model with the data that the user entered.

		runGUI(display, shell, windowHeight);

		if ((userFinished.object == false) && (exceptionThrown.object != null)) { // TODO ?
    		throw new GUICanceledException(
    			exceptionThrown.object.getMessage(), exceptionThrown.object);
    	}

    	return model;
	}

	@Override
	protected StyledText createInstructionsLabel(Composite parent, int height) {
		StyledText instructionsLabel = super.createInstructionsLabel(parent, height);

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

	private static SWTModelField<
			String, Combo, DropDownDataSynchronizer<String>> createLeafSelectionField(
				Composite parent, StarDatabaseDescriptor databaseDescriptor, SWTModel model)
				throws UniqueNameException {
		Label label = new Label(parent, SWT.READ_ONLY | SWT.WRAP);
		label.setLayoutData(createLeafSelectionFieldLabelLayoutData());
		label.setText(LEAF_FIELD_LABEL);

		Collection<String> columnNames = databaseDescriptor.getLeafTableNames();
		Map<String, String> columnOptions = databaseDescriptor.createTableNameOptionsWithoutCore();
		SWTModelField<String, Combo, DropDownDataSynchronizer<String>> leafField =
			model.addDropDown(
			LEAF_FIELD_NAME,
			"",
			HEADER_GROUP_NAME,
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
		layoutData.widthHint = LEAF_SELECTOR_LABEL_WIDTH;

		return layoutData;
	}

	private static GridData createLeafSelectionFieldLayoutData() {
		GridData layoutData = new GridData(SWT.FILL, SWT.CENTER, true, true);
		layoutData.widthHint = LEAF_SELECTOR_WIDTH;

		return layoutData;
	}
}