package edu.iu.sci2.database.scholarly.model.entity;

import static edu.iu.sci2.database.scopus.load.EntityUtils.newDictionary;
import static edu.iu.sci2.database.scopus.load.EntityUtils.putPK;
import static edu.iu.sci2.database.scopus.load.EntityUtils.putValue;

import java.util.Dictionary;
import java.util.List;

import com.google.common.collect.Lists;

import edu.iu.cns.database.load.framework.DBField;
import edu.iu.cns.database.load.framework.DerbyFieldType;
import edu.iu.cns.database.load.framework.RowItem;
import edu.iu.cns.database.load.framework.Schema;
import edu.iu.nwb.shared.isiutil.database.ISI;

public class ResearchAddress extends RowItem<ResearchAddress> {
	public ResearchAddress(Dictionary<String, Object> attributes) {
		super(attributes);
	}

	public static enum Field implements DBField {
		DOCUMENT_ID(DerbyFieldType.FOREIGN_KEY),
		ADDRESS_ID(DerbyFieldType.FOREIGN_KEY),
		ORDER_LISTED(DerbyFieldType.INTEGER);
		
		private Field(DerbyFieldType type) {
			this.type = type;
		}

		private final DerbyFieldType type;
		
		@Override
		public DerbyFieldType type() {
			return this.type;
		}
	}

	public static final Schema<ResearchAddress> SCHEMA = new Schema<ResearchAddress>(
			false, Field.values()).FOREIGN_KEYS(
			Field.DOCUMENT_ID.name(), ISI.DOCUMENT_TABLE_NAME,
			Field.ADDRESS_ID.name(), ISI.ADDRESS_TABLE_NAME);
	
	public static List<ResearchAddress> makeResearchAddresses(Document doc, List<Address> addresses) {
		List<ResearchAddress> researchAddresses = Lists.newArrayList();
		int sequence = 1;
		
		for (Address address : addresses) {
			Dictionary<String, Object> attribs = newDictionary();
			putPK(attribs, Field.ADDRESS_ID, address);
			putPK(attribs, Field.DOCUMENT_ID, doc);
			putValue(attribs, Field.ORDER_LISTED, sequence++);
			
			researchAddresses.add(new ResearchAddress(attribs));
		}
		
		return researchAddresses;
	}

}
