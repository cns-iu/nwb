package edu.iu.sci2.database.scholarly.model.entity;

import java.util.Dictionary;

import edu.iu.cns.database.load.framework.DBField;
import edu.iu.cns.database.load.framework.DerbyFieldType;
import edu.iu.cns.database.load.framework.RowItem;
import edu.iu.cns.database.load.framework.Schema;
import edu.iu.nwb.shared.isiutil.database.ISI;
import static edu.iu.sci2.database.scopus.load.EntityUtils.*;

public class ReprintAddress extends RowItem<ReprintAddress> {
	public ReprintAddress(Dictionary<String, Object> attributes) {
		super(attributes);
	}

	public static enum Field implements DBField {
		DOCUMENT_ID(DerbyFieldType.FOREIGN_KEY),
		ADDRESS_ID(DerbyFieldType.FOREIGN_KEY);
		
		private Field(DerbyFieldType type) {
			this.type = type;
		}

		private final DerbyFieldType type;
		
		@Override
		public DerbyFieldType type() {
			return this.type;
		}
	}

	public static final Schema<ReprintAddress> SCHEMA = new Schema<ReprintAddress>(
			false, Field.values()).FOREIGN_KEYS(
			Field.DOCUMENT_ID.name(), ISI.DOCUMENT_TABLE_NAME,
			Field.ADDRESS_ID.name(), ISI.ADDRESS_TABLE_NAME);
	
	public static ReprintAddress link(Document doc, Address address) {
		Dictionary<String, Object> attribs = newDictionary();
		putPK(attribs, Field.ADDRESS_ID, address);
		putPK(attribs, Field.DOCUMENT_ID, doc);
		
		return new ReprintAddress(attribs);
	}

}
