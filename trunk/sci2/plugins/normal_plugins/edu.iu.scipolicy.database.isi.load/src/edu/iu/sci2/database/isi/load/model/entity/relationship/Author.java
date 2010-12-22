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
import edu.iu.sci2.database.isi.load.model.entity.Person;

public class Author extends RowItem<Author> {
	public static final Schema<Author> SCHEMA = new Schema<Author>(
		false,
		ISI.AUTHORS_DOCUMENT_FOREIGN_KEY, DerbyFieldType.FOREIGN_KEY,
		ISI.AUTHORS_PERSON_FOREIGN_KEY, DerbyFieldType.FOREIGN_KEY,
		ISI.AUTHORS_EMAIL_ADDRESS, DerbyFieldType.TEXT,
		ISI.ORDER_LISTED, DerbyFieldType.INTEGER).
		FOREIGN_KEYS(
			ISI.AUTHORS_DOCUMENT_FOREIGN_KEY, ISI.DOCUMENT_TABLE_NAME,
			ISI.AUTHORS_PERSON_FOREIGN_KEY, ISI.PERSON_TABLE_NAME);

	private Document document;
	private Person person;
	private String emailAddress;
	private Integer orderListed;

	public Author(Document document, Person person, String emailAddress, Integer orderListed) {
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

	public Integer getOrderListed() {
		return this.orderListed;
	}

	public static Dictionary<String, Object> createAttributes(
			Document document, Person person, String emailAddress, Integer orderListed) {
		Dictionary<String, Object> attributes = new Hashtable<String, Object>();
		attributes.put(ISI.AUTHORS_DOCUMENT_FOREIGN_KEY, document.getPrimaryKey());
		attributes.put(ISI.AUTHORS_PERSON_FOREIGN_KEY, person.getPrimaryKey());
		DictionaryUtilities.addIfNotNull(
			attributes,
			new DictionaryEntry<String, Object>(ISI.AUTHORS_EMAIL_ADDRESS, emailAddress),
			new DictionaryEntry<String, Object>(ISI.ORDER_LISTED, orderListed));

		return attributes;
	}
}