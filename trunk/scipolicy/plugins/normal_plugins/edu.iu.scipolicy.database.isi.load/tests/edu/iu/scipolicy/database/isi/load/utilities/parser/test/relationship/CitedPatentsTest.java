package edu.iu.scipolicy.database.isi.load.utilities.parser.test.relationship;


import static org.junit.Assert.fail;

import java.util.Collection;

import edu.iu.scipolicy.database.isi.load.model.entity.Document;
import edu.iu.scipolicy.database.isi.load.model.entity.Patent;
import edu.iu.scipolicy.database.isi.load.model.entity.relationship.CitedPatent;
import edu.iu.scipolicy.database.isi.load.utilities.parser.RowItemTest;

public class CitedPatentsTest extends RowItemTest {
	public static CitedPatent getCitedPatent(
			Collection<CitedPatent> citedPatents, Document document, Patent patent) {
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
