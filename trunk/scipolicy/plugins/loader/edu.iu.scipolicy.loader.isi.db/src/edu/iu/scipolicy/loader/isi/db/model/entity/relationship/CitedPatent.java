package edu.iu.scipolicy.loader.isi.db.model.entity.relationship;

import java.util.Dictionary;
import java.util.Hashtable;

import edu.iu.cns.database.loader.framework.EntityRelationship;
import edu.iu.scipolicy.loader.isi.db.model.entity.Document;
import edu.iu.scipolicy.loader.isi.db.model.entity.Patent;

public class CitedPatent extends EntityRelationship<Document, Patent> {
	public CitedPatent(Document document, Patent patent) {
		super(document, patent);
	}

	public Document getDocument() {
		return getLeftEntity();
	}

	public Patent getPatent() {
		return getRightEntity();
	}

	public Dictionary<String, Object> createAttributes() {
		return new Hashtable<String, Object>();
	}
}