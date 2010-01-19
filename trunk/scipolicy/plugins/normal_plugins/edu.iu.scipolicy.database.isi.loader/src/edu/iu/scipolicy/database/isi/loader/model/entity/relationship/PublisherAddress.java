package edu.iu.scipolicy.database.isi.loader.model.entity.relationship;

import java.util.Dictionary;
import java.util.Hashtable;

import edu.iu.cns.database.loader.framework.RowItem;
import edu.iu.cns.database.loader.framework.Schema;
import edu.iu.cns.database.loader.framework.DerbyFieldType;
import edu.iu.nwb.shared.isiutil.database.ISI;
import edu.iu.scipolicy.database.isi.loader.model.entity.Address;
import edu.iu.scipolicy.database.isi.loader.model.entity.Publisher;

public class PublisherAddress extends RowItem<PublisherAddress> {
	public static final Schema<PublisherAddress> SCHEMA = new Schema<PublisherAddress>(
		false,
		ISI.PUBLISHER_ADDRESSES_PUBLISHER_FOREIGN_KEY, DerbyFieldType.FOREIGN_KEY,
		ISI.PUBLISHER_ADDRESSES_ADDRESS_FOREIGN_KEY, DerbyFieldType.FOREIGN_KEY).
		FOREIGN_KEYS(
			ISI.PUBLISHER_ADDRESSES_PUBLISHER_FOREIGN_KEY,
				ISI.PUBLISHER_TABLE_NAME,
			ISI.PUBLISHER_ADDRESSES_ADDRESS_FOREIGN_KEY,
				ISI.ADDRESS_TABLE_NAME);

	private Publisher publisher;
	private Address address;

	public PublisherAddress(Publisher publisher, Address address) {
		super(createAttributes(publisher, address));
		this.publisher = publisher;
		this.address = address;
	}

	public Publisher getPublisher() {
		return this.publisher;
	}

	public Address getAddress() {
		return this.address;
	}

	public boolean shouldMerge(PublisherAddress otherPublisherAddress) {
		if ((this.publisher != null) && (this.address != null)) {
			Publisher otherPublisher = otherPublisherAddress.getPublisher();
			Address otherAddress = otherPublisherAddress.getAddress();

			if ((otherPublisher != null) && (otherAddress != null)) {
				return (
					(this.publisher.getPrimaryKey() == otherPublisher.getPrimaryKey()) &&
					(this.address.getPrimaryKey() == otherAddress.getPrimaryKey()));
			}
		}

		return false;
	}

	public void merge(PublisherAddress otherPublisherAddress) {
	}

	public static Dictionary<String, Comparable<?>> createAttributes(
			Publisher publisher, Address address) {
		Dictionary<String, Comparable<?>> attributes = new Hashtable<String, Comparable<?>>();
		attributes.put(ISI.PUBLISHER_ADDRESSES_PUBLISHER_FOREIGN_KEY,
				publisher.getPrimaryKey());
		attributes.put(ISI.PUBLISHER_ADDRESSES_ADDRESS_FOREIGN_KEY, address.getPrimaryKey());

		return attributes;
	}
}