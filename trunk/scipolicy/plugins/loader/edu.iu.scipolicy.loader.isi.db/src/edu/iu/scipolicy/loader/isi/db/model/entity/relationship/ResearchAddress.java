package edu.iu.scipolicy.loader.isi.db.model.entity.relationship;

import java.util.Dictionary;
import java.util.Hashtable;

import edu.iu.cns.database.loader.framework.RowItem;
import edu.iu.scipolicy.loader.isi.db.ISIDatabase;
import edu.iu.scipolicy.loader.isi.db.model.entity.Address;
import edu.iu.scipolicy.loader.isi.db.model.entity.Document;

public class ResearchAddress extends RowItem<ResearchAddress> {
	private Document document;
	private Address address;
	private int orderListed;

	public ResearchAddress(Document document, Address address, int orderListed) {
		super(createAttributes(orderListed));
		this.document = document;
		this.address = address;
		this.orderListed = orderListed;
	}

	public Document getDocument() {
		return this.document;
	}

	public Address getAddress() {
		return this.address;
	}

	public int getOrderListed() {
		return this.orderListed;
	}

	public ResearchAddress merge(ResearchAddress otherResearchAddress) {
		return otherResearchAddress;
	}

	public static Dictionary<String, Comparable<?>> createAttributes(int orderListed) {
		Dictionary<String, Comparable<?>> attributes = new Hashtable<String, Comparable<?>>();
		attributes.put(ISIDatabase.ORDER_LISTED, orderListed);

		return attributes;
	}
}