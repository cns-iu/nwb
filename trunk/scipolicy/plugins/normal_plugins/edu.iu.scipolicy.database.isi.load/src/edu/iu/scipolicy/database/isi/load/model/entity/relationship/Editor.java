package edu.iu.scipolicy.database.isi.load.model.entity.relationship;

import java.util.Dictionary;
import java.util.Hashtable;

import org.cishell.utilities.dictionary.DictionaryEntry;
import org.cishell.utilities.dictionary.DictionaryUtilities;

import edu.iu.cns.database.loader.framework.DerbyFieldType;
import edu.iu.cns.database.loader.framework.RowItem;
import edu.iu.cns.database.loader.framework.Schema;
import edu.iu.nwb.shared.isiutil.database.ISI;
import edu.iu.scipolicy.database.isi.load.model.entity.Document;
import edu.iu.scipolicy.database.isi.load.model.entity.Person;

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
	private Integer orderListed;

	public Editor(Document document, Person person, Integer orderListed) {
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

	public Integer getOrderListed() {
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
			Document document, Person person, Integer orderListed) {
		Dictionary<String, Comparable<?>> attributes = new Hashtable<String, Comparable<?>>();
		attributes.put(ISI.EDITORS_DOCUMENT_FOREIGN_KEY, document.getPrimaryKey());
		attributes.put(ISI.EDITORS_PERSON_FOREIGN_KEY, person.getPrimaryKey());
		DictionaryUtilities.addIfNotNull(
			attributes,
			new DictionaryEntry<String, Comparable<?>>(ISI.ORDER_LISTED, orderListed));
		//attributes.put(ISI.ORDER_LISTED, orderListed);

		return attributes;
	}
}