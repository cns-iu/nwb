package edu.iu.scipolicy.database.isi.load.utilities.parser.test.relationship;


import java.util.Collection;

import edu.iu.scipolicy.database.isi.load.model.entity.Address;
import edu.iu.scipolicy.database.isi.load.model.entity.Document;
import edu.iu.scipolicy.database.isi.load.model.entity.relationship.ReprintAddress;
import edu.iu.scipolicy.database.isi.load.utilities.parser.RowItemTest;

public class ReprintAddressesTest extends RowItemTest {
	public static ReprintAddress getReprintAddress(
			Collection<ReprintAddress> reprintAddresses, Document document, Address address) {
		for (ReprintAddress reprintAddress : reprintAddresses) {
			boolean documentsMatch =
				(reprintAddress.getDocument().getPrimaryKey() == document.getPrimaryKey());
			boolean addressesMatch =
				(reprintAddress.getAddress().getPrimaryKey() == address.getPrimaryKey());

			if (documentsMatch && addressesMatch) {
				return reprintAddress;
			}
		}

		return null;
	}
}
