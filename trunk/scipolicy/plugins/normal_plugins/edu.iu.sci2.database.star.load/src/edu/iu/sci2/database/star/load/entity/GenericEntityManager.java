package edu.iu.sci2.database.star.load.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.iu.cns.database.load.framework.Schema;
import edu.iu.sci2.database.star.load.parameter.ColumnDescriptor;

public class GenericEntityManager {
	public static Schema<GenericEntity> createEntitySchema(ColumnDescriptor entityColumn) {
		List<ColumnDescriptor> entityColumns = new ArrayList<ColumnDescriptor>();
		entityColumns.add(entityColumn);

		return createEntitySchema(entityColumns);
	}

	public static Schema<GenericEntity> createEntitySchema(
			Collection<ColumnDescriptor> entityColumns) {
		Schema<GenericEntity> schema = new Schema<GenericEntity>(true);

		for (ColumnDescriptor entityColumn : entityColumns) {
			schema.addField(entityColumn.getNameForDatabase(), entityColumn.getType());
		}

		return schema;
	}
}