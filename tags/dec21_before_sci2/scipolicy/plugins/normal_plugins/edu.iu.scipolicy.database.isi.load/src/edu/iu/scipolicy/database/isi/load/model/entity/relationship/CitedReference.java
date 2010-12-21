package edu.iu.scipolicy.database.isi.load.model.entity.relationship;

import java.util.Dictionary;
import java.util.Hashtable;

import edu.iu.cns.database.load.framework.DerbyFieldType;
import edu.iu.cns.database.load.framework.RowItem;
import edu.iu.cns.database.load.framework.Schema;
import edu.iu.nwb.shared.isiutil.database.ISI;
import edu.iu.scipolicy.database.isi.load.model.entity.Document;
import edu.iu.scipolicy.database.isi.load.model.entity.Reference;

public class CitedReference extends RowItem<CitedReference> {
	public static final Schema<CitedReference> SCHEMA = new Schema<CitedReference>(
		false,
		ISI.CITED_REFERENCES_DOCUMENT_FOREIGN_KEY, DerbyFieldType.FOREIGN_KEY,
		ISI.CITED_REFERENCES_REFERENCE_FOREIGN_KEY, DerbyFieldType.FOREIGN_KEY).
		FOREIGN_KEYS(
			ISI.CITED_REFERENCES_DOCUMENT_FOREIGN_KEY, ISI.DOCUMENT_TABLE_NAME,
			ISI.CITED_REFERENCES_REFERENCE_FOREIGN_KEY, ISI.REFERENCE_TABLE_NAME);

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

	public static Dictionary<String, Object> createAttributes(
			Document document, Reference reference) {
		Dictionary<String, Object> attributes = new Hashtable<String, Object>();
		attributes.put(ISI.CITED_REFERENCES_DOCUMENT_FOREIGN_KEY, document.getPrimaryKey());
		attributes.put(ISI.CITED_REFERENCES_REFERENCE_FOREIGN_KEY, reference.getPrimaryKey());

		return attributes;
	}
}