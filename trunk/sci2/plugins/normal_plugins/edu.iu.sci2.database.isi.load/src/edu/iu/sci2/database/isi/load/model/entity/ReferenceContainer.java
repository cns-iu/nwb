package edu.iu.sci2.database.isi.load.model.entity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import edu.iu.cns.database.load.framework.EntityContainer;
import edu.iu.cns.database.load.framework.RowItemContainer;
import edu.iu.cns.database.load.framework.Schema;

public class ReferenceContainer extends RowItemContainer<Reference> {
	private EntityContainer<Reference> references;
	private Map<Object, Integer> mergeKeysToPrimaryKeys = new HashMap<Object, Integer>();

	public ReferenceContainer(String humanReadableName, Schema<Reference> schema) {
		super(humanReadableName, schema);
		this.references = new EntityContainer<Reference>(humanReadableName, schema);
	}

	public ReferenceContainer(
			String humanReadableName, Schema<Reference> schema, int batchSize) {
		super(humanReadableName, schema, batchSize);
		this.references = new EntityContainer<Reference>(humanReadableName, schema);
	}

	public ReferenceContainer(
			String humanReadableName, String databaseTableName, Schema<Reference> schema) {
		super(humanReadableName, databaseTableName, schema);
		this.references =
			new EntityContainer<Reference>(humanReadableName, databaseTableName, schema);
	}

	public ReferenceContainer(
			String humanReadableName,
			String databaseTableName,
			Schema<Reference> schema,
			int batchSize) {
		super(humanReadableName, databaseTableName, schema, batchSize);
		this.references =
			new EntityContainer<Reference>(humanReadableName, databaseTableName, schema);
	}

	public Collection<Reference> getItems() {
		Collection<Reference> resultReferences = this.references.getItems();

		return resultReferences;
	}

	public final Reference add(Reference newItem) {
		return newItem;
	}
}