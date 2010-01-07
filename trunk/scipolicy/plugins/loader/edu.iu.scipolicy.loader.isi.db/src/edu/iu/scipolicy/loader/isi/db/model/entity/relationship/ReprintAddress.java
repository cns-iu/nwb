package edu.iu.scipolicy.loader.isi.db.model.entity.relationship;

import java.util.Dictionary;
import java.util.Hashtable;

import edu.iu.cns.database.loader.framework.RowItem;
import edu.iu.cns.database.loader.framework.Schema;
import edu.iu.cns.database.loader.framework.DerbyFieldType;
import edu.iu.nwb.shared.isiutil.database.ISIDatabase;
import edu.iu.scipolicy.loader.isi.db.model.entity.Address;
import edu.iu.scipolicy.loader.isi.db.model.entity.Document;

public class ReprintAddress extends RowItem<ReprintAddress> {
	public static final Schema<ReprintAddress> SCHEMA = new Schema<ReprintAddress>(
		false,
		ISIDatabase.REPRINT_ADDRESSES_PUBLISHER_FOREIGN_KEY, DerbyFieldType.FOREIGN_KEY,
		ISIDatabase.REPRINT_ADDRESSES_ADDRESS_FOREIGN_KEY, DerbyFieldType.FOREIGN_KEY).
		FOREIGN_KEYS(
			ISIDatabase.REPRINT_ADDRESSES_PUBLISHER_FOREIGN_KEY, ISIDatabase.DOCUMENT_TABLE_NAME,
			ISIDatabase.REPRINT_ADDRESSES_ADDRESS_FOREIGN_KEY, ISIDatabase.ADDRESS_TABLE_NAME);

	private Document document;
	private Address address;

	public ReprintAddress(Document document, Address address) {
		super(createAttributes(document, address));
		this.document = document;
		this.address = address;
	}

	public Document getDocument() {
		return this.document;
	}

	public Address getAddress() {
		return this.address;
	}

	public boolean shouldMerge(ReprintAddress otherReprintAddress) {
		return false;
	}

	public void merge(ReprintAddress otherReprintAddress) {
	}

	public static Dictionary<String, Comparable<?>> createAttributes(
			Document document, Address address) {
		Dictionary<String, Comparable<?>> attributes = new Hashtable<String, Comparable<?>>();
		attributes.put(
			ISIDatabase.REPRINT_ADDRESSES_PUBLISHER_FOREIGN_KEY, document.getPrimaryKey());
		attributes.put(
			ISIDatabase.REPRINT_ADDRESSES_ADDRESS_FOREIGN_KEY, address.getPrimaryKey());

		return attributes;
	}
}