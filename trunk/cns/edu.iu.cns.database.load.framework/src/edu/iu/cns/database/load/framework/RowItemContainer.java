package edu.iu.cns.database.load.framework;

import java.util.ArrayList;
import java.util.List;

import edu.iu.cns.database.load.framework.utilities.DatabaseTableKeyGenerator;

public class RowItemContainer<T extends RowItem<T>> {
	private DatabaseTableKeyGenerator keyGenerator = new DatabaseTableKeyGenerator();
	private List<T> items = new ArrayList<T>();
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

	public final List<? extends RowItem<T>> getItems() {
		return this.items;
	}

	public final String getHumanReadableName() {
		return this.humanReadableName;
	}

	public final String getDatabaseTableName() {
		return this.databaseTableName;
	}

	public final Schema<T> getSchema() {
		return this.schema;
	}
	
	
	//TODO: replace with a method using item keys, to avoid the computational complexity hit
	public final T addOrMerge(T newItem) {
		for (T originalItem : this.items) {
			if (originalItem == newItem) {
				return originalItem;
			} else if (originalItem.shouldMerge(newItem)) {
				originalItem.merge(newItem);

				return originalItem;
			}
		}

		this.items.add(newItem);

		return newItem;
	}
}