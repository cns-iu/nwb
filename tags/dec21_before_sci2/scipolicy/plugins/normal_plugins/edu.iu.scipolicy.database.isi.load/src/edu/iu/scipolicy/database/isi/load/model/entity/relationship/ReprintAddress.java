package edu.iu.scipolicy.database.isi.load.model.entity.relationship;

import java.util.Dictionary;
import java.util.Hashtable;

import edu.iu.cns.database.load.framework.DerbyFieldType;
import edu.iu.cns.database.load.framework.RowItem;
import edu.iu.cns.database.load.framework.Schema;
import edu.iu.nwb.shared.isiutil.database.ISI;
import edu.iu.scipolicy.database.isi.load.model.entity.Address;
import edu.iu.scipolicy.database.isi.load.model.entity.Document;

public class ReprintAddress extends RowItem<ReprintAddress> {
	public static final Schema<ReprintAddress> SCHEMA = new Schema<ReprintAddress>(
		false,
		ISI.REPRINT_ADDRESSES_DOCUMENT_FOREIGN_KEY, DerbyFieldType.FOREIGN_KEY,
		ISI.REPRINT_ADDRESSES_ADDRESS_FOREIGN_KEY, DerbyFieldType.FOREIGN_KEY).
		FOREIGN_KEYS(
			ISI.REPRINT_ADDRESSES_DOCUMENT_FOREIGN_KEY, ISI.DOCUMENT_TABLE_NAME,
			ISI.REPRINT_ADDRESSES_ADDRESS_FOREIGN_KEY, ISI.ADDRESS_TABLE_NAME);

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

	public static Dictionary<String, Object> createAttributes(
			Document document, Address address) {
		Dictionary<String, Object> attributes = new Hashtable<String, Object>();
		attributes.put(
			ISI.REPRINT_ADDRESSES_DOCUMENT_FOREIGN_KEY, document.getPrimaryKey());
		attributes.put(
			ISI.REPRINT_ADDRESSES_ADDRESS_FOREIGN_KEY, address.getPrimaryKey());

		return attributes;
	}
}