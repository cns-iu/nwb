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

public class DocumentKeyword extends RowItem<DocumentKeyword> {
	public static enum Field implements DBField {
		DOCUMENT_KEYWORDS_DOCUMENT_FK,
		DOCUMENT_KEYWORDS_KEYWORD_FK;

		public DerbyFieldType type() {
			return DerbyFieldType.FOREIGN_KEY;
		}
	}
	
	public static final Schema<DocumentKeyword> SCHEMA = new Schema<DocumentKeyword>(false, Field.values())
			.FOREIGN_KEYS(
					Field.DOCUMENT_KEYWORDS_DOCUMENT_FK.name(), ISI.DOCUMENT_TABLE_NAME,
					Field.DOCUMENT_KEYWORDS_KEYWORD_FK.name(), ISI.KEYWORD_TABLE_NAME);

	public DocumentKeyword(Dictionary<String, Object> attributes) {
		super(attributes);
	}

	public static Iterable<DocumentKeyword> makeDocumentKeywords(
			Document document, List<Keyword> keywords) {
		List<DocumentKeyword> documentKeywords = Lists.newArrayList();
		
		for (Keyword k : keywords) {
			Dictionary<String, Object> attribs = new Hashtable<String, Object>();
			putPK(attribs, Field.DOCUMENT_KEYWORDS_DOCUMENT_FK, document);
			putPK(attribs, Field.DOCUMENT_KEYWORDS_KEYWORD_FK, k);
			
			documentKeywords.add(new DocumentKeyword(attribs));
		}
		
		return documentKeywords;
	}
}
