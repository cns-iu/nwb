package edu.iu.scipolicy.loader.isi.db.model.entity.relationship;

import java.util.Dictionary;
import java.util.Hashtable;

import edu.iu.cns.database.loader.framework.RowItem;
import edu.iu.scipolicy.loader.isi.db.model.entity.Address;
import edu.iu.scipolicy.loader.isi.db.model.entity.Publisher;

public class PublisherAddress extends RowItem<PublisherAddress> {
	private Publisher publisher;
	private Address address;

	public PublisherAddress(Publisher publisher, Address address) {
		super(createAttributes());
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

	public static Dictionary<String, Comparable<?>> createAttributes() {
		return new Hashtable<String, Comparable<?>>();
	}
}