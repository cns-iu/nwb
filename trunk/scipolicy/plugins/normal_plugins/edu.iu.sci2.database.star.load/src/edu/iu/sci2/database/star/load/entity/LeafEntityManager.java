package edu.iu.sci2.database.star.load.entity;

import java.util.ArrayList;
import java.util.Collection;

import org.cishell.utilities.ListUtilities;
import org.cishell.utilities.StringUtilities;

import edu.iu.cns.database.load.framework.DerbyFieldType;
import edu.iu.cns.database.load.framework.EntityContainer;
import edu.iu.cns.database.load.framework.RelationshipContainer;
import edu.iu.cns.database.load.framework.RowItem;
import edu.iu.cns.database.load.framework.RowItemContainer;
import edu.iu.cns.database.load.framework.Schema;
import edu.iu.sci2.database.star.load.parameter.ColumnDescriptor;

public class LeafEntityManager {
	private RowItemContainer<GenericEntity> entities;
	private ColumnDescriptor entityColumn;
	private boolean shouldMergeIdenticalValues;

	private RowItemContainer<GenericRelationship> relationships;
	private String coreEntityForeignKeyColumnName;
	private String leafEntityForeignKeyColumnName;

	public LeafEntityManager(
			RowItemContainer<GenericEntity> coreEntities, ColumnDescriptor leafEntityColumn) {
		this.entities = new EntityContainer<GenericEntity>(
			leafEntityColumn.getName(),
			leafEntityColumn.getNameForDatabase(),
			GenericEntityManager.createEntitySchema(leafEntityColumn));
		this.entityColumn = leafEntityColumn;
		this.shouldMergeIdenticalValues = GenericEntityManager.determineIdenticalValueMerging(
			ListUtilities.createAndFillList(this.entityColumn));

		this.coreEntityForeignKeyColumnName =
			constructColumnName(coreEntities.getDatabaseTableName());
		this.leafEntityForeignKeyColumnName =
			constructColumnName(leafEntityColumn.getNameForDatabase());
		this.relationships =
			constructRelationshipTable(coreEntities, leafEntityColumn);
	}

	public Collection<RowItemContainer<? extends RowItem<?>>> getRowItemContainers() {
		Collection<RowItemContainer<? extends RowItem<?>>> rowItemContainers =
			new ArrayList<RowItemContainer<? extends RowItem<?>>>();
		rowItemContainers.add(this.entities);

		rowItemContainers.add(this.relationships);

		return rowItemContainers;
	}

	public void addEntity(String[] row, CoreEntity coreEntity) {
		ColumnDescriptor column = this.entityColumn;
		String rawEntityValue = row[column.getColumnIndex()];

		if (column.isMultiValued() &&
				!StringUtilities.isNull_Empty_OrWhitespace(column.getSeparator())) {
			String[] entityValues =
				StringUtilities.getAllTokens(rawEntityValue, column.getSeparator(), true);

			for (String entityValue : entityValues) {
				if (StringUtilities.isNull_Empty_OrWhitespace(entityValue)) {
					continue;
				}

				GenericEntity entity = this.entities.add(new LeafEntity(
					this.entities.getKeyGenerator(),
					entityValue,
					column,
					this.shouldMergeIdenticalValues));

				this.relationships.add(new GenericRelationship(
					this.coreEntityForeignKeyColumnName,
					this.leafEntityForeignKeyColumnName,
					coreEntity,
					entity));
			}
		} else if (!StringUtilities.isNull_Empty_OrWhitespace(rawEntityValue)) {
			GenericEntity entity = this.entities.add(new LeafEntity(
				this.entities.getKeyGenerator(),
				rawEntityValue,
				column,
				this.shouldMergeIdenticalValues));

			this.relationships.add(new GenericRelationship(
				this.coreEntityForeignKeyColumnName,
				this.leafEntityForeignKeyColumnName,
				coreEntity,
				entity));
		}
	}

	public static String constructColumnName(String tableName) {
		return tableName + "_FOREIGN_KEY";
	}

	private RowItemContainer<GenericRelationship> constructRelationshipTable(
			RowItemContainer<GenericEntity> coreEntities, ColumnDescriptor leafEntityColumn) {
		return new RelationshipContainer<GenericRelationship>(
			constructRelationshipDisplayName(
				coreEntities.getHumanReadableName(), leafEntityColumn.getName()),
			constructRelationshipTableName(
				coreEntities.getDatabaseTableName(), leafEntityColumn.getNameForDatabase()),
			constructRelationshipSchema(
				leafEntityColumn,
				this.coreEntityForeignKeyColumnName,
				this.leafEntityForeignKeyColumnName));
	}

	public static String constructRelationshipDisplayName(
			String coreHumanReadableName, String leafHumanReadableName) {
		return coreHumanReadableName + "-" + leafHumanReadableName;
	}

	public static String constructRelationshipTableName(
			String coreDatabaseTableName, String leafDatabaseTableName) {
		return coreDatabaseTableName + "_TO_" + leafDatabaseTableName;
	}

	public static Schema<GenericRelationship> constructRelationshipSchema(
			ColumnDescriptor leafEntityColumn,
			String coreEntityForeignKeyColumnName,
			String leafEntityForeignKeyColumnName) {
		Schema<GenericRelationship> schema = new Schema<GenericRelationship>(
			false,
			coreEntityForeignKeyColumnName, DerbyFieldType.FOREIGN_KEY,
			leafEntityForeignKeyColumnName, DerbyFieldType.FOREIGN_KEY);

		return schema;
	}
}