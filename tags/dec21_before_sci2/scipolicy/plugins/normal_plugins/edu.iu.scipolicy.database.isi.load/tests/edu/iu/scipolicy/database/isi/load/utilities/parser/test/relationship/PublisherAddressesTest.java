package edu.iu.scipolicy.database.isi.load.utilities.parser.test.relationship;


import java.util.Collection;

import edu.iu.scipolicy.database.isi.load.model.entity.Address;
import edu.iu.scipolicy.database.isi.load.model.entity.Publisher;
import edu.iu.scipolicy.database.isi.load.model.entity.relationship.PublisherAddress;
import edu.iu.scipolicy.database.isi.load.utilities.parser.RowItemTest;

public class PublisherAddressesTest extends RowItemTest {
	public static PublisherAddress getPublisherAddress(
			Collection<PublisherAddress> publisherAddresses,
			Publisher publisher,
			Address address) {
		for (PublisherAddress publisherAddress : publisherAddresses) {
			boolean documentsMatch =
				(publisherAddress.getPublisher().getPrimaryKey() == publisher.getPrimaryKey());
			boolean addressesMatch =
				(publisherAddress.getAddress().getPrimaryKey() == address.getPrimaryKey());

			if (documentsMatch && addressesMatch) {
				return publisherAddress;
			}
		}

		return null;
	}
}
