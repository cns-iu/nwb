package edu.iu.cns.database.loader.framework;

import java.util.Dictionary;


public abstract class EntityRelationship<
		FromType extends PrimaryKeyContainer, ToType extends PrimaryKeyContainer> {
	public static final String FROM_PRIMARY_KEY_CONTAINER = "from_primary_key_container";
	public static final String TO_PRIMARY_KEY_CONTAINER = "to_primary_key_container";

	private FromType fromPrimaryKeyContainer;
	private ToType toPrimaryKeyContainer;
	private Dictionary<String, Object> attributes;

	public EntityRelationship(
			FromType fromPrimaryKeyContainer,
			ToType toPrimaryKeyContainer,
			Dictionary<String, Object> attributes) {
		this.fromPrimaryKeyContainer = fromPrimaryKeyContainer;
		this.toPrimaryKeyContainer = toPrimaryKeyContainer;
		this.attributes = attributes;

		// TODO: Hhmmm, is this the right thing to do?
		this.attributes.put(FROM_PRIMARY_KEY_CONTAINER, fromPrimaryKeyContainer);
		this.attributes.put(TO_PRIMARY_KEY_CONTAINER, toPrimaryKeyContainer);
	}

	public final FromType getFromPrimaryKeyContainer() {
		return this.fromPrimaryKeyContainer;
	}

	public final ToType getToPrimaryKeyContainer() {
		return this.toPrimaryKeyContainer;
	}

	public final Dictionary<String, Object> getAttributes() {
		return this.attributes;
	}

	// TODO: formInsertSQLQuery?
}