package edu.iu.scipolicy.loader.isi.db.utilities.parser.test.relationship;


import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;

import edu.iu.scipolicy.loader.isi.db.model.entity.Address;
import edu.iu.scipolicy.loader.isi.db.model.entity.Document;
import edu.iu.scipolicy.loader.isi.db.model.entity.relationship.ReprintAddress;
import edu.iu.scipolicy.loader.isi.db.utilities.parser.RowItemTest;

public class ReprintAddressesTest extends RowItemTest {
	public static ReprintAddress getReprintAddress(
			List<ReprintAddress> reprintAddresses, Document document, Address address) {
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
