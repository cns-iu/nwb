package edu.iu.sci2.database.isi.load.model.entity.relationship;

import java.util.Dictionary;
import java.util.Hashtable;

import org.cishell.utilities.dictionary.DictionaryEntry;
import org.cishell.utilities.dictionary.DictionaryUtilities;

import edu.iu.cns.database.load.framework.DerbyFieldType;
import edu.iu.cns.database.load.framework.RowItem;
import edu.iu.cns.database.load.framework.Schema;
import edu.iu.nwb.shared.isiutil.database.ISI;
import edu.iu.sci2.database.isi.load.model.entity.Document;
import edu.iu.sci2.database.isi.load.model.entity.Keyword;

public class DocumentKeyword extends RowItem<DocumentKeyword> {
	public static final Schema<DocumentKeyword> SCHEMA = new Schema<DocumentKeyword>(
		false,
		ISI.DOCUMENT_KEYWORDS_DOCUMENT_FOREIGN_KEY, DerbyFieldType.FOREIGN_KEY,
		ISI.DOCUMENT_KEYWORDS_KEYWORD_FOREIGN_KEY, DerbyFieldType.FOREIGN_KEY,
		ISI.ORDER_LISTED, DerbyFieldType.INTEGER).
		FOREIGN_KEYS(
			ISI.DOCUMENT_KEYWORDS_DOCUMENT_FOREIGN_KEY, ISI.DOCUMENT_TABLE_NAME,
			ISI.DOCUMENT_KEYWORDS_KEYWORD_FOREIGN_KEY, ISI.KEYWORD_TABLE_NAME);

	private Document document;
	private Keyword keyword;
	private Integer orderListed;

	public DocumentKeyword(Document document, Keyword keyword, Integer orderListed) {
		super(createAttributes(document, keyword, orderListed));
		this.document = document;
		this.keyword = keyword;
		this.orderListed = orderListed;
	}

	public Document getDocument() {
		return this.document;
	}

	public Keyword getKeyword() {
		return this.keyword;
	}

	public Integer getOrderListed() {
		return this.orderListed;
	}

	public static Dictionary<String, Object> createAttributes(
			Document document, Keyword keyword, Integer orderListed) {
		Dictionary<String, Object> attributes = new Hashtable<String, Object>();
		attributes.put(ISI.DOCUMENT_KEYWORDS_DOCUMENT_FOREIGN_KEY, document.getPrimaryKey());
		attributes.put(ISI.DOCUMENT_KEYWORDS_KEYWORD_FOREIGN_KEY, keyword.getPrimaryKey());
		DictionaryUtilities.addIfNotNull(
			attributes,
			new DictionaryEntry<String, Object>(ISI.ORDER_LISTED, orderListed));

		return attributes;
	}
}