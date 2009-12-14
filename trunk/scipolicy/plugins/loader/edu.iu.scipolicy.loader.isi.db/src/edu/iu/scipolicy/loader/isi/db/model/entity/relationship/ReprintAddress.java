package edu.iu.scipolicy.loader.isi.db.model.entity.relationship;

import java.util.Dictionary;
import java.util.Hashtable;

import edu.iu.cns.database.loader.framework.EntityRelationship;
import edu.iu.scipolicy.loader.isi.db.ISIDatabase;
import edu.iu.scipolicy.loader.isi.db.model.entity.Address;
import edu.iu.scipolicy.loader.isi.db.model.entity.Document;

public class ReprintAddress extends EntityRelationship<Document, Address> {
	private int orderListed;

	public ReprintAddress(Document document, Address address, int orderListed) {
		super(document, address);
	}

	public Document getDocument() {
		return getFromPrimaryKeyContainer();
	}

	public Address getAddress() {
		return getToPrimaryKeyContainer();
	}

	public int getOrderListed() {
		return this.orderListed;
	}

	public Dictionary<String, Object> createAttributes() {
		Dictionary<String, Object> attributes = new Hashtable<String, Object>();
		attributes.put(ISIDatabase.ORDER_LISTED, this.orderListed);

		return attributes;
	}
}