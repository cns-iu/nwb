package edu.iu.sci2.database.isi.load.model.entity.relationship;

import java.util.Dictionary;
import java.util.Hashtable;

import org.cishell.utilities.dictionary.DictionaryEntry;
import org.cishell.utilities.dictionary.DictionaryUtilities;

import edu.iu.cns.database.load.framework.DerbyFieldType;
import edu.iu.cns.database.load.framework.RowItem;
import edu.iu.cns.database.load.framework.Schema;
import edu.iu.nwb.shared.isiutil.database.ISI;
import edu.iu.sci2.database.isi.load.model.entity.Address;
import edu.iu.sci2.database.isi.load.model.entity.Document;

public class ResearchAddress extends RowItem<ResearchAddress> {
	public static final Schema<ResearchAddress> SCHEMA = new Schema<ResearchAddress>(
		false,
		ISI.RESEARCH_ADDRESSES_DOCUMENT_FOREIGN_KEY, DerbyFieldType.FOREIGN_KEY,
		ISI.RESEARCH_ADDRESSES_ADDRESS_FOREIGN_KEY, DerbyFieldType.FOREIGN_KEY,
		ISI.ORDER_LISTED, DerbyFieldType.INTEGER).
		FOREIGN_KEYS(
			ISI.RESEARCH_ADDRESSES_DOCUMENT_FOREIGN_KEY, ISI.DOCUMENT_TABLE_NAME,
			ISI.RESEARCH_ADDRESSES_ADDRESS_FOREIGN_KEY, ISI.ADDRESS_TABLE_NAME);

	private Document document;
	private Address address;
	private Integer orderListed;

	public ResearchAddress(Document document, Address address, Integer orderListed) {
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

	public Integer getOrderListed() {
		return this.orderListed;
	}

	public static Dictionary<String, Object> createAttributes(
			Document document, Address address, Integer orderListed) {
		Dictionary<String, Object> attributes = new Hashtable<String, Object>();
		attributes.put(
				ISI.RESEARCH_ADDRESSES_DOCUMENT_FOREIGN_KEY, document.getPrimaryKey());
		attributes.put(
				ISI.RESEARCH_ADDRESSES_ADDRESS_FOREIGN_KEY, address.getPrimaryKey());
		DictionaryUtilities.addIfNotNull(
			attributes,
			new DictionaryEntry<String, Object>(ISI.ORDER_LISTED, orderListed));

		return attributes;
	}
}