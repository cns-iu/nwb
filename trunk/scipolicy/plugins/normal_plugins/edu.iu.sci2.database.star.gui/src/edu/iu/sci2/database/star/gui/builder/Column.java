package edu.iu.sci2.database.star.gui.builder;

import org.eclipse.swt.widgets.Composite;

public class Column {
	private String columnName;
	private ColumnHeader header;
	private ColumnProperties properties;

	public Column(Composite parent, String columnName, String[] columnTypeOptions) {
		this(parent, columnName, columnTypeOptions, 0);
	}

	public Column(
			Composite parent,
			String columnName,
			String[] columnTypeOptions,
			int defaultOptionIndex) {
		this.header = new ColumnHeader(parent, columnName, columnTypeOptions, defaultOptionIndex);
		this.properties = new ColumnProperties(parent);
	}

	public String getColumnName() {
		return this.columnName;
	}

	public ColumnHeader getHeader() {
		return this.header;
	}

	public ColumnProperties getProperties() {
		return this.properties;
	}
}