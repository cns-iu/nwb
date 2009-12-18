package edu.iu.scipolicy.loader.isi.db.model.entity.relationship;

import java.util.Dictionary;
import java.util.Hashtable;

import edu.iu.cns.database.loader.framework.RowItem;
import edu.iu.nwb.shared.isiutil.database.ISIDatabase;
import edu.iu.scipolicy.loader.isi.db.model.entity.Document;
import edu.iu.scipolicy.loader.isi.db.model.entity.Keyword;

public class DocumentKeyword extends RowItem<DocumentKeyword> {
	private Document document;
	private Keyword keyword;
	private int orderListed;

	public DocumentKeyword(Document document, Keyword keyword, int orderListed) {
		super(createAttributes(orderListed));
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

	public int getOrderListed() {
		return this.orderListed;
	}

	public DocumentKeyword merge(DocumentKeyword otherDocumentKeyword) {
		return otherDocumentKeyword;
	}

	public static Dictionary<String, Comparable<?>> createAttributes(int orderListed) {
		Dictionary<String, Comparable<?>> attributes = new Hashtable<String, Comparable<?>>();
		attributes.put(ISIDatabase.ORDER_LISTED, orderListed);

		return attributes;
	}
}