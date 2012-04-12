package edu.iu.cns.database.load.framework;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class EntityContainer<T extends Entity<T>> extends RowItemContainer<T> {
	private Map<Object, T> items = new HashMap<Object, T>();

	public EntityContainer(String humanReadableName, Schema<T> schema) {
		super(humanReadableName, schema);
	}

	public EntityContainer(String humanReadableName, Schema<T> schema, int batchSize) {
		super(humanReadableName, schema, batchSize);
	}

	public EntityContainer(String humanReadableName, String databaseTableName, Schema<T> schema) {
		super(humanReadableName, databaseTableName, schema);
	}

	public EntityContainer(
			String humanReadableName, String databaseTableName, Schema<T> schema, int batchSize) {
		super(humanReadableName, databaseTableName, schema, batchSize);
	}

	public Collection<T> getItems() {
		return this.items.values();
	}

	public final T add(T newItem) {
		Object newItemMergeKey = newItem.createMergeKey();

		if (this.items.containsKey(newItemMergeKey)) {
			T originalItem = this.items.get(newItemMergeKey);
			originalItem.merge(newItem);

			return originalItem;
		}
		this.items.put(newItemMergeKey, newItem);

		return newItem;
	}
}