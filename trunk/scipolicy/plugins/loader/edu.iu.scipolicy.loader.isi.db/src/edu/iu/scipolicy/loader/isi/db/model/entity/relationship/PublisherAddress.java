package edu.iu.scipolicy.loader.isi.db.model.entity.relationship;

import java.util.Dictionary;
import java.util.Hashtable;

import edu.iu.cns.database.loader.framework.EntityRelationship;
import edu.iu.scipolicy.loader.isi.db.model.entity.Address;
import edu.iu.scipolicy.loader.isi.db.model.entity.Publisher;

public class PublisherAddress extends EntityRelationship<Publisher, Address> {
	public PublisherAddress(Publisher publisher, Address address) {
		super(publisher, address);
	}

	public Publisher getPublisher() {
		return getLeftEntity();
	}

	public Address getAddress() {
		return getRightEntity();
	}

	public Dictionary<String, Object> createAttributes() {
		return new Hashtable<String, Object>();
	}
}