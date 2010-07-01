package edu.iu.sci2.database.star.gui;

/**
 * ColumnDescriptor describes how the Star Database Loader will process the CSV column of the
 *  same name.
 * For a given CSV, a combination of all ColumnDescriptors describes the entire database schema to
 *  the Star Database Loader.
 * A ColumnDescriptor is merely data about a column.  It is not an actual column, since the
 *  column's data cannot be accessed through it.  It is also not merely metadata in the
 */
public class ColumnDescriptor {
	private String name;
	private String type;
	private boolean isCoreColumn;
	private boolean mergeIdenticalValues;
	private boolean isMultiValued;
	private String separator;

	//TODO: Describe class and how it might be used in the future (defaults are valid).
	
	public ColumnDescriptor(String name) {
		this(name, "", true, false, false, "");
	}

	public ColumnDescriptor(
			String name,
			String type,
			boolean isCoreColumn,
			boolean mergeIdenticalValues,
			boolean isMultiValued,
			String separator) {
		this.name = name;
		this.type = type;
		this.isCoreColumn = isCoreColumn;
		this.isMultiValued = isMultiValued;
		this.mergeIdenticalValues = mergeIdenticalValues;
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

	public boolean mergeIdenticalValues() {
		return this.mergeIdenticalValues;
	}

	public boolean isMultiValued() {
		return this.isMultiValued;
	}

	public String getSeparator() {
		return this.separator;
	}
}