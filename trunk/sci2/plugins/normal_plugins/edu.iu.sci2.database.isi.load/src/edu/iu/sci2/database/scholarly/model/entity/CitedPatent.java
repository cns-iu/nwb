package edu.iu.sci2.database.scholarly.model.entity;

import static edu.iu.sci2.database.scopus.load.EntityUtils.putPK;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import com.google.common.collect.Lists;

import edu.iu.cns.database.load.framework.DBField;
import edu.iu.cns.database.load.framework.DerbyFieldType;
import edu.iu.cns.database.load.framework.RowItem;
import edu.iu.cns.database.load.framework.Schema;
import edu.iu.nwb.shared.isiutil.database.ISI;

public class CitedPatent extends RowItem<CitedPatent> {
	public static enum Field implements DBField {
		DOCUMENT_ID,
		PATENT_ID;

		@Override
		public DerbyFieldType type() {
			return DerbyFieldType.FOREIGN_KEY;
		}
	}
	
	public static final Schema<CitedPatent> SCHEMA = new Schema<CitedPatent>(false, Field.values())
			.FOREIGN_KEYS(
					Field.DOCUMENT_ID.name(), ISI.DOCUMENT_TABLE_NAME,
					Field.PATENT_ID.name(), ISI.PATENT_TABLE_NAME);

	public CitedPatent(Dictionary<String, Object> attributes) {
		super(attributes);
	}

	public static Iterable<CitedPatent> makeCitedPatents(
			Document document, List<Patent> patents) {
		List<CitedPatent> citedPatents = Lists.newArrayList();
		
		for (Patent p: patents) {
			Dictionary<String, Object> attribs = new Hashtable<String, Object>();
			putPK(attribs, Field.DOCUMENT_ID, document);
			putPK(attribs, Field.PATENT_ID, p);
			
			citedPatents.add(new CitedPatent(attribs));
		}
		
		return citedPatents;
	}
}
