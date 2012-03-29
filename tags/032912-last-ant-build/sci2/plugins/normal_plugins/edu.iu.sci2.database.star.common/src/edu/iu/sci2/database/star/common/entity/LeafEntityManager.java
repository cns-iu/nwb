package edu.iu.sci2.database.star.common.entity;

import java.util.ArrayList;
import java.util.Collection;

import org.cishell.utilities.StringUtilities;

import edu.iu.cns.database.load.framework.DerbyFieldType;
import edu.iu.cns.database.load.framework.EntityContainer;
import edu.iu.cns.database.load.framework.RelationshipContainer;
import edu.iu.cns.database.load.framework.RowItem;
import edu.iu.cns.database.load.framework.RowItemContainer;
import edu.iu.cns.database.load.framework.Schema;
import edu.iu.sci2.database.star.common.parameter.ColumnDescriptor;

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
			GenericEntityManager.createEntitySchema(leafEntityColumn),
			GenericEntityManager.BATCH_SIZE);
		this.entityColumn = leafEntityColumn;
		this.shouldMergeIdenticalValues = this.entityColumn.shouldMergeIdenticalValues();

		String coreTableName = coreEntities.getDatabaseTableName();
		String leafTableName = leafEntityColumn.getNameForDatabase();
		String relationshipTableName =
			constructRelationshipTableName(coreEntities.getDatabaseTableName(), leafTableName);

		this.coreEntityForeignKeyColumnName =
			constructColumnName(relationshipTableName, coreEntities.getDatabaseTableName());
		this.leafEntityForeignKeyColumnName =
			constructColumnName(relationshipTableName, leafEntityColumn.getNameForDatabase());
		this.relationships = constructRelationshipTable(
			coreEntities, leafEntityColumn, coreTableName, leafTableName, relationshipTableName);
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
				StringUtilities.getAllTokens(rawEntityValue, column.getSeparator(), true, true);

			for (int ii = 0; ii < entityValues.length; ii++) {
				String entityValue = entityValues[ii];

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
					entity,
					(ii + 1)));
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
				entity,
				1));
		}
	}

	public static String constructColumnName(
			String relationshipTableName, String entityTableName) {
		return relationshipTableName + "_" + entityTableName + "_FOREIGN_KEY";
	}

	private RowItemContainer<GenericRelationship> constructRelationshipTable(
			RowItemContainer<GenericEntity> coreEntities,
			ColumnDescriptor leafEntityColumn,
			String coreTableName,
			String leafTableName,
			String relationshipTableName) {
		return new RelationshipContainer<GenericRelationship>(
			constructRelationshipDisplayName(coreEntities.getHumanReadableName(), coreTableName),
			relationshipTableName,
			constructRelationshipSchema(
				this.coreEntityForeignKeyColumnName,
				this.leafEntityForeignKeyColumnName,
				coreTableName,
				leafTableName));
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
			String coreEntityForeignKeyColumnName,
			String leafEntityForeignKeyColumnName,
			String coreTableName,
			String leafTableName) {
		Schema<GenericRelationship> schema = new Schema<GenericRelationship>(
			false,
			coreEntityForeignKeyColumnName, DerbyFieldType.FOREIGN_KEY,
			leafEntityForeignKeyColumnName, DerbyFieldType.FOREIGN_KEY,
			GenericRelationship.ORDER_NAME, DerbyFieldType.INTEGER).
			FOREIGN_KEYS(
				coreEntityForeignKeyColumnName, coreTableName,
				leafEntityForeignKeyColumnName, leafTableName);

		return schema;
	}
}