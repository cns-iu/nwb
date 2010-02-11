package edu.iu.cns.database.load.framework;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.iu.cns.database.load.framework.utilities.DatabaseTableKeyGenerator;

public class RowItemContainer<T extends RowItem<T>> {
	private DatabaseTableKeyGenerator keyGenerator = new DatabaseTableKeyGenerator();
	//private List<T> items = new ArrayList<T>();
	private Map<Object, T> items = new HashMap<Object, T>();
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

	public final Collection<T> getItems() {
		return this.items.values();
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
		Object newItemMergeKey = newItem.createMergeKey();

		if (this.items.containsKey(newItemMergeKey)) {
			T originalItem = this.items.get(newItemMergeKey);
			originalItem.merge(newItem);

			return originalItem;
		} else {
			this.items.put(newItemMergeKey, newItem);

			return newItem;
		}
	}
}