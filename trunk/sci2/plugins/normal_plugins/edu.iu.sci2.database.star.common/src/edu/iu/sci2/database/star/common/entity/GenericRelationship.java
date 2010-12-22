package edu.iu.sci2.database.star.common.entity;

import java.util.Dictionary;
import java.util.Hashtable;

import edu.iu.cns.database.load.framework.RowItem;

public class GenericRelationship extends RowItem<GenericRelationship> {
	public static String ORDER_NAME = "ORDER";

	public GenericRelationship(
			String coreTableForeignKeyColumnName,
			String leafTableForeignKeyColumnName,
			GenericEntity coreEntity,
			GenericEntity leafEntity,
			Integer order) {
		super(createAttributes(
			coreTableForeignKeyColumnName,
			leafTableForeignKeyColumnName,
			coreEntity,
			leafEntity,
			order));
	}

	public static Dictionary<String, Object> createAttributes(
			String coreTableForeignKeyColumnName,
			String leafTableForeignKeyColumnName,
			GenericEntity coreEntity,
			GenericEntity leafEntity,
			Integer order) {
		Dictionary<String, Object> attributes = new Hashtable<String, Object>();
		attributes.put(coreTableForeignKeyColumnName, coreEntity.getPrimaryKey());
		attributes.put(leafTableForeignKeyColumnName, leafEntity.getPrimaryKey());
		attributes.put(ORDER_NAME, order);

		return attributes;
	}
}