package edu.iu.sci2.database.star.common;

import java.util.Map;

import edu.iu.sci2.database.star.common.parameter.ColumnDescriptor;

public class StarDatabaseMetadata {
	private String coreEntityHumanReadableName;
	private String coreEntityTableName;
	private Map<String, ColumnDescriptor> columnDescriptors;

	public StarDatabaseMetadata(
			String coreEntityHumanReadableName,
			String coreEntityTableName,
			Map<String, ColumnDescriptor> columnDescriptors) {
		this.coreEntityHumanReadableName = coreEntityHumanReadableName;
		this.coreEntityTableName = coreEntityTableName;
		this.columnDescriptors = columnDescriptors;
	}

	public String getCoreEntityHumanReadableName() {
		return this.coreEntityHumanReadableName;
	}

	public String getCoreEntityTableName() {
		return this.coreEntityTableName;
	}

	public Map<String, ColumnDescriptor> getColumnDescriptors() {
		return this.columnDescriptors;
	}
}