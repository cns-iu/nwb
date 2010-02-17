package edu.iu.scipolicy.database.isi.load.model.entity.relationship;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import edu.iu.cns.database.load.framework.DerbyFieldType;
import edu.iu.cns.database.load.framework.RowItem;
import edu.iu.cns.database.load.framework.Schema;
import edu.iu.nwb.shared.isiutil.database.ISI;
import edu.iu.scipolicy.database.isi.load.model.entity.Document;
import edu.iu.scipolicy.database.isi.load.model.entity.ISIFile;

public class DocumentOccurrence extends RowItem<DocumentOccurrence> {
	public static final Schema<DocumentOccurrence> SCHEMA = new Schema<DocumentOccurrence>(
		false,
		ISI.DOCUMENT_OCCURRENCES_DOCUMENT_FOREIGN_KEY, DerbyFieldType.FOREIGN_KEY,
		ISI.DOCUMENT_OCCURRENCES_ISI_FILE_FOREIGN_KEY, DerbyFieldType.FOREIGN_KEY).
		FOREIGN_KEYS(
			ISI.DOCUMENT_OCCURRENCES_DOCUMENT_FOREIGN_KEY,
				ISI.DOCUMENT_TABLE_NAME,
			ISI.DOCUMENT_OCCURRENCES_ISI_FILE_FOREIGN_KEY,
				ISI.ISI_FILE_TABLE_NAME);

	private Document document;
	private ISIFile isiFile;

	public DocumentOccurrence(Document document, ISIFile isiFile) {
		super(createAttributes(document, isiFile));
		this.document = document;
		this.isiFile = isiFile;
	}

	public Document getDocument() {
		return this.document;
	}

	public ISIFile getISIFile() {
		return this.isiFile;
	}

	public static Dictionary<String, Object> createAttributes(
			Document document, ISIFile isiFile) {
		Dictionary<String, Object> attributes = new Hashtable<String, Object>();
		attributes.put(ISI.DOCUMENT_OCCURRENCES_DOCUMENT_FOREIGN_KEY, document.getPrimaryKey());
		attributes.put(ISI.DOCUMENT_OCCURRENCES_ISI_FILE_FOREIGN_KEY, isiFile.getPrimaryKey());

		return attributes;
	}
}