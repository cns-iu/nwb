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
		super(document, keyword, createAttributes(orderListed));
		this.orderListed = orderListed;
	}

	public Document getDocument() {
		return getFromPrimaryKeyContainer();
	}

	public Keyword getKeyword() {
		return getToPrimaryKeyContainer();
	}

	public int getOrderListed() {
		return this.orderListed;
	}

	public static Dictionary<String, Object> createAttributes(int orderListed) {
		Dictionary<String, Object> attributes = new Hashtable<String, Object>();
		attributes.put(ISIDatabase.ORDER_LISTED, orderListed);

		return attributes;
	}
}