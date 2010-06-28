package edu.iu.sci2.database.star.gui.builder;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import edu.iu.sci2.database.star.gui.ColumnDescriptor;
import edu.iu.sci2.database.star.gui.ColumnsDataForLoader;
import edu.iu.sci2.database.star.gui.StarDatabaseGUIAlgorithm;

public class LoadStarDatabaseGUIBuilder {
	public static final int NEW_NAME_WIDGET_HEIGHT = 100;

	public static final String DEFAULT_CORE_ENTITY_NAME = "CORE";

	public static final String COLUMN_OPTION_CHOOSE_TYPE = "Choose Type";
	public static final String COLUMN_OPTION_STRING = "String";
	public static final String COLUMN_OPTION_INTEGER = "Integer";
	public static final String COLUMN_OPTION_DOUBLE = "Double";
	public static final String[] COLUMN_TYPE_OPTIONS = new String[] {
		COLUMN_OPTION_CHOOSE_TYPE,
		COLUMN_OPTION_STRING,
		COLUMN_OPTION_INTEGER,
		COLUMN_OPTION_DOUBLE
	};

	// TODO: Needs new name.
	public static ColumnsDataForLoader getStuffFromUser(
			String windowTitle,
			int windowWidth,
			int windowHeight,
			Collection<ColumnDescriptor> columnDescriptors) {
		Display display = createDisplay();
    	Shell shell = createShell(display, windowTitle, windowWidth, windowHeight);
    	CoreEntityNameWidget coreEntityNameWidget = createCoreEntityNameWidget(shell);
    	ColumnListWidget columnListWidget = createColumnListWidget(shell, columnDescriptors);
    	BottomWidgetThingy newNameAreaWidget = createNewNameArea(shell);
    	shell.setMinimumSize(shell.computeSize(SWT.DEFAULT, SWT.DEFAULT).x, windowHeight);

//    	dispatch(display, shell, windowHeight);
    	// TODO: Hack
    	dispatch(display, shell, columnListWidget, windowHeight);

    	return gatherColumnsDataForLoader(coreEntityNameWidget, columnListWidget);
	}

	private static Display createDisplay() {
		return new Display();
	}

//	private static void dispatch(Display display, Shell shell, int windowHeight) {
	// TODO: Hack
	private static void dispatch(
			Display display,
			Shell shell,
			ColumnListWidget columnListWidget,
			int windowHeight) {
		shell.pack();
    	shell.open();
    	shell.setSize(shell.getSize().x, windowHeight);
    	hackHideSomeStuff(columnListWidget);

    	while (!shell.isDisposed()) {
    		if (!display.readAndDispatch()) {
    			display.sleep();
    		}
    	}

    	display.dispose();
	}

	private static Shell createShell(
			Display display, String windowTitle, int windowWidth, int windowHeight) {
		Shell shell = new Shell(display, SWT.CLOSE | SWT.MIN | SWT.ON_TOP | SWT.TITLE);
		shell.setText(windowTitle);
    	shell.setSize(windowWidth, windowHeight);
    	shell.setLayout(createShellLayout());

    	return shell;
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
			new ColumnListWidget(parent, columnDescriptors, COLUMN_TYPE_OPTIONS);
    	columnList.setLayoutData(createColumnListLayoutData());

    	return columnList;
	}

	private static BottomWidgetThingy createNewNameArea(Composite parent) {
		BottomWidgetThingy newNameArea = new BottomWidgetThingy(parent);
		newNameArea.setLayoutData(createNewNameAreaLayoutData());

		return newNameArea;
	}

	private static GridLayout createShellLayout() {
		GridLayout layout = new GridLayout(1, true);
		Utilities.clearMargins(layout);
		Utilities.clearSpacing(layout);

		return layout;
	}

	private static GridData createCoreEntityNameLayoutData() {
		GridData layoutData = new GridData(SWT.FILL, SWT.TOP, true, false);

		return layoutData;
	}

	private static GridData createColumnListLayoutData() {
		GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, true);

		return layoutData;
	}

	private static GridData createNewNameAreaLayoutData() {
		GridData layoutData = new GridData(SWT.FILL, SWT.BOTTOM, true, false);
		layoutData.heightHint = NEW_NAME_WIDGET_HEIGHT;

		return layoutData;
	}

	private static void hackHideSomeStuff(ColumnListWidget columnList) {
		for (ColumnWidget columnWidget : columnList.getColumnWidgets()) {
    		columnWidget.getProperties().getIsNotCoreColumnProperties().setExpanded(false);
    	}
	}

	private static ColumnsDataForLoader gatherColumnsDataForLoader(
			CoreEntityNameWidget coreEntityNameWidget, ColumnListWidget columnListWidget) {
		String coreEntityName = coreEntityNameWidget.getCoreEntityName();
		Collection<ColumnDescriptor> columnDescriptors = new ArrayList<ColumnDescriptor>();

		for (ColumnWidget columnWidget : columnListWidget.getColumnWidgets()) {
			ColumnHeaderWidget headerWidget = columnWidget.getHeader();
			ColumnPropertiesWidget propertiesWidget = columnWidget.getProperties();
			String name = headerWidget.getColumnName();
			String type = headerWidget.getType();
			boolean isCoreColumn = propertiesWidget.getIsCoreColumn().isCoreColumn();

			if (isCoreColumn) {
				columnDescriptors.add(new ColumnDescriptor(name, type, true, false, ""));
			} else {
				IsNotCoreColumnPropertiesWidget isNotCoreColumnPropertiesWidget =
					propertiesWidget.getIsNotCoreColumnProperties();
				boolean isMultiValued =
					isNotCoreColumnPropertiesWidget.getSingleValuesInputField().isSelected();

				if (isMultiValued) {
					String separator =
						isNotCoreColumnPropertiesWidget.getSeparatorInputField().getText();
					columnDescriptors.add(
						new ColumnDescriptor(name, type, false, true, separator));
				} else {
					columnDescriptors.add(new ColumnDescriptor(name, type, false, true, ""));
				}
			}	
		}

		return new ColumnsDataForLoader(coreEntityName, columnDescriptors);
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

		getStuffFromUser(
			StarDatabaseGUIAlgorithm.WINDOW_TITLE,
			StarDatabaseGUIAlgorithm.WINDOW_WIDTH,
			StarDatabaseGUIAlgorithm.WINDOW_HEIGHT,
			columnDescriptors);
	}
}