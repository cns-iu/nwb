package edu.iu.sci2.database.star.gui;

public class ColumnDescriptor {
	private String type;
	private boolean isCoreColumn;
	private boolean isMultiValued;
	private String separator;

	public ColumnDescriptor(
			String type, boolean isCoreColumn, boolean isMultiValued, String separator) {
		this.type = type;
		this.isCoreColumn = isCoreColumn;
		this.isMultiValued = isMultiValued;
		this.separator = separator;
	}

	public String getType() {
		return this.type;
	}

	public boolean isCoreColumn() {
		return this.isCoreColumn;
	}

	public boolean isMultiValued() {
		return this.isMultiValued;
	}

	public String getSeparator() {
		return this.separator;
	}
}