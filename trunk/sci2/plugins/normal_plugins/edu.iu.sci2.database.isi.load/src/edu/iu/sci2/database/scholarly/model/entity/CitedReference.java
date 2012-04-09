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

public class CitedReference extends RowItem<CitedReference> {
	public static enum Field implements DBField {
		DOCUMENT_ID,
		CITATION_ID;

		@Override
		public DerbyFieldType type() {
			return DerbyFieldType.FOREIGN_KEY;
		}
	}
	
	public static final Schema<CitedReference> SCHEMA = new Schema<CitedReference>(false, Field.values())
			.FOREIGN_KEYS(
					Field.DOCUMENT_ID.name(), ISI.DOCUMENT_TABLE_NAME,
					Field.CITATION_ID.name(), ISI.REFERENCE_TABLE_NAME);

	public CitedReference(Dictionary<String, Object> attributes) {
		super(attributes);
	}

	public static Iterable<CitedReference> makeCitedReferences(
			Document document, List<Reference> references) {
		List<CitedReference> citedReferences = Lists.newArrayList();
		
		for (Reference r: references) {
			Dictionary<String, Object> attribs = new Hashtable<String, Object>();
			putPK(attribs, Field.DOCUMENT_ID, document);
			putPK(attribs, Field.CITATION_ID, r);
			
			citedReferences.add(new CitedReference(attribs));
		}
		
		return citedReferences;
	}
}
