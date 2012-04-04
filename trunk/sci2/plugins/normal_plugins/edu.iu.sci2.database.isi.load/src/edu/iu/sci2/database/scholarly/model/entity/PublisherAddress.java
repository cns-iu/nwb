package edu.iu.sci2.database.scholarly.model.entity;

import static edu.iu.sci2.database.scopus.load.EntityUtils.putPK;

import java.util.Dictionary;
import java.util.Hashtable;

import edu.iu.cns.database.load.framework.DBField;
import edu.iu.cns.database.load.framework.DerbyFieldType;
import edu.iu.cns.database.load.framework.RowItem;
import edu.iu.cns.database.load.framework.Schema;
import edu.iu.nwb.shared.isiutil.database.ISI;

public class PublisherAddress extends RowItem<PublisherAddress> {
	public static enum Field implements DBField {
		PUBLISHER_ADDRESS_ADDRESS_FK,
		PUBLISHER_ADDRESS_PUBLISHER_FK;

		public DerbyFieldType type() {
			return DerbyFieldType.FOREIGN_KEY;
		}
	}
	
	public static final Schema<PublisherAddress> SCHEMA = new Schema<PublisherAddress>(false, Field.values())
			.FOREIGN_KEYS(
					Field.PUBLISHER_ADDRESS_ADDRESS_FK.name(), ISI.ADDRESS_TABLE_NAME,
					Field.PUBLISHER_ADDRESS_PUBLISHER_FK.name(), ISI.PUBLISHER_TABLE_NAME);

	public PublisherAddress(Dictionary<String, Object> attributes) {
		super(attributes);
	}

	public static PublisherAddress link(
			Publisher publisher, Address address) {
		Dictionary<String, Object> attribs = new Hashtable<String, Object>();
		
		putPK(attribs, Field.PUBLISHER_ADDRESS_ADDRESS_FK, publisher);
		putPK(attribs, Field.PUBLISHER_ADDRESS_PUBLISHER_FK, address);
		
		return new PublisherAddress(attribs);
	}
}
