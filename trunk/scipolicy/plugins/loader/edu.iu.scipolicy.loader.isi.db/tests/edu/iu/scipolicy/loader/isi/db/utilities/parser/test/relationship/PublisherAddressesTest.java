package edu.iu.scipolicy.loader.isi.db.utilities.parser.test.relationship;


import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;

import edu.iu.scipolicy.loader.isi.db.model.entity.Address;
import edu.iu.scipolicy.loader.isi.db.model.entity.Publisher;
import edu.iu.scipolicy.loader.isi.db.model.entity.relationship.PublisherAddress;
import edu.iu.scipolicy.loader.isi.db.utilities.parser.RowItemTest;

public class PublisherAddressesTest extends RowItemTest {
	public static PublisherAddress getPublisherAddress(
			List<PublisherAddress> publisherAddresses, Publisher publisher, Address address) {
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
