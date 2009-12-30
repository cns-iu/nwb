package edu.iu.scipolicy.loader.isi.db.model.entity.relationship;

import java.util.Dictionary;
import java.util.Hashtable;

import edu.iu.cns.database.loader.framework.RowItem;
import edu.iu.cns.database.loader.framework.Schema;
import edu.iu.nwb.shared.isiutil.database.ISIDatabase;
import edu.iu.scipolicy.loader.isi.db.model.entity.Document;
import edu.iu.scipolicy.loader.isi.db.model.entity.Person;

public class Author extends RowItem<Author> {
	public static final Schema<Author> SCHEMA = new Schema<Author>(
		ISIDatabase.AUTHORS_DOCUMENT_FOREIGN_KEY, Schema.FOREIGN_KEY_CLASS,
		ISIDatabase.AUTHORS_PERSON_FOREIGN_KEY, Schema.FOREIGN_KEY_CLASS,
		ISIDatabase.ORDER_LISTED, Schema.INTEGER_CLASS).
		FOREIGN_KEYS(
			ISIDatabase.AUTHORS_DOCUMENT_FOREIGN_KEY, ISIDatabase.DOCUMENT_TABLE_NAME,
			ISIDatabase.AUTHORS_PERSON_FOREIGN_KEY, ISIDatabase.PERSON_TABLE_NAME);

	private Document document;
	private Person person;
	private int orderListed;

	public Author(Document document, Person person, int orderListed) {
		super(createAttributes(orderListed));
		this.document = document;
		this.person = person;
		this.orderListed = orderListed;
	}

	public Document getDocument() {
		return this.document;
	}

	public Person getPerson() {
		return this.person;
	}

	public int getOrderListed() {
		return this.orderListed;
	}

	public boolean shouldMerge(Author otherAuthor) {
		return false;
	}

	public void merge(Author otherAuthor) {
	}

	public static Dictionary<String, Comparable<?>> createAttributes(int orderListed) {
		Dictionary<String, Comparable<?>> attributes = new Hashtable<String, Comparable<?>>();
		attributes.put(ISIDatabase.ORDER_LISTED, orderListed);

		return attributes;
	}
}