package edu.iu.sci2.database.star.gui;

public class ColumnDescriptor {
	private String name;
	private String type;
	private boolean isCoreColumn;
	private boolean isMultiValued;
	private String separator;

	public ColumnDescriptor(String name) {
		this(name, "", true, false, "");
	}

	public ColumnDescriptor(
			String name,
			String type,
			boolean isCoreColumn,
			boolean isMultiValued,
			String separator) {
		this.name = name;
		this.type = type;
		this.isCoreColumn = isCoreColumn;
		this.isMultiValued = isMultiValued;
		this.separator = separator;
	}

	public String getName() {
		return this.name;
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