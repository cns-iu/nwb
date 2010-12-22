package edu.iu.sci2.database.star.common;

import java.util.Collections;
import java.util.Map;

import edu.iu.sci2.database.star.common.parameter.ColumnDescriptor;

public class StarDatabaseMetadata {
	private String coreEntityHumanReadableName;
	private String coreEntityTableName;
	private Map<String, ColumnDescriptor> columnDescriptorsByHumanReadableName;
	private Map<String, ColumnDescriptor> columnDescriptorsByDatabaseName;

	public StarDatabaseMetadata(
			String coreEntityHumanReadableName,
			String coreEntityTableName,
			Map<String, ColumnDescriptor> columnDescriptorsByHumanReadableName,
			Map<String, ColumnDescriptor> columnDescriptorsByDatabaseName) {
		this.coreEntityHumanReadableName = coreEntityHumanReadableName;
		this.coreEntityTableName = coreEntityTableName;
		this.columnDescriptorsByHumanReadableName =
			Collections.unmodifiableMap(columnDescriptorsByHumanReadableName);
		this.columnDescriptorsByDatabaseName =
			Collections.unmodifiableMap(columnDescriptorsByDatabaseName);
	}

	public String getCoreEntityHumanReadableName() {
		return this.coreEntityHumanReadableName;
	}

	public String getCoreEntityTableName() {
		return this.coreEntityTableName;
	}

	public Map<String, ColumnDescriptor> getColumnDescriptorsByHumanReadableName() {
		return this.columnDescriptorsByHumanReadableName;
	}

	public Map<String, ColumnDescriptor> getColumnDescriptorsByDatabaseName() {
		return this.columnDescriptorsByDatabaseName;
	}
}