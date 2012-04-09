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
		ADDRESS_ID,
		PUBLISHER_ID;

		@Override
		public DerbyFieldType type() {
			return DerbyFieldType.FOREIGN_KEY;
		}
	}
	
	public static final Schema<PublisherAddress> SCHEMA = new Schema<PublisherAddress>(false, Field.values())
			.FOREIGN_KEYS(
					Field.ADDRESS_ID.name(), ISI.ADDRESS_TABLE_NAME,
					Field.PUBLISHER_ID.name(), ISI.PUBLISHER_TABLE_NAME);

	public PublisherAddress(Dictionary<String, Object> attributes) {
		super(attributes);
	}

	public static PublisherAddress link(
			Publisher publisher, Address address) {
		Dictionary<String, Object> attribs = new Hashtable<String, Object>();
		
		putPK(attribs, Field.ADDRESS_ID, publisher);
		putPK(attribs, Field.PUBLISHER_ID, address);
		
		return new PublisherAddress(attribs);
	}
}
