package edu.iu.scipolicy.loader.isi.db.model.entity.relationship;

import java.util.Dictionary;
import java.util.Hashtable;

import edu.iu.cns.database.loader.framework.EntityRelationship;
import edu.iu.scipolicy.loader.isi.db.model.entity.Document;
import edu.iu.scipolicy.loader.isi.db.model.entity.ISIFile;

public class DocumentOccurrence extends EntityRelationship<Document, ISIFile> {
	public DocumentOccurrence(Document document, ISIFile isiFile) {
		super(document, isiFile);
	}

	public Document getDocument() {
		return getFromPrimaryKeyContainer();
	}

	public ISIFile getISIFile() {
		return getToPrimaryKeyContainer();
	}

	public Dictionary<String, Object> createAttributes() {
		return new Hashtable<String, Object>();
	}
}