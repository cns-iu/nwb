package edu.iu.scipolicy.loader.isi.db.model.entity.relationship;

import java.util.Dictionary;
import java.util.Hashtable;

import edu.iu.cns.database.loader.framework.RowItem;
import edu.iu.cns.database.loader.framework.Schema;
import edu.iu.nwb.shared.isiutil.database.ISIDatabase;
import edu.iu.scipolicy.loader.isi.db.model.entity.Document;
import edu.iu.scipolicy.loader.isi.db.model.entity.ISIFile;

public class DocumentOccurrence extends RowItem<DocumentOccurrence> {
	public static final Schema<DocumentOccurrence> SCHEMA = new Schema<DocumentOccurrence>(
		ISIDatabase.DOCUMENT_OCCURRENCES_DOCUMENT_FOREIGN_KEY, Schema.FOREIGN_KEY_CLASS,
		ISIDatabase.DOCUMENT_OCCURRENCES_ISI_FILE_FOREIGN_KEY, Schema.FOREIGN_KEY_CLASS).
		FOREIGN_KEYS(
			ISIDatabase.DOCUMENT_OCCURRENCES_DOCUMENT_FOREIGN_KEY,
				ISIDatabase.DOCUMENT_TABLE_NAME,
			ISIDatabase.DOCUMENT_OCCURRENCES_ISI_FILE_FOREIGN_KEY,
				ISIDatabase.ISI_FILE_TABLE_NAME);

	private Document document;
	private ISIFile isiFile;

	public DocumentOccurrence(Document document, ISIFile isiFile) {
		super(createAttributes());
		this.document = document;
		this.isiFile = isiFile;
	}

	public Document getDocument() {
		return this.document;
	}

	public ISIFile getISIFile() {
		return this.isiFile;
	}

	public boolean shouldMerge(DocumentOccurrence otherDocumentOccurrence) {
		return false;
	}

	public void merge(DocumentOccurrence otherDocumentOccurrence) {
	}

	public static Dictionary<String, Comparable<?>> createAttributes() {
		return new Hashtable<String, Comparable<?>>();
	}
}