package edu.iu.cns.database.load.framework;

import java.util.Collection;

import edu.iu.cns.database.load.framework.utilities.DatabaseTableKeyGenerator;

public abstract class RowItemContainer<T extends RowItem<T>> {
	public static final int NO_BATCHING = -1;

	private DatabaseTableKeyGenerator keyGenerator = new DatabaseTableKeyGenerator();
	private String humanReadableName;
	private String databaseTableName;
	private Schema<T> schema;
	private int batchSize = NO_BATCHING;

	public RowItemContainer(String humanReadableName, Schema<T> schema) {
		this(humanReadableName, humanReadableName.toUpperCase(), schema);
	}

	public RowItemContainer(String humanReadableName, Schema<T> schema, int batchSize) {
		this(humanReadableName, humanReadableName.toUpperCase(), schema, batchSize);
	}

	public RowItemContainer(String humanReadableName, String databaseTableName, Schema<T> schema) {
		this.humanReadableName = humanReadableName;
		this.databaseTableName = databaseTableName;
		this.schema = schema;
	}

	public RowItemContainer(
			String humanReadableName, String databaseTableName, Schema<T> schema, int batchSize) {
		this.humanReadableName = humanReadableName;
		this.databaseTableName = databaseTableName;
		this.schema = schema;
		this.batchSize = batchSize;
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

	public final int getBatchSize() {
		return this.batchSize;
	}

	public final boolean shouldInsertInBatches() {
		return (this.batchSize != NO_BATCHING);
	}

	/**
	 * If the Entity is already in this collection, merges the Entity.  Otherwise, adds it.
	 * 
	 * Already in the collection is determined with {@link Entity.createMergeKey()}
	 * @param newItem
	 * @return
	 */
	public abstract T add(T newItem);
}