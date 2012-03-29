package edu.iu.sci2.database.star.gui;

import java.util.Collection;

import edu.iu.sci2.database.star.common.parameter.ColumnDescriptor;

public class ColumnsDataForLoader {
	private String coreEntityName;
	private Collection<ColumnDescriptor> columnDescriptors;

	public ColumnsDataForLoader(
			String coreEntityName, Collection<ColumnDescriptor> columnDescriptors) {
		this.coreEntityName = coreEntityName;
		this.columnDescriptors = columnDescriptors;
	}

	public String getCoreEntityName() {
		return this.coreEntityName;
	}

	public Collection<ColumnDescriptor> getColumnDescriptors() {
		return this.columnDescriptors;
	}
}