package edu.iu.scipolicy.loader.isi.db.model.entity.relationship;

import java.util.Dictionary;
import java.util.Hashtable;

import edu.iu.cns.database.loader.framework.RowItem;
import edu.iu.scipolicy.loader.isi.db.model.entity.Document;
import edu.iu.scipolicy.loader.isi.db.model.entity.Reference;

public class CitedReference extends RowItem<CitedReference> {
	private Document document;
	private Reference reference;

	public CitedReference(Document document, Reference reference) {
		super(createAttributes());
		this.document = document;
		this.reference = reference;
	}

	public Document getDocument() {
		return this.document;
	}

	public Reference getReference() {
		return this.reference;
	}

	public CitedReference merge(CitedReference otherCitedReference) {
		return otherCitedReference;
	}

	public static Dictionary<String, Comparable<?>> createAttributes() {
		return new Hashtable<String, Comparable<?>>();
	}
}