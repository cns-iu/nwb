package edu.iu.sci2.database.star.gui.builder;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import edu.iu.sci2.database.star.gui.ColumnDescriptor;

public class ColumnListWidget extends ScrolledComposite {
	private Composite columnArea;
	private Collection<Column> columns;

	public ColumnListWidget(Composite parent, Collection<ColumnDescriptor> columnDescriptors) {
		super(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		setLayout(createLayout());
		setExpandHorizontal(true);
		setExpandVertical(true);
		setAlwaysShowScrollBars(false);
		this.columnArea = createColumnArea(this);
		this.columns = createColumns(this.columnArea, columnDescriptors);
	}

	public Composite getColumnArea() {
		return this.columnArea;
	}

	private static GridLayout createLayout() {
		GridLayout layout = new GridLayout(1, true);

		return layout;
	}

	private static Composite createColumnArea(Composite parent) {
		Composite columnArea = new Composite(parent, SWT.NONE);
		columnArea.setLayout(createColumnAreaLayout());
		columnArea.setLayoutData(createColumnAreaLayoutData());

		return columnArea;
	}

	private static Collection<Column> createColumns(
			Composite parent, Collection<ColumnDescriptor> columnDescriptors) {
		Collection<Column> columns = new ArrayList<Column>();

		for (ColumnDescriptor columnDescriptor : columnDescriptors) {
			
		}

		return columns;
	}

	private static GridLayout createColumnAreaLayout() {
		GridLayout layout = new GridLayout(2, true);

		return layout;
	}

	private static GridData createColumnAreaLayoutData() {
		GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, true);

		return layoutData;
	}
}