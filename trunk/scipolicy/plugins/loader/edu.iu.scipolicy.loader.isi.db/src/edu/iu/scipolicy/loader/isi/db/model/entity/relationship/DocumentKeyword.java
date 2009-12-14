package edu.iu.scipolicy.loader.isi.db.model.entity.relationship;

import java.util.Dictionary;
import java.util.Hashtable;

import edu.iu.cns.database.loader.framework.EntityRelationship;
import edu.iu.scipolicy.loader.isi.db.ISIDatabase;
import edu.iu.scipolicy.loader.isi.db.model.entity.Document;
import edu.iu.scipolicy.loader.isi.db.model.entity.Keyword;

public class DocumentKeyword extends EntityRelationship<Document, Keyword> {
	private int orderListed;

	public DocumentKeyword(Document document, Keyword keyword, int orderListed) {
		super(document, keyword);
		this.orderListed = orderListed;
	}

	public Document getDocument() {
		return getLeftEntity();
	}

	public Keyword getKeyword() {
		return getRightEntity();
	}

	public int getOrderListed() {
		return this.orderListed;
	}

	public Dictionary<String, Object> createAttributes() {
		Dictionary<String, Object> attributes = new Hashtable<String, Object>();
		attributes.put(ISIDatabase.ORDER_LISTED_KEY, this.orderListed);

		return attributes;
	}
}