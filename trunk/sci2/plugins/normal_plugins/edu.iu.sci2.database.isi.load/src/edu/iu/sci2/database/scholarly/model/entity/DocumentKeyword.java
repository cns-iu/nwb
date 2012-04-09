package edu.iu.sci2.database.scholarly.model.entity;

import static edu.iu.sci2.database.scopus.load.EntityUtils.putPK;
import static edu.iu.sci2.database.scopus.load.EntityUtils.putValue;

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
		DOCUMENT_ID(DerbyFieldType.FOREIGN_KEY),
		KEYWORD_ID(DerbyFieldType.FOREIGN_KEY),
		ORDER_LISTED(DerbyFieldType.INTEGER);
		
		private final DerbyFieldType fieldType;
		private Field(DerbyFieldType type) {
			this.fieldType = type;
		}
		@Override
		public DerbyFieldType type() {
			return this.fieldType;
		}
	}
	
	public static final Schema<DocumentKeyword> SCHEMA = new Schema<DocumentKeyword>(false, Field.values())
			.FOREIGN_KEYS(
					Field.DOCUMENT_ID.name(), ISI.DOCUMENT_TABLE_NAME,
					Field.KEYWORD_ID.name(), ISI.KEYWORD_TABLE_NAME);

	public DocumentKeyword(Dictionary<String, Object> attributes) {
		super(attributes);
	}

	public static Iterable<DocumentKeyword> makeDocumentKeywords(
			Document document, List<Keyword> keywords) {
		List<DocumentKeyword> documentKeywords = Lists.newArrayList();
		int keywordNumber = 1;
		
		for (Keyword k : keywords) {
			Dictionary<String, Object> attribs = new Hashtable<String, Object>();
			
			putPK(attribs, Field.DOCUMENT_ID, document);
			putPK(attribs, Field.KEYWORD_ID, k);
			putValue(attribs, Field.ORDER_LISTED, keywordNumber++);
			
			documentKeywords.add(new DocumentKeyword(attribs));
		}
		
		return documentKeywords;
	}
}
