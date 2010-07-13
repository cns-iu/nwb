package edu.iu.sci2.database.star.gui.builder;

import java.util.ArrayList;
import java.util.Collection;

import org.cishell.utilities.swt.GUIBuilderUtilities;
import org.cishell.utilities.swt.SWTUtilities;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import edu.iu.sci2.database.star.gui.ColumnDescriptor;
import edu.iu.sci2.database.star.gui.ColumnsDataForLoader;
import edu.iu.sci2.database.star.gui.StarDatabaseGUIAlgorithm;
import edu.iu.sci2.database.star.load.parameter.ParameterDescriptors;

public class LoadStarDatabaseGUIBuilder {
	public static final boolean GRAY_OUT_NON_CORE_COLUMN_CONTROLS = true;

	public static final int INSTRUCTIONS_WIDTH = 300;
	public static final int INSTRUCTIONS_HEIGHT = 150;

	public static final int FINISHED_BUTTON_HEIGHT = 50;

	public static final String DEFAULT_CORE_ENTITY_NAME = "CORE";

	public static final String TUTORIAL_DISPLAY_URL = "Sci2 Tutorial";
	public static final String TUTORIAL_URL =
		"https://nwb.slis.indiana.edu/community/?n=Sci2Algorithm.LoadStarDatabase";
	public static final String INSTRUCTIONS_LABEL_TEXT =
		"The Star Database Loader loads a csv file into a database with a \"star\" schema. " +
		"The star schema has one \"core\" or central table for the primary entity of your csv " +
		"(e.g. publications, grants, etc...) and zero or more \"leaf\" tables with entities " +
		"related to the primary entity. For example if your central table contained " +
		"publications, your leaf tables might contain authors, journals, and institutions. " +
		"For each column, please specify whether it belongs to the primary entity or should be " +
		"used to form a leaf table entity.\n\n" +
		"For more information see the Sci2 tutorial at: ";

	public static final String FINISHED_BUTTON_TEXT = "I'm Finished!";

	public static ColumnsDataForLoader gatherUserInput(
			String windowTitle,
			int windowWidth,
			int windowHeight,
			Collection<ColumnDescriptor> columnDescriptors) {
		Display display = GUIBuilderUtilities.createDisplay();
    	final Shell shell = GUIBuilderUtilities.createShell(
    		display, windowTitle, windowWidth, windowHeight, 1, true);

    	@SuppressWarnings("unused")
    	StyledText instructionsLabel = createInstructionsLabel(shell);

    	final CoreEntityNameWidget coreEntityNameWidget = createCoreEntityNameWidget(shell);

    	final ColumnListWidget columnListWidget = createColumnListWidget(shell, columnDescriptors);

    	Button finishedButton = createFinishedButton(shell);
    	shell.setDefaultButton(finishedButton);
    	final ColumnsDataForLoader[] columnsDataForLoader = new ColumnsDataForLoader[1];
    	finishedButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				columnsDataForLoader[0] =
					gatherColumnsDataForLoader(coreEntityNameWidget, columnListWidget);
				shell.close();
			}
		});

    	runGUI(display, shell, columnListWidget, windowHeight);

    	return columnsDataForLoader[0];
	}

	private static void runGUI(
			Display display,
			Shell shell,
			ColumnListWidget columnListWidget,
			int windowHeight) {
		GUIBuilderUtilities.openShell(shell, windowHeight, true);

    	if (!GRAY_OUT_NON_CORE_COLUMN_CONTROLS) {
    		hackHideSomeStuff(columnListWidget);
    	}

    	GUIBuilderUtilities.swtLoop(display, shell);
	}

	private static StyledText createInstructionsLabel(Composite parent) {
		StyledText instructionsLabel =
			new StyledText(parent, SWT.LEFT | SWT.READ_ONLY | SWT.WRAP);
		instructionsLabel.setBackground(
			parent.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		instructionsLabel.setLayoutData(createInstructionsLabelLayoutData());
		instructionsLabel.getCaret().setVisible(false);

//		URLClickedListener urlClickedListener = new URLClickedListener(instructionsLabel);
//		URLMouseCursorListener urlCursorListener =
//			new URLMouseCursorListener(parent, instructionsLabel);
//		instructionsLabel.addMouseListener(urlClickedListener);
//		instructionsLabel.addMouseMoveListener(urlCursorListener);
//
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
//        urlClickedListener.addURL(
//        	instructionsLabel.getText().length(), TUTORIAL_URL, TUTORIAL_DISPLAY_URL);
//        urlCursorListener.addURL(
//        	instructionsLabel.getText().length(), TUTORIAL_URL, TUTORIAL_DISPLAY_URL);
//        SWTUtilities.styledPrint(
//        	instructionsLabel,
//        	TUTORIAL_DISPLAY_URL,
//        	parent.getDisplay().getSystemColor(SWT.COLOR_BLUE),
//        	SWT.BOLD);

		return instructionsLabel;
	}

	private static CoreEntityNameWidget createCoreEntityNameWidget(Composite parent) {
		CoreEntityNameWidget coreEntityName =
    		new CoreEntityNameWidget(parent, DEFAULT_CORE_ENTITY_NAME);
    	coreEntityName.setLayoutData(createCoreEntityNameLayoutData());

    	return coreEntityName;
	}

	private static ColumnListWidget createColumnListWidget(
			Composite parent, Collection<ColumnDescriptor> columnDescriptors) {
		ColumnListWidget columnList =
			new ColumnListWidget(parent, columnDescriptors, ParameterDescriptors.Type.OPTIONS);
    	columnList.setLayoutData(createColumnListLayoutData());

    	return columnList;
	}

	private static Button createFinishedButton(final Composite parent) {
		Button finishedButton = new Button(parent, SWT.BORDER | SWT.PUSH);
		finishedButton.setLayoutData(createFinishedButtonLayoutData());
		finishedButton.setText(FINISHED_BUTTON_TEXT);

		return finishedButton;
	}

	private static GridData createInstructionsLabelLayoutData() {
		GridData layoutData = new GridData(SWT.FILL, SWT.TOP, true, false);
		layoutData.widthHint = INSTRUCTIONS_WIDTH;
		layoutData.heightHint = INSTRUCTIONS_HEIGHT;

		return layoutData;
	}

	private static GridData createCoreEntityNameLayoutData() {
		GridData layoutData = new GridData(SWT.FILL, SWT.TOP, true, false);

		return layoutData;
	}

	private static GridData createColumnListLayoutData() {
		GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, true);

		return layoutData;
	}

	private static GridData createFinishedButtonLayoutData() {
		GridData layoutData = new GridData(SWT.FILL, SWT.BOTTOM, true, false);
		layoutData.heightHint = FINISHED_BUTTON_HEIGHT;

		return layoutData;
	}

	private static void hackHideSomeStuff(ColumnListWidget columnList) {
		for (ColumnWidget columnWidget : columnList.getColumnWidgets()) {
    		columnWidget.getProperties().getNonCoreColumnProperties().setExpanded(false);
    	}
	}

	/**
	 * This method extracts user-inputted data from the GUI and transforms it into ColumnDescriptor
	 *  objects that can later be used to invoke the Star Database Loader algorithm.
	 */
	private static ColumnsDataForLoader gatherColumnsDataForLoader(
			CoreEntityNameWidget coreEntityNameWidget, ColumnListWidget columnListWidget) {
		String coreEntityName = coreEntityNameWidget.getCoreEntityName();
		Collection<ColumnDescriptor> columnDescriptors = new ArrayList<ColumnDescriptor>();

		for (ColumnWidget columnWidget : columnListWidget.getColumnWidgets()) {
			columnDescriptors.add(gatherColumnDescriptorFromGUI(columnWidget));
		}

		return new ColumnsDataForLoader(coreEntityName, columnDescriptors);
	}

	private static ColumnDescriptor gatherColumnDescriptorFromGUI(ColumnWidget columnWidget) {
		ColumnHeaderWidget headerWidget = columnWidget.getHeader();
		ColumnPropertiesWidget propertiesWidget = columnWidget.getProperties();
		String name = headerWidget.getColumnName();
		String type = headerWidget.getType();
		boolean isCoreColumn = propertiesWidget.getIsCoreColumn().isCoreColumn();

		if (isCoreColumn) {
			return new ColumnDescriptor(name, type, true, false, false, "");
		} else {
			NonCoreColumnPropertiesWidget isNotCoreColumnPropertiesWidget =
				propertiesWidget.getNonCoreColumnProperties();
			boolean mergeIdenticalValues = isNotCoreColumnPropertiesWidget.
				getMergeIdenticalValuesInputField().isSelected();
			boolean isMultiValued =
				isNotCoreColumnPropertiesWidget.getMultiValuedFieldInputField().isSelected();

			if (isMultiValued) {
				String separator =
					isNotCoreColumnPropertiesWidget.getSeparatorInputField().getText();
				return new ColumnDescriptor(
					name, type, false, mergeIdenticalValues, true, separator);
			} else {
				return new ColumnDescriptor(name, type, false, mergeIdenticalValues, true, "");
			}
		}
	}

	public static void main(String[] args) {
		Collection<ColumnDescriptor> columnDescriptors = new ArrayList<ColumnDescriptor>();
		columnDescriptors.add(new ColumnDescriptor("Test 1"));
		columnDescriptors.add(new ColumnDescriptor("Test 2"));
		columnDescriptors.add(new ColumnDescriptor("Test 3"));
		columnDescriptors.add(new ColumnDescriptor("Test 4"));
		columnDescriptors.add(new ColumnDescriptor("Test 5"));
		columnDescriptors.add(new ColumnDescriptor("Test 6"));
		columnDescriptors.add(new ColumnDescriptor("Test 7"));
		columnDescriptors.add(new ColumnDescriptor("Test 8"));
		columnDescriptors.add(new ColumnDescriptor("Test 9"));

		gatherUserInput(
			StarDatabaseGUIAlgorithm.WINDOW_TITLE,
			StarDatabaseGUIAlgorithm.WINDOW_WIDTH,
			StarDatabaseGUIAlgorithm.WINDOW_HEIGHT,
			columnDescriptors);
	}
}