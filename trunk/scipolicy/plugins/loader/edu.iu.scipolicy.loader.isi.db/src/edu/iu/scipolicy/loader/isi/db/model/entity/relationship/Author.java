package edu.iu.scipolicy.loader.isi.db.model.entity.relationship;

import java.util.Dictionary;
import java.util.Hashtable;

import edu.iu.cns.database.loader.framework.EntityRelationship;
import edu.iu.scipolicy.loader.isi.db.ISIDatabase;
import edu.iu.scipolicy.loader.isi.db.model.entity.Document;
import edu.iu.scipolicy.loader.isi.db.model.entity.Person;

public class Author extends EntityRelationship<Document, Person> {
	private int orderListed;

	public Author(Document document, Person person, int orderListed) {
		super(document, person, createAttributes(orderListed));
		this.orderListed = orderListed;
	}

	public Document getDocument() {
		return getFromPrimaryKeyContainer();
	}

	public Person getPerson() {
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