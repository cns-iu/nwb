package edu.iu.cns.database.load.framework;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RelationshipContainer<T extends RowItem<T>> extends RowItemContainer<T> {
	private List<T> items = new ArrayList<T>();

	public RelationshipContainer(String humanReadableName, Schema<T> schema) {
		super(humanReadableName, schema);
	}

	public RelationshipContainer(String humanReadableName, Schema<T> schema, int batchSize) {
		super(humanReadableName, schema, batchSize);
	}

	public RelationshipContainer(
			String humanReadableName, String databaseTableName, Schema<T> schema) {
		super(humanReadableName, databaseTableName, schema);
	}

	public RelationshipContainer(
			String humanReadableName, String databaseTableName, Schema<T> schema, int batchSize) {
		super(humanReadableName, databaseTableName, schema, batchSize);
	}

	public Collection<T> getItems() {
		return this.items;
	}

	public T add(T newItem) {
		this.items.add(newItem);

		return newItem;
	}
}