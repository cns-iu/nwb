package edu.iu.scipolicy.loader.isi.db.model.entity.relationship;

import java.util.Dictionary;
import java.util.Hashtable;

import edu.iu.cns.database.loader.framework.RowItem;
import edu.iu.cns.database.loader.framework.Schema;
import edu.iu.cns.database.loader.framework.DerbyFieldType;
import edu.iu.nwb.shared.isiutil.database.ISIDatabase;
import edu.iu.scipolicy.loader.isi.db.model.entity.Document;
import edu.iu.scipolicy.loader.isi.db.model.entity.Patent;

public class CitedPatent extends RowItem<CitedPatent> {
	public static final Schema<CitedPatent> SCHEMA = new Schema<CitedPatent>(
		false,
		ISIDatabase.CITED_PATENTS_DOCUMENT_FOREIGN_KEY, DerbyFieldType.FOREIGN_KEY,
		ISIDatabase.CITED_PATENTS_PATENT_FOREIGN_KEY, DerbyFieldType.FOREIGN_KEY).
		FOREIGN_KEYS(
			ISIDatabase.CITED_PATENTS_DOCUMENT_FOREIGN_KEY, ISIDatabase.DOCUMENT_TABLE_NAME,
			ISIDatabase.CITED_PATENTS_PATENT_FOREIGN_KEY, ISIDatabase.PATENT_TABLE_NAME);

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

	public boolean shouldMerge(CitedPatent otherCitedPatent) {
		return false;
	}

	public void merge(CitedPatent otherCitedPatent) {
	}

	public static Dictionary<String, Comparable<?>> createAttributes(
			Document document, Patent patent) {
		Dictionary<String, Comparable<?>> attributes = new Hashtable<String, Comparable<?>>();
		attributes.put(ISIDatabase.CITED_PATENTS_DOCUMENT_FOREIGN_KEY, document.getPrimaryKey());
		attributes.put(ISIDatabase.CITED_PATENTS_PATENT_FOREIGN_KEY, patent.getPrimaryKey());

		return attributes;
	}
}