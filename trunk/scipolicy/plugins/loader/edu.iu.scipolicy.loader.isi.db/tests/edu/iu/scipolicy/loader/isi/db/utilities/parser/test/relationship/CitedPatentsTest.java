package edu.iu.scipolicy.loader.isi.db.utilities.parser.test.relationship;


import static org.junit.Assert.fail;

import java.util.List;

import edu.iu.scipolicy.loader.isi.db.model.entity.Document;
import edu.iu.scipolicy.loader.isi.db.model.entity.Patent;
import edu.iu.scipolicy.loader.isi.db.model.entity.relationship.CitedPatent;
import edu.iu.scipolicy.loader.isi.db.utilities.parser.RowItemTest;

public class CitedPatentsTest extends RowItemTest {
	public static CitedPatent getCitedPatent(
			List<CitedPatent> citedPatents, Document document, Patent patent) {
		for (CitedPatent citedPatent : citedPatents) {
			boolean documentsMatch =
				(citedPatent.getDocument().getPrimaryKey() == document.getPrimaryKey());
			boolean patentsMatch =
				(citedPatent.getPatent().getPrimaryKey() == patent.getPrimaryKey());

			if (documentsMatch && patentsMatch) {
				return citedPatent;
			}
		}

		return null;
	}

	public static void checkCitedPatent(CitedPatent citedPatent) {
		if (citedPatent == null) {
			fail("Cited patent is null.");
		}

		if (citedPatent.getDocument() == null) {
			fail("Document in CitedPatent is null.");
		}

		if (citedPatent.getPatent() == null) {
			fail("Patent in CitedPatent is null.");
		}
	}
}
