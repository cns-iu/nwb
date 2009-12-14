package edu.iu.scipolicy.loader.isi.db.model.entity.relationship;

import java.util.Dictionary;
import java.util.Hashtable;

import edu.iu.cns.database.loader.framework.EntityRelationship;
import edu.iu.scipolicy.loader.isi.db.model.entity.Document;
import edu.iu.scipolicy.loader.isi.db.model.entity.Reference;

public class CitedReference extends EntityRelationship<Document, Reference> {
	public CitedReference(Document document, Reference reference) {
		super(document, reference);
	}

	public Document getDocument() {
		return getLeftEntity();
	}

	public Reference getReference() {
		return getRightEntity();
	}

	public Dictionary<String, Object> createAttributes() {
		return new Hashtable<String, Object>();
	}
}