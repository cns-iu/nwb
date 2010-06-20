package edu.iu.sci2.database.star.load.entity;

import java.util.Dictionary;
import java.util.Hashtable;

import edu.iu.cns.database.load.framework.RowItem;

public class GenericRelationship extends RowItem<GenericRelationship> {
	public GenericRelationship(
			String coreTableForeignKeyColumnName,
			String leafTableForeignKeyColumnName,
			GenericEntity coreEntity,
			GenericEntity leafEntity) {
		super(createAttributes(
			coreTableForeignKeyColumnName, leafTableForeignKeyColumnName, coreEntity, leafEntity));
	}

	public static Dictionary<String, Object> createAttributes(
			String coreTableForeignKeyColumnName,
			String leafTableForeignKeyColumnName,
			GenericEntity coreEntity,
			GenericEntity leafEntity) {
		Dictionary<String, Object> attributes = new Hashtable<String, Object>();
		attributes.put(coreTableForeignKeyColumnName, coreEntity.getPrimaryKey());
		attributes.put(leafTableForeignKeyColumnName, leafEntity.getPrimaryKey());

		return attributes;
	}
}