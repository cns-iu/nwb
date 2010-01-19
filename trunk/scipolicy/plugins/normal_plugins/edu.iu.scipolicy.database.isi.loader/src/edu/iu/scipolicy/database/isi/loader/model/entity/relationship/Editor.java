package edu.iu.scipolicy.database.isi.loader.model.entity.relationship;

import java.util.Dictionary;
import java.util.Hashtable;

import edu.iu.cns.database.loader.framework.RowItem;
import edu.iu.cns.database.loader.framework.Schema;
import edu.iu.cns.database.loader.framework.DerbyFieldType;
import edu.iu.nwb.shared.isiutil.database.ISI;
import edu.iu.scipolicy.database.isi.loader.model.entity.Document;
import edu.iu.scipolicy.database.isi.loader.model.entity.Person;

public class Editor extends RowItem<Editor> implements Comparable<Editor> {
	public static final Schema<Editor> SCHEMA = new Schema<Editor>(
		false,
		ISI.EDITORS_DOCUMENT_FOREIGN_KEY, DerbyFieldType.FOREIGN_KEY,
		ISI.EDITORS_PERSON_FOREIGN_KEY, DerbyFieldType.FOREIGN_KEY,
		ISI.ORDER_LISTED, DerbyFieldType.INTEGER).
		FOREIGN_KEYS(
			ISI.EDITORS_DOCUMENT_FOREIGN_KEY, ISI.DOCUMENT_TABLE_NAME,
			ISI.EDITORS_PERSON_FOREIGN_KEY, ISI.PERSON_TABLE_NAME);

	private Document document;
	private Person person;
	private int orderListed;

	public Editor(Document document, Person person, int orderListed) {
		super(createAttributes(document, person, orderListed));
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

	public int compareTo(Editor otherEditor) {
		// TODO:
		return -1;
	}

	public boolean shouldMerge(Editor otherEditor) {
		if ((this.document != null) && (this.person != null)) {
			Document otherDocument = otherEditor.getDocument();
			Person otherPerson = otherEditor.getPerson();

			if ((otherDocument != null) && (otherPerson != null)) {
				return (
					(this.document.getPrimaryKey() == otherDocument.getPrimaryKey()) &&
					(this.person.getPrimaryKey() == otherPerson.getPrimaryKey()));
			}
		}

		return false;
	}

	public void merge(Editor otherEditor) {
	}

	public static Dictionary<String, Comparable<?>> createAttributes(
			Document document, Person person, int orderListed) {
		Dictionary<String, Comparable<?>> attributes = new Hashtable<String, Comparable<?>>();
		attributes.put(ISI.EDITORS_DOCUMENT_FOREIGN_KEY, document.getPrimaryKey());
		attributes.put(ISI.EDITORS_PERSON_FOREIGN_KEY, person.getPrimaryKey());
		attributes.put(ISI.ORDER_LISTED, orderListed);

		return attributes;
	}
}