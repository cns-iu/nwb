package edu.iu.scipolicy.database.isi.load.model.entity.relationship;

import java.util.Dictionary;
import java.util.Hashtable;

import edu.iu.cns.database.load.framework.DerbyFieldType;
import edu.iu.cns.database.load.framework.RowItem;
import edu.iu.cns.database.load.framework.Schema;
import edu.iu.nwb.shared.isiutil.database.ISI;
import edu.iu.scipolicy.database.isi.load.model.entity.Document;
import edu.iu.scipolicy.database.isi.load.model.entity.Patent;

public class CitedPatent extends RowItem<CitedPatent> {
	public static final Schema<CitedPatent> SCHEMA = new Schema<CitedPatent>(
		false,
		ISI.CITED_PATENTS_DOCUMENT_FOREIGN_KEY, DerbyFieldType.FOREIGN_KEY,
		ISI.CITED_PATENTS_PATENT_FOREIGN_KEY, DerbyFieldType.FOREIGN_KEY).
		FOREIGN_KEYS(
			ISI.CITED_PATENTS_DOCUMENT_FOREIGN_KEY, ISI.DOCUMENT_TABLE_NAME,
			ISI.CITED_PATENTS_PATENT_FOREIGN_KEY, ISI.PATENT_TABLE_NAME);

	private Document document;
	private Patent patent;

	public CitedPatent(Document document, Patent patent) {
		super(createAttributes(document, patent));
		this.document = document;
		this.patent = patent;
	}

	public Document getDocument() {
		return this.document;
	}

	public Patent getPatent() {
		return this.patent;
	}

	public static Dictionary<String, Object> createAttributes(
			Document document, Patent patent) {
		Dictionary<String, Object> attributes = new Hashtable<String, Object>();
		attributes.put(ISI.CITED_PATENTS_DOCUMENT_FOREIGN_KEY, document.getPrimaryKey());
		attributes.put(ISI.CITED_PATENTS_PATENT_FOREIGN_KEY, patent.getPrimaryKey());

		return attributes;
	}
}