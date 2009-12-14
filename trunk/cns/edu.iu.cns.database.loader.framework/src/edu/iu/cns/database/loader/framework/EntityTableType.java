package edu.iu.cns.database.loader.framework;

import java.util.ArrayList;
import java.util.List;

public class EntityTableType<EntityObjectType extends Entity> {
	private List<EntityObjectType> entities = new ArrayList<EntityObjectType>();
	private String entityTypeName;
	private String entityTableName;

	public EntityTableType(String entityTypeName) {
		this(entityTypeName, entityTypeName.toUpperCase());
	}

	public EntityTableType(String entityTypeName, String entityTableName) {
		this.entityTypeName = entityTypeName;
		this.entityTableName = entityTableName;
	}
	
	public int getEntityPrimaryKey(EntityObjectType entity) {
		return entity.getPrimaryKey();
	}

	public final List<? extends Entity> getEntities() {
	// public final List<EntityObjectType> getEntities() {
	
		return this.entities;
	}

	public final String getEntityTypeName() {
		return this.entityTypeName;
	}

	public final String getEntityTableName() {
		return this.entityTableName;
	}

	public EntityObjectType addEntity(EntityObjectType newEntity) {
		EntityObjectType duplicateEntity = findDuplicateEntity(newEntity);

		if (duplicateEntity == null) {
			this.entities.add(newEntity);

			return newEntity;
		} else {
			return duplicateEntity;
		}
	}

	private EntityObjectType findDuplicateEntity(EntityObjectType newEntity) {
		for (EntityObjectType originalEntity : this.entities) {
			if (originalEntity.equals(newEntity)) {
				return originalEntity;
			}
		}

		return null;
	}
}