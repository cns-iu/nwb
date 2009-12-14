package edu.iu.scipolicy.loader.isi.db.model.entity.relationship;

import java.util.Dictionary;
import java.util.Hashtable;

import edu.iu.cns.database.loader.framework.EntityRelationship;
import edu.iu.scipolicy.loader.isi.db.ISIDatabase;
import edu.iu.scipolicy.loader.isi.db.model.entity.Document;
import edu.iu.scipolicy.loader.isi.db.model.entity.Person;

public class Editor extends EntityRelationship<Document, Person> {
	private int orderListed;

	public Editor(Document document, Person person, int orderListed) {
		super(document, person);
		this.orderListed = orderListed;
	}

	public Document getDocument() {
		return getLeftEntity();
	}

	public Person getPerson() {
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