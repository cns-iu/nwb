package edu.iu.sci2.database.scholarly.model.entity;

import java.util.Dictionary;

import edu.iu.cns.database.load.framework.DBField;
import edu.iu.cns.database.load.framework.DerbyFieldType;
import edu.iu.cns.database.load.framework.RowItem;
import edu.iu.cns.database.load.framework.Schema;
import edu.iu.nwb.shared.isiutil.database.ISI;
import static edu.iu.sci2.database.scopus.load.EntityUtils.*;

public class ResearchAddress extends RowItem<ResearchAddress> {
	public ResearchAddress(Dictionary<String, Object> attributes) {
		super(attributes);
	}

	public static enum Field implements DBField {
		RESEARCH_ADDRESS_DOCUMENT_FK(DerbyFieldType.FOREIGN_KEY),
		RESEARCH_ADDRESS_ADDRESS_FK(DerbyFieldType.FOREIGN_KEY);
		
		private Field(DerbyFieldType type) {
			this.type = type;
		}

		private final DerbyFieldType type;
		
		public DerbyFieldType type() {
			return this.type;
		}
	}

	public static final Schema<ResearchAddress> SCHEMA = new Schema<ResearchAddress>(
			false, Field.values()).FOREIGN_KEYS(
			Field.RESEARCH_ADDRESS_DOCUMENT_FK.name(), ISI.DOCUMENT_TABLE_NAME,
			Field.RESEARCH_ADDRESS_ADDRESS_FK.name(), ISI.ADDRESS_TABLE_NAME);
	
	public static ResearchAddress link(Document doc, Address address) {
		Dictionary<String, Object> attribs = newDictionary();
		putPK(attribs, Field.RESEARCH_ADDRESS_ADDRESS_FK, address);
		putPK(attribs, Field.RESEARCH_ADDRESS_DOCUMENT_FK, doc);
		
		return new ResearchAddress(attribs);
	}

}
