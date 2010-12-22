package edu.iu.sci2.database.star.gui.builder;

import java.util.ArrayList;
import java.util.Collection;

import org.cishell.utility.swt.GUIBuilderUtilities;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import edu.iu.sci2.database.star.common.parameter.ColumnDescriptor;

public class ColumnListWidget extends ScrolledComposite {
	public static final int COLUMN_AREA_LAYOUT_VERTICAL_SPACING = 1;
	public static final int VERTICAL_SCROLL_INCREMENT = 250;

	private Composite columnArea;
	private Collection<ColumnWidget> columns;

	public ColumnListWidget(
			Composite parent,
			Collection<ColumnDescriptor> columnDescriptors,
			String[] columnTypeOptions) {
		super(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		setLayout(createLayout());
		setExpandHorizontal(true);
		setExpandVertical(true);
		setAlwaysShowScrollBars(false);
		this.columnArea = createColumnArea(this);
		this.columns = createColumns(this, columnDescriptors, columnTypeOptions);

		setContent(this.columnArea);
		fixSize();
		getVerticalBar().setPageIncrement(VERTICAL_SCROLL_INCREMENT);
	}

	public Composite getColumnArea() {
		return this.columnArea;
	}

	public Collection<ColumnWidget> getColumnWidgets() {
		return this.columns;
	}

	public void fixSize() {
		setMinSize(this.columnArea.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}

	private static GridLayout createLayout() {
		GridLayout layout = new GridLayout(1, true);
		GUIBuilderUtilities.clearMargins(layout);
		GUIBuilderUtilities.clearSpacing(layout);

		return layout;
	}

	private static Composite createColumnArea(Composite parent) {
		Composite columnArea = new Composite(parent, SWT.BORDER);
		columnArea.setLayoutData(createColumnAreaLayoutData());
		columnArea.setLayout(createColumnAreaLayout());

		return columnArea;
	}

	private static Collection<ColumnWidget> createColumns(
			ColumnListWidget columnList,
			Collection<ColumnDescriptor> columnDescriptors,
			String[] columnTypeOptions) {
		Collection<ColumnWidget> columns = new ArrayList<ColumnWidget>();

		for (ColumnDescriptor columnDescriptor : columnDescriptors) {
			ColumnWidget column = new ColumnWidget(
				columnList, columnDescriptor, columnTypeOptions);
			column.setLayoutData(createColumnLayoutData());
			columns.add(column);
		}

		return columns;
	}

	private static GridData createColumnAreaLayoutData() {
		GridData layoutData = new GridData(SWT.LEFT, SWT.FILL, false, true);

		return layoutData;
	}

	private static GridLayout createColumnAreaLayout() {
		GridLayout layout = new GridLayout(1, false);
		GUIBuilderUtilities.clearMargins(layout);
		GUIBuilderUtilities.clearSpacing(layout);

		return layout;
	}

	private static GridData createColumnLayoutData() {
		GridData layoutData = new GridData(SWT.FILL, SWT.TOP, true, false);

		return layoutData;
	}
}