package edu.iu.sci2.database.star.common.entity;

import java.util.Collection;
import java.util.List;

import edu.iu.cns.database.load.framework.EntityContainer;
import edu.iu.cns.database.load.framework.RowItemContainer;
import edu.iu.sci2.database.star.common.parameter.ColumnDescriptor;

public class CoreEntityManager {
	private RowItemContainer<GenericEntity> entities;
	private Collection<ColumnDescriptor> entityColumns;
	private boolean shouldMergeIdenticalValues;

	public CoreEntityManager(
			String coreEntityDisplayName,
			String coreEntityTableName,
			List<ColumnDescriptor> coreEntityColumns) {
		this.entities = new EntityContainer<GenericEntity>(
			coreEntityDisplayName,
			coreEntityTableName,
			GenericEntityManager.createEntitySchema(coreEntityColumns),
			GenericEntityManager.BATCH_SIZE);
		this.entityColumns = coreEntityColumns;
		this.shouldMergeIdenticalValues = false;
	}

	public RowItemContainer<GenericEntity> getEntities() {
		return this.entities;
	}

	public CoreEntity addEntity(String[] row) {
		CoreEntity entity = new CoreEntity(
			this.entities.getKeyGenerator(),
			row,
			this.entityColumns,
			this.shouldMergeIdenticalValues);
		this.entities.add(entity);

		return entity;
	}
}