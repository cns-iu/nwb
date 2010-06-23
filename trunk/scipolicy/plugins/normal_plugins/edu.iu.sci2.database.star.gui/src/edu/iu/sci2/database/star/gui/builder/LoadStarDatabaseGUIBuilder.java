package edu.iu.sci2.database.star.gui.builder;

import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import edu.iu.sci2.database.star.gui.ColumnDescriptor;
import edu.iu.sci2.database.star.gui.StarDatabaseGUIAlgorithm;

public class LoadStarDatabaseGUIBuilder {
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

	public static void createAndDispatchGUI(
			String windowTitle,
			int windowWidth,
			int windowHeight,
			Collection<ColumnDescriptor> columnDescriptors) {
		Display display = createDisplay();
    	Shell shell = createShell(display, windowTitle, windowWidth, windowHeight);
    	CoreEntityNameWidget coreEntityName = createCoreEntityNameWidget(shell);
    	ColumnListWidget columnList = createColumnListWidget(shell, columnDescriptors);
//    	Column testColumn1 = new Column(shell, "Test Column 1", COLUMN_TYPE_OPTIONS);

    	dispatch(display, shell);
	}

	private static Display createDisplay() {
		return new Display();
	}

	private static void dispatch(Display display, Shell shell) {
		shell.pack();
    	shell.open();

    	while (!shell.isDisposed()) {
    		if (!display.readAndDispatch()) {
    			display.sleep();
    		}
    	}

    	display.dispose();
	}

	private static Shell createShell(
			Display display, String windowTitle, int windowWidth, int windowHeight) {
		Shell shell = new Shell(display);
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
		ColumnListWidget columnList = new ColumnListWidget(parent, columnDescriptors);
    	columnList.setLayoutData(createColumnListLayoutData());

    	return columnList;
	}

	private static RowLayout createShellLayout() {
		RowLayout layout = new RowLayout(SWT.VERTICAL);
		layout.fill = true;

		return layout;
	}

	private static RowData createCoreEntityNameLayoutData() {
		RowData layoutData = new RowData();

		return layoutData;
	}

	private static RowData createColumnListLayoutData() {
		RowData layoutData = new RowData();

		return layoutData;
	}

	public static void main(String[] args) {
//		Collection<ColumnDescriptor> columnDescriptors = 
//		createAndDispatchGUI(
//			StarDatabaseGUIAlgorithm.WINDOW_TITLE,
//			StarDatabaseGUIAlgorithm.WINDOW_WIDTH,
//			StarDatabaseGUIAlgorithm.WINDOW_HEIGHT);
	}
}