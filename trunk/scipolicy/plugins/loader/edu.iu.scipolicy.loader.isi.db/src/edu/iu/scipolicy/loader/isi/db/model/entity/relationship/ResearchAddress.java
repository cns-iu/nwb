package edu.iu.scipolicy.loader.isi.db.model.entity.relationship;

import java.util.Dictionary;
import java.util.Hashtable;

import edu.iu.cns.database.loader.framework.RowItem;
import edu.iu.cns.database.loader.framework.Schema;
import edu.iu.nwb.shared.isiutil.database.ISIDatabase;
import edu.iu.scipolicy.loader.isi.db.model.entity.Address;
import edu.iu.scipolicy.loader.isi.db.model.entity.Document;

public class ResearchAddress extends RowItem<ResearchAddress> {
	public static final Schema<ResearchAddress> SCHEMA = new Schema<ResearchAddress>(
		false,
		ISIDatabase.RESEARCH_ADDRESSES_PUBLISHER_FOREIGN_KEY, Schema.FOREIGN_KEY_CLASS,
		ISIDatabase.RESEARCH_ADDRESSES_ADDRESS_FOREIGN_KEY, Schema.FOREIGN_KEY_CLASS,
		ISIDatabase.ORDER_LISTED, Schema.INTEGER_CLASS).
		FOREIGN_KEYS(
			ISIDatabase.RESEARCH_ADDRESSES_PUBLISHER_FOREIGN_KEY, ISIDatabase.DOCUMENT_TABLE_NAME,
			ISIDatabase.RESEARCH_ADDRESSES_ADDRESS_FOREIGN_KEY, ISIDatabase.ADDRESS_TABLE_NAME);

	private Document document;
	private Address address;
	private int orderListed;

	public ResearchAddress(Document document, Address address, int orderListed) {
		super(createAttributes(document, address, orderListed));
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

	public boolean shouldMerge(ResearchAddress otherResearchAddress) {
		return false;
	}

	public void merge(ResearchAddress otherResearchAddress) {
	}

	public static Dictionary<String, Comparable<?>> createAttributes(
			Document document, Address address, int orderListed) {
		Dictionary<String, Comparable<?>> attributes = new Hashtable<String, Comparable<?>>();
		attributes.put(ISIDatabase.REPRINT_ADDRESSES_PUBLISHER_FOREIGN_KEY, document);
		attributes.put(ISIDatabase.REPRINT_ADDRESSES_ADDRESS_FOREIGN_KEY, address);
		attributes.put(ISIDatabase.ORDER_LISTED, orderListed);

		return attributes;
	}
}