package edu.iu.sci2.database.isi.load.utilities.parser.test.relationship;


import static org.junit.Assert.fail;

import java.util.Collection;

import edu.iu.sci2.database.isi.load.model.entity.Document;
import edu.iu.sci2.database.isi.load.model.entity.Keyword;
import edu.iu.sci2.database.isi.load.model.entity.relationship.DocumentKeyword;
import edu.iu.sci2.database.isi.load.utilities.parser.RowItemTest;

public class DocumentKeywordsTest extends RowItemTest {
	public static DocumentKeyword getDocumentKeyword(
			Collection<DocumentKeyword> documentKeywords, Document document, Keyword keyword) {
		for (DocumentKeyword documentKeyword : documentKeywords) {
			boolean documentsMatch =
				(documentKeyword.getDocument().getPrimaryKey() == document.getPrimaryKey());
			boolean keywordsMatch =
				(documentKeyword.getKeyword().getPrimaryKey() == keyword.getPrimaryKey());

			if (documentsMatch && keywordsMatch) {
				return documentKeyword;
			}
		}

		return null;
	}

	public static void checkDocumentKeyword(
			DocumentKeyword documentKeyword, int providedOrderListed) {
		if (documentKeyword == null) {
			fail("Document keyword is null.");
		}

		if (documentKeyword.getDocument() == null) {
			fail("Document in DocumentKeyword is null.");
		}

		if (documentKeyword.getKeyword() == null) {
			fail("Keyword in DocumentKeyword is null.");
		}

		int documentKeywordOrderListed = documentKeyword.getOrderListed();

		if (documentKeywordOrderListed != providedOrderListed) {
			String failMessage =
				"Orders listed do not match." +
				"\n\tDocument keyword order listed: " + documentKeywordOrderListed +
				"\n\tProvided keyword order listed: " + providedOrderListed;
			fail(failMessage);
		}
	}
}
