package edu.iu.scipolicy.database.isi.load.model.entity.relationship;

import java.util.Dictionary;
import java.util.Hashtable;

import edu.iu.cns.database.loader.framework.RowItem;
import edu.iu.cns.database.loader.framework.Schema;
import edu.iu.cns.database.loader.framework.DerbyFieldType;
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

	public boolean shouldMerge(CitedPatent otherCitedPatent) {
		if ((this.document != null) && (this.patent != null)) {
			Document otherDocument = otherCitedPatent.getDocument();
			Patent otherPatent = otherCitedPatent.getPatent();

			if ((otherDocument != null) && (otherPatent != null)) {
				return (
					(this.document.getPrimaryKey() == otherDocument.getPrimaryKey()) &&
					(this.patent.getPrimaryKey() == otherPatent.getPrimaryKey()));
			}
		}

		return false;
	}

	public void merge(CitedPatent otherCitedPatent) {
	}

	public static Dictionary<String, Comparable<?>> createAttributes(
			Document document, Patent patent) {
		Dictionary<String, Comparable<?>> attributes = new Hashtable<String, Comparable<?>>();
		attributes.put(ISI.CITED_PATENTS_DOCUMENT_FOREIGN_KEY, document.getPrimaryKey());
		attributes.put(ISI.CITED_PATENTS_PATENT_FOREIGN_KEY, patent.getPrimaryKey());

		return attributes;
	}
}