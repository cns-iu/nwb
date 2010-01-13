package edu.iu.scipolicy.database.isi.loader.model.entity.relationship;

import java.util.Dictionary;
import java.util.Hashtable;

import edu.iu.cns.database.loader.framework.RowItem;
import edu.iu.cns.database.loader.framework.Schema;
import edu.iu.cns.database.loader.framework.DerbyFieldType;
import edu.iu.nwb.shared.isiutil.database.ISIDatabase;
import edu.iu.scipolicy.database.isi.loader.model.entity.Document;
import edu.iu.scipolicy.database.isi.loader.model.entity.Reference;

public class CitedReference extends RowItem<CitedReference> {
	public static final Schema<CitedReference> SCHEMA = new Schema<CitedReference>(
		false,
		ISIDatabase.CITED_REFERENCES_DOCUMENT_FOREIGN_KEY, DerbyFieldType.FOREIGN_KEY,
		ISIDatabase.CITED_REFERENCES_REFERENCE_FOREIGN_KEY, DerbyFieldType.FOREIGN_KEY).
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
		if ((this.document != null) && (this.reference != null)) {
			Document otherDocument = otherCitedReference.getDocument();
			Reference otherReference = otherCitedReference.getReference();

			if ((otherDocument != null) && (otherReference != null)) {
				return (
					(this.document.getPrimaryKey() == otherDocument.getPrimaryKey()) &&
					(this.reference.getPrimaryKey() == otherReference.getPrimaryKey()));
			}
		}

		return false;
	}

	public void merge(CitedReference otherCitedReference) {
	}

	public static Dictionary<String, Comparable<?>> createAttributes(
			Document document, Reference reference) {
		Dictionary<String, Comparable<?>> attributes = new Hashtable<String, Comparable<?>>();
		attributes.put(ISIDatabase.CITED_REFERENCES_DOCUMENT_FOREIGN_KEY, document.getPrimaryKey());
		attributes.put(ISIDatabase.CITED_REFERENCES_REFERENCE_FOREIGN_KEY, reference.getPrimaryKey());

		return attributes;
	}
}