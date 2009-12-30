package edu.iu.scipolicy.loader.isi.db.model.entity.relationship;

import java.util.Dictionary;
import java.util.Hashtable;

import edu.iu.cns.database.loader.framework.RowItem;
import edu.iu.cns.database.loader.framework.Schema;
import edu.iu.nwb.shared.isiutil.database.ISIDatabase;
import edu.iu.scipolicy.loader.isi.db.model.entity.Address;
import edu.iu.scipolicy.loader.isi.db.model.entity.Publisher;

public class PublisherAddress extends RowItem<PublisherAddress> {
	public static final Schema<PublisherAddress> SCHEMA = new Schema<PublisherAddress>(
		ISIDatabase.PUBLISHER_ADDRESSES_PUBLISHER_FOREIGN_KEY, Schema.FOREIGN_KEY_CLASS,
		ISIDatabase.PUBLISHER_ADDRESSES_ADDRESS_FOREIGN_KEY, Schema.FOREIGN_KEY_CLASS).
		FOREIGN_KEYS(
			ISIDatabase.PUBLISHER_ADDRESSES_PUBLISHER_FOREIGN_KEY,
				ISIDatabase.PUBLISHER_TABLE_NAME,
			ISIDatabase.PUBLISHER_ADDRESSES_ADDRESS_FOREIGN_KEY,
				ISIDatabase.ADDRESS_TABLE_NAME);

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
		return false;
	}

	public void merge(PublisherAddress otherPublisherAddress) {
	}

	public static Dictionary<String, Comparable<?>> createAttributes(
			Publisher publisher, Address address) {
		Dictionary<String, Comparable<?>> attributes = new Hashtable<String, Comparable<?>>();
		// TODO:
		/*attributes.put(ISIDatabase.PUBLISHER_ADDRESSES_PUBLISHER_FOREIGN_KEY, publisher);
		attributes.put(ISIDatabase.PUBLISHER_ADDRESSES_ADDRESS_FOREIGN_KEY, address);*/

		return attributes;
	}
}