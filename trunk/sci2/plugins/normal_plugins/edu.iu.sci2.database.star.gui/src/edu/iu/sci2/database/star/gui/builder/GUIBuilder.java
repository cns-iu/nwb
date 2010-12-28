package edu.iu.sci2.database.star.gui.builder;

import java.util.ArrayList;
import java.util.Collection;

import org.cishell.utility.datastructure.ObjectContainer;
import org.cishell.utility.swt.GUIBuilderUtilities;
import org.cishell.utility.swt.GUICanceledException;
import org.cishell.utility.swt.SWTUtilities;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import edu.iu.cns.database.load.framework.DerbyFieldType;
import edu.iu.cns.database.load.framework.exception.InvalidDerbyFieldTypeException;
import edu.iu.sci2.database.star.common.StarDatabaseCSVDataValidationRules;
import edu.iu.sci2.database.star.common.parameter.ColumnDescriptor;
import edu.iu.sci2.database.star.common.parameter.ParameterDescriptors;
import edu.iu.sci2.database.star.gui.ColumnsDataForLoader;

public class GUIBuilder {
	public static final boolean GRAY_OUT_NON_CORE_COLUMN_CONTROLS = true;

	public static final int INSTRUCTIONS_WIDTH = 300;
	public static final int INSTRUCTIONS_HEIGHT = 150;

	public static final int FINISHED_BUTTON_HEIGHT = 50;

	public static final String DEFAULT_CORE_ENTITY_NAME = "CORE";

	public static final String TUTORIAL_DISPLAY_URL = "Sci2 Tutorial";
	public static final String TUTORIAL_URL =
		"https://nwb.cns.iu.edu/community/?n=Sci2Algorithm.LoadGenericCSVFileIntoDatabase";
	public static final String INSTRUCTIONS_LABEL_TEXT =
		"The Generic-CSV Database Loader loads a CSV file into a database with a \"star\" " +
		"schema. The star schema has one \"core\" or central table for the primary entity of " +
		"your csv (e.g. publications, grants, etc...) and zero or more \"leaf\" tables with " +
		"entities related to the primary entity. For example if your central table contained " +
		"publications, your leaf tables might contain authors, journals, and institutions. " +
		"For each column, please specify whether it belongs to the primary entity or should be " +
		"used to form a leaf table entity.\n\n" +
		"For more information see the Sci2 tutorial at: ";

	public static final String FINISHED_BUTTON_TEXT = "I'm Finished!";

	public static ColumnsDataForLoader gatherUserInput(
			String windowTitle,
			int windowWidth,
			int windowHeight,
			Collection<ColumnDescriptor> columnDescriptors) throws GUICanceledException {
		final ObjectContainer<GUICanceledException> exceptionThrown =
			new ObjectContainer<GUICanceledException>();
		final ObjectContainer<Boolean> userFinished = new ObjectContainer<Boolean>(false);

		Display display = GUIBuilderUtilities.createDisplay();
    	final Shell shell = GUIBuilderUtilities.createShell(
    		display, windowTitle, windowWidth, windowHeight, 1, true);

    	@SuppressWarnings("unused")
    	StyledText instructionsLabel = createInstructionsLabel(shell);

    	// TODO: Make this more official (less quick-and-dirty'd in).
    	Composite persistButtonArea = new Composite(shell, SWT.BORDER);
    	persistButtonArea.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
    	persistButtonArea.setLayout(new GridLayout(2, true));
    	Button saveButton = new Button(persistButtonArea, SWT.PUSH);
    	saveButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    	saveButton.setText("Save Column Attributes");
    	Button loadButton = new Button(persistButtonArea, SWT.PUSH);
    	loadButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    	loadButton.setText("Restore Column Attributes");

    	final CoreEntityNameWidget coreEntityNameWidget = createCoreEntityNameWidget(shell);

    	final ColumnListWidget columnListWidget = createColumnListWidget(shell, columnDescriptors);

    	Button finishedButton = createFinishedButton(shell);
    	shell.setDefaultButton(finishedButton);
    	final ColumnsDataForLoader[] columnsDataForLoader = new ColumnsDataForLoader[1];
    	finishedButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				try {
					columnsDataForLoader[0] =
						gatherColumnsDataForLoader(coreEntityNameWidget, columnListWidget);
					shell.close();
					userFinished.object = true;
				} catch (InvalidDerbyFieldTypeException e) {
					exceptionThrown.object = new GUICanceledException(e.getMessage(), e);
				}
			}
		});

    	
    	saveButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent event) {
				selected(event);
			}

			public void widgetSelected(SelectionEvent event) {
				selected(event);
			}

			private void selected(SelectionEvent event) {
				Persister.saveSession(shell, coreEntityNameWidget, columnListWidget);
			}
    	});
    	loadButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent event) {
				selected(event);
			}

			public void widgetSelected(SelectionEvent event) {
				selected(event);
			}

			private void selected(SelectionEvent event) {
				Persister.loadSession(shell, coreEntityNameWidget, columnListWidget);
			}
    	});

    	GUIBuilderUtilities.setCancelable(shell, exceptionThrown);
    	runGUI(display, shell, columnListWidget, windowHeight);

    	if ((userFinished.object == false) && (exceptionThrown.object != null)) {
    		throw new GUICanceledException(
    			exceptionThrown.object.getMessage(), exceptionThrown.object);
    	}

    	return columnsDataForLoader[0];
	}

	private static void runGUI(
			Display display, Shell shell, ColumnListWidget columnListWidget, int windowHeight) {
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
	public static ColumnsDataForLoader gatherColumnsDataForLoader(
			CoreEntityNameWidget coreEntityNameWidget, ColumnListWidget columnListWidget)
			throws InvalidDerbyFieldTypeException {
		String coreEntityName = coreEntityNameWidget.getCoreEntityName();
		Collection<ColumnDescriptor> columnDescriptors = new ArrayList<ColumnDescriptor>();
		int index = 0;

		for (ColumnWidget columnWidget : columnListWidget.getColumnWidgets()) {
			columnDescriptors.add(gatherColumnDescriptorFromGUI(index, columnWidget));
			index++;
		}

		return new ColumnsDataForLoader(coreEntityName, columnDescriptors);
	}

	private static ColumnDescriptor gatherColumnDescriptorFromGUI(
			int index, ColumnWidget columnWidget) throws InvalidDerbyFieldTypeException {
		ColumnHeaderWidget headerWidget = columnWidget.getHeader();
		ColumnPropertiesWidget propertiesWidget = columnWidget.getProperties();
		String name = headerWidget.getColumnName();
		String databaseName = StarDatabaseCSVDataValidationRules.normalizeName(name);
		// TODO: Handle this error better?
		DerbyFieldType type =
			DerbyFieldType.getFieldTypeByHumanReadableName(headerWidget.getType());
		boolean isCoreColumn = propertiesWidget.getIsCoreColumn().isCoreColumn();

		if (isCoreColumn) {
			return new ColumnDescriptor(index, name, databaseName, type, true, false, false, "");
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
					index,
					name,
					databaseName,
					type,
					false,
					mergeIdenticalValues,
					true,
					separator);
			} else {
				return new ColumnDescriptor(
					index, name, databaseName, type, false, mergeIdenticalValues, true, "");
			}
		}
	}
//
//	public static void main(String[] args) {
//		Collection<ColumnDescriptor> columnDescriptors = new ArrayList<ColumnDescriptor>();
//
//		for (int ii = 1; ii < 10; ii++) {
//			columnDescriptors.add(new ColumnDescriptor(ii, "Test " + ii));
//		}
//
//		gatherUserInput(
//			StarDatabaseGUIAlgorithm.WINDOW_TITLE,
//			StarDatabaseGUIAlgorithm.WINDOW_WIDTH,
//			StarDatabaseGUIAlgorithm.WINDOW_HEIGHT,
//			columnDescriptors);
//	}
}