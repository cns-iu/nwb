package edu.iu.scipolicy.loader.isi.db.model.entity.relationship;

import java.util.Dictionary;
import java.util.Hashtable;

import edu.iu.cns.database.loader.framework.RowItem;
import edu.iu.cns.database.loader.framework.Schema;
import edu.iu.nwb.shared.isiutil.database.ISIDatabase;
import edu.iu.scipolicy.loader.isi.db.model.entity.Document;
import edu.iu.scipolicy.loader.isi.db.model.entity.Reference;

public class CitedReference extends RowItem<CitedReference> {
	public static final Schema<CitedReference> SCHEMA = new Schema<CitedReference>(
		false,
		ISIDatabase.CITED_REFERENCES_DOCUMENT_FOREIGN_KEY, Schema.FOREIGN_KEY_CLASS,
		ISIDatabase.CITED_REFERENCES_REFERENCE_FOREIGN_KEY, Schema.FOREIGN_KEY_CLASS).
		FOREIGN_KEYS(
			ISIDatabase.CITED_REFERENCES_DOCUMENT_FOREIGN_KEY, ISIDatabase.DOCUMENT_TABLE_NAME,
			ISIDatabase.CITED_REFERENCES_REFERENCE_FOREIGN_KEY, ISIDatabase.REFERENCE_TABLE_NAME);

	private Document document;
	private Reference reference;

	public CitedReference(Document document, Reference reference) {
		super(createAttributes(document, reference));
		this.document = document;
		this.reference = reference;
	}

	public Document getDocument() {
		return this.document;
	}

	public Reference getReference() {
		return this.reference;
	}

	public boolean shouldMerge(CitedReference otherCitedReference) {
		return false;
	}

	public void merge(CitedReference otherCitedReference) {
	}

	public static Dictionary<String, Comparable<?>> createAttributes(
			Document document, Reference reference) {
		Dictionary<String, Comparable<?>> attributes = new Hashtable<String, Comparable<?>>();
		attributes.put(ISIDatabase.CITED_REFERENCES_DOCUMENT_FOREIGN_KEY, document);
		attributes.put(ISIDatabase.CITED_REFERENCES_REFERENCE_FOREIGN_KEY, reference);

		return attributes;
	}
}