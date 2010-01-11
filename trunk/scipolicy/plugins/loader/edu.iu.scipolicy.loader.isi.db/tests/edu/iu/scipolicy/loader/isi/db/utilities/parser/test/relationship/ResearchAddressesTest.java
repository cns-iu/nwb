package edu.iu.scipolicy.loader.isi.db.utilities.parser.test.relationship;


import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;

import edu.iu.scipolicy.loader.isi.db.model.entity.Address;
import edu.iu.scipolicy.loader.isi.db.model.entity.Document;
import edu.iu.scipolicy.loader.isi.db.model.entity.relationship.ResearchAddress;
import edu.iu.scipolicy.loader.isi.db.utilities.parser.RowItemTest;

public class ResearchAddressesTest extends RowItemTest {
	@Test
	public void test() {
		fail();
	}

	public static ResearchAddress getResearchAddress(
			List<ResearchAddress> researchAddresses, Document document, Address address) {
		for (ResearchAddress researchAddress : researchAddresses) {
			boolean documentsMatch =
				(researchAddress.getDocument().getPrimaryKey() == document.getPrimaryKey());
			boolean addressesMatch =
				(researchAddress.getAddress().getPrimaryKey() == address.getPrimaryKey());

			if (documentsMatch && addressesMatch) {
				return researchAddress;
			}
		}

		return null;
	}
}
