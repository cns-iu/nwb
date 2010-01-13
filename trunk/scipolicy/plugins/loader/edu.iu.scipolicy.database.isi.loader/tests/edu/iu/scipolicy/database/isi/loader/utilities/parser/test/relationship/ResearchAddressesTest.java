package edu.iu.scipolicy.database.isi.loader.utilities.parser.test.relationship;


import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;

import edu.iu.scipolicy.database.isi.loader.model.entity.Address;
import edu.iu.scipolicy.database.isi.loader.model.entity.Document;
import edu.iu.scipolicy.database.isi.loader.model.entity.relationship.ResearchAddress;
import edu.iu.scipolicy.database.isi.loader.utilities.parser.RowItemTest;

public class ResearchAddressesTest extends RowItemTest {
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
