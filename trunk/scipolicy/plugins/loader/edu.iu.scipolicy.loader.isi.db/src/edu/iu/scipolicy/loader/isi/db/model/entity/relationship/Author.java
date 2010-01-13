package edu.iu.scipolicy.loader.isi.db.model.entity.relationship;

import java.util.Dictionary;
import java.util.Hashtable;

import edu.iu.cns.database.loader.framework.RowItem;
import edu.iu.cns.database.loader.framework.Schema;
import edu.iu.cns.database.loader.framework.DerbyFieldType;
import edu.iu.nwb.shared.isiutil.database.ISIDatabase;
import edu.iu.scipolicy.loader.isi.db.model.entity.Document;
import edu.iu.scipolicy.loader.isi.db.model.entity.Person;

public class Author extends RowItem<Author> {
	// TODO: E-mail address.
	public static final Schema<Author> SCHEMA = new Schema<Author>(
		false,
		ISIDatabase.AUTHORS_DOCUMENT_FOREIGN_KEY, DerbyFieldType.FOREIGN_KEY,
		ISIDatabase.AUTHORS_PERSON_FOREIGN_KEY, DerbyFieldType.FOREIGN_KEY,
		ISIDatabase.AUTHORS_EMAIL_ADDRESS, DerbyFieldType.TEXT,
		ISIDatabase.ORDER_LISTED, DerbyFieldType.INTEGER).
		FOREIGN_KEYS(
			ISIDatabase.AUTHORS_DOCUMENT_FOREIGN_KEY, ISIDatabase.DOCUMENT_TABLE_NAME,
			ISIDatabase.AUTHORS_PERSON_FOREIGN_KEY, ISIDatabase.PERSON_TABLE_NAME);

	private Document document;
	private Person person;
	private String emailAddress;
	private int orderListed;

	public Author(Document document, Person person, String emailAddress, int orderListed) {
		super(createAttributes(document, person, emailAddress, orderListed));
		this.document = document;
		this.person = person;
		this.emailAddress = emailAddress;
		this.orderListed = orderListed;
	}

	public Document getDocument() {
		return this.document;
	}

	public Person getPerson() {
		return this.person;
	}

	public String getEmailAddress() {
		return this.emailAddress;
	}

	public int getOrderListed() {
		return this.orderListed;
	}

	public boolean shouldMerge(Author otherAuthor) {
		if ((this.document != null) && (this.person != null)) {
			Document otherDocument = otherAuthor.getDocument();
			Person otherPerson = otherAuthor.getPerson();

			if ((otherDocument != null) && (otherPerson != null)) {
				return (
					(this.document.getPrimaryKey() == otherDocument.getPrimaryKey()) &&
					(this.person.getPrimaryKey() == otherPerson.getPrimaryKey()));
			}
		}

		return false;
	}

	public void merge(Author otherAuthor) {
	}

	public static Dictionary<String, Comparable<?>> createAttributes(
			Document document, Person person, String emailAddress, int orderListed) {
		Dictionary<String, Comparable<?>> attributes = new Hashtable<String, Comparable<?>>();
		attributes.put(ISIDatabase.AUTHORS_DOCUMENT_FOREIGN_KEY, document.getPrimaryKey());
		attributes.put(ISIDatabase.AUTHORS_PERSON_FOREIGN_KEY, person.getPrimaryKey());
		attributes.put(ISIDatabase.AUTHORS_EMAIL_ADDRESS, emailAddress);
		attributes.put(ISIDatabase.ORDER_LISTED, orderListed);

		return attributes;
	}
}