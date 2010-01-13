package edu.iu.scipolicy.database.isi.loader.model.entity.relationship;

import java.util.Dictionary;
import java.util.Hashtable;

import edu.iu.cns.database.loader.framework.RowItem;
import edu.iu.cns.database.loader.framework.Schema;
import edu.iu.cns.database.loader.framework.DerbyFieldType;
import edu.iu.nwb.shared.isiutil.database.ISIDatabase;
import edu.iu.scipolicy.database.isi.loader.model.entity.Document;
import edu.iu.scipolicy.database.isi.loader.model.entity.ISIFile;

public class DocumentOccurrence extends RowItem<DocumentOccurrence> {
	public static final Schema<DocumentOccurrence> SCHEMA = new Schema<DocumentOccurrence>(
		false,
		ISIDatabase.DOCUMENT_OCCURRENCES_DOCUMENT_FOREIGN_KEY, DerbyFieldType.FOREIGN_KEY,
		ISIDatabase.DOCUMENT_OCCURRENCES_ISI_FILE_FOREIGN_KEY, DerbyFieldType.FOREIGN_KEY).
		FOREIGN_KEYS(
			ISIDatabase.DOCUMENT_OCCURRENCES_DOCUMENT_FOREIGN_KEY,
				ISIDatabase.DOCUMENT_TABLE_NAME,
			ISIDatabase.DOCUMENT_OCCURRENCES_ISI_FILE_FOREIGN_KEY,
				ISIDatabase.ISI_FILE_TABLE_NAME);

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

	public boolean shouldMerge(DocumentOccurrence otherDocumentOccurrence) {
		if ((this.document != null) && (this.isiFile != null)) {
			Document otherDocument = otherDocumentOccurrence.getDocument();
			ISIFile otherISIFile = otherDocumentOccurrence.getISIFile();

			if ((otherDocument != null) && (otherISIFile != null)) {
				return (
					(this.document.getPrimaryKey() == otherDocument.getPrimaryKey()) &&
					(this.isiFile.getPrimaryKey() == otherISIFile.getPrimaryKey()));
			}
		}

		return false;
	}

	public void merge(DocumentOccurrence otherDocumentOccurrence) {
	}

	public static Dictionary<String, Comparable<?>> createAttributes(
			Document document, ISIFile isiFile) {
		Dictionary<String, Comparable<?>> attributes = new Hashtable<String, Comparable<?>>();
		attributes.put(ISIDatabase.DOCUMENT_OCCURRENCES_DOCUMENT_FOREIGN_KEY, document.getPrimaryKey());
		attributes.put(ISIDatabase.DOCUMENT_OCCURRENCES_ISI_FILE_FOREIGN_KEY, isiFile.getPrimaryKey());

		return attributes;
	}
}