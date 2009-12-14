package edu.iu.cns.database.loader.framework;

import java.util.ArrayList;
import java.util.List;


public class EntityRelationshipTableType<
		RelationshipType extends EntityRelationship<? extends Entity, ? extends Entity> > {
	private List<RelationshipType> relationships = new ArrayList<RelationshipType>();
	private String relationshipTypeName;
	private String relationshipTableName;

	public EntityRelationshipTableType(String relationshipTypeName, String relationshipTableName) {
		this.relationshipTypeName = relationshipTypeName;
		this.relationshipTableName = relationshipTableName;
	}

	public final List<
			? extends EntityRelationship<? extends Entity, ? extends Entity> > getRelationships() {
		return this.relationships;
	}

	public final String getRelationshipTypeName() {
		return this.relationshipTypeName;
	}

	public final String getRelationshipTableName() {
		return this.relationshipTableName;
	}

	public void addRelationship(RelationshipType relationship) {
		this.relationships.add(relationship);
	}
}