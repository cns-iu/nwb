package edu.iu.sci2.database.star.extract.common.aggregate;

import java.util.Map;

import edu.iu.sci2.database.star.common.parameter.ColumnDescriptor;


public class Aggregate {
	private String name;
	private AggregateFunction function;
	private ColumnDescriptor targetColumnDescriptor;

	public Aggregate(
			String name,
			String functionName,
			String targetColumnName,
			Map<String, ColumnDescriptor> columnDescriptors) {
		this.name = name;
		this.function = AggregateFunction.FUNCTIONS_BY_SQL_NAMES.get(functionName);
		this.targetColumnDescriptor = columnDescriptors.get(targetColumnName);
	}

	public String getName() {
		return this.name;
	}

	public AggregateFunction getFunction() {
		return this.function;
	}

	public ColumnDescriptor getTargetColumnDescriptor() {
		return this.targetColumnDescriptor;
	}

	public String databaseRepresentation() {
		return this.function.databaseRepresentation(
			this.targetColumnDescriptor.getNameForDatabase(), true, this.name);
	}

	public String emptyDatabaseRepresentation() {
		return this.function.emptyDatabaseRepresentation(this.name);
	}
}
