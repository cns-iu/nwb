package edu.iu.scipolicy.loader.isi.db.model.entity.relationship;

import java.util.Dictionary;
import java.util.Hashtable;

import edu.iu.cns.database.loader.framework.RowItem;
import edu.iu.scipolicy.loader.isi.db.model.entity.Document;
import edu.iu.scipolicy.loader.isi.db.model.entity.ISIFile;

public class DocumentOccurrence extends RowItem<DocumentOccurrence> {
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

	public DocumentOccurrence merge(DocumentOccurrence otherDocumentOccurrence) {
		return otherDocumentOccurrence;
	}

	public static Dictionary<String, Comparable<?>> createAttributes() {
		return new Hashtable<String, Comparable<?>>();
	}
}