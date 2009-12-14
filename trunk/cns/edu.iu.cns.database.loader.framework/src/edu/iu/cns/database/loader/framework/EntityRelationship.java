package edu.iu.cns.database.loader.framework;

import java.util.Dictionary;


public abstract class EntityRelationship<LeftType extends Entity, RightType extends Entity> {
	private LeftType leftEntity;
	private RightType rightEntity;
	private Dictionary<String, Object> attributes;

	public EntityRelationship(LeftType leftEntity, RightType rightEntity) {
		this.leftEntity = leftEntity;
		this.rightEntity = rightEntity;

		this.attributes = createAttributes();
	}

	public final LeftType getLeftEntity() {
		return this.leftEntity;
	}

	public final RightType getRightEntity() {
		return this.rightEntity;
	}

	public final Dictionary<String, Object> getAttributes() {
		return this.attributes;
	}

	public abstract Dictionary<String, Object> createAttributes();

	// TODO: formInsertSQLQuery?
}