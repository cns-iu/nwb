package edu.iu.sci2.database.isi.load.utilities.parser.test.relationship;


import java.util.Collection;

import edu.iu.sci2.database.isi.load.model.entity.Address;
import edu.iu.sci2.database.isi.load.model.entity.Document;
import edu.iu.sci2.database.isi.load.model.entity.relationship.ResearchAddress;
import edu.iu.sci2.database.isi.load.utilities.parser.RowItemTest;

public class ResearchAddressesTest extends RowItemTest {
	public static ResearchAddress getResearchAddress(
			Collection<ResearchAddress> researchAddresses, Document document, Address address) {
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
