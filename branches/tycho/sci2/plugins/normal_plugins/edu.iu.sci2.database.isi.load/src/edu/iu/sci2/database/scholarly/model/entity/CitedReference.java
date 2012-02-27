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
		CITED_REFERENCES_DOCUMENT_FK,
		CITED_REFERENCES_REFERENCE_FK;

		public DerbyFieldType type() {
			return DerbyFieldType.FOREIGN_KEY;
		}
	}
	
	public static final Schema<CitedReference> SCHEMA = new Schema<CitedReference>(false, Field.values())
			.FOREIGN_KEYS(
					Field.CITED_REFERENCES_DOCUMENT_FK.name(), ISI.DOCUMENT_TABLE_NAME,
					Field.CITED_REFERENCES_REFERENCE_FK.name(), ISI.REFERENCE_TABLE_NAME);

	public CitedReference(Dictionary<String, Object> attributes) {
		super(attributes);
	}

	public static Iterable<CitedReference> makeCitedReferences(
			Document document, List<Reference> references) {
		List<CitedReference> citedReferences = Lists.newArrayList();
		
		for (Reference r: references) {
			Dictionary<String, Object> attribs = new Hashtable<String, Object>();
			putPK(attribs, Field.CITED_REFERENCES_DOCUMENT_FK, document);
			putPK(attribs, Field.CITED_REFERENCES_REFERENCE_FK, r);
			
			citedReferences.add(new CitedReference(attribs));
		}
		
		return citedReferences;
	}
}
