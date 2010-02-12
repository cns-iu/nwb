package edu.iu.cns.database.load.framework;

import java.util.Collection;

import edu.iu.cns.database.load.framework.utilities.DatabaseTableKeyGenerator;

public abstract class RowItemContainer<T extends RowItem<T>> {
	private DatabaseTableKeyGenerator keyGenerator = new DatabaseTableKeyGenerator();
	private String humanReadableName;
	private String databaseTableName;
	private Schema<T> schema;

	public RowItemContainer(String humanReadableName, Schema<T> schema) {
		this(humanReadableName, humanReadableName.toUpperCase(), schema);
	}

	public RowItemContainer(String humanReadableName, String databaseTableName, Schema<T> schema) {
		this.humanReadableName = humanReadableName;
		this.databaseTableName = databaseTableName;
		this.schema = schema;
	}

	public final DatabaseTableKeyGenerator getKeyGenerator() {
		return this.keyGenerator;
	}

	public abstract Collection<T> getItems();

	public final String getHumanReadableName() {
		return this.humanReadableName;
	}

	public final String getDatabaseTableName() {
		return this.databaseTableName;
	}

	public final Schema<T> getSchema() {
		return this.schema;
	}

	public abstract T add(T newItem);
}