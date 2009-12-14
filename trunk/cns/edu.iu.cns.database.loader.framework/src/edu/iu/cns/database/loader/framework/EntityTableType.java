package edu.iu.cns.database.loader.framework;

import java.util.ArrayList;
import java.util.List;

import edu.iu.cns.database.loader.framework.utilities.DatabaseTableKeyGenerator;

public class EntityTableType<EntityType extends Entity> {
	private DatabaseTableKeyGenerator keyGenerator = new DatabaseTableKeyGenerator();
	private List<EntityType> entities = new ArrayList<EntityType>();
	private String entityTypeName;
	private String entityTableName;

	public EntityTableType(String entityTypeName) {
		this(entityTypeName, entityTypeName.toUpperCase());
	}

	public EntityTableType(String entityTypeName, String entityTableName) {
		this.entityTypeName = entityTypeName;
		this.entityTableName = entityTableName;
	}

	public DatabaseTableKeyGenerator getKeyGenerator() {
		return this.keyGenerator;
	}

	public final List<? extends Entity> getEntities() {
		return this.entities;
	}

	public final String getEntityTypeName() {
		return this.entityTypeName;
	}

	public final String getEntityTableName() {
		return this.entityTableName;
	}

	public EntityType getOrAddEntity(EntityType newEntity) {
		EntityType duplicateEntity = findDuplicateEntity(newEntity);

		if (duplicateEntity == null) {
			this.entities.add(newEntity);

			return newEntity;
		} else {
			return duplicateEntity;
		}
	}

	private EntityType findDuplicateEntity(EntityType newEntity) {
		for (EntityType originalEntity : this.entities) {
			if (originalEntity.equals(newEntity)) {
				return originalEntity;
			}
		}

		return null;
	}
}