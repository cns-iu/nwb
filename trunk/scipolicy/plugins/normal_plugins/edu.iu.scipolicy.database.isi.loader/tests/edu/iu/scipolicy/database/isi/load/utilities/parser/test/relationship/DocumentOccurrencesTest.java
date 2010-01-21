package edu.iu.scipolicy.database.isi.load.utilities.parser.test.relationship;


import java.util.List;

import edu.iu.scipolicy.database.isi.load.model.entity.Document;
import edu.iu.scipolicy.database.isi.load.model.entity.ISIFile;
import edu.iu.scipolicy.database.isi.load.model.entity.relationship.DocumentOccurrence;
import edu.iu.scipolicy.database.isi.load.utilities.parser.RowItemTest;

public class DocumentOccurrencesTest extends RowItemTest {
	public static DocumentOccurrence getDocumentOccurrence(
			List<DocumentOccurrence> documentOccurrences, Document document, ISIFile isiFile) {
		for (DocumentOccurrence documentOccurrence : documentOccurrences) {
			boolean documentsMatch =
				(documentOccurrence.getDocument().getPrimaryKey() == document.getPrimaryKey());
			boolean isiFilesMatch =
				(documentOccurrence.getISIFile().getPrimaryKey() == isiFile.getPrimaryKey());

			if (documentsMatch && isiFilesMatch) {
				return documentOccurrence;
			}
		}

		return null;
	}

	public static void checkDocumentOccurrence(DocumentOccurrence documentOccurrence) {
	}
}
