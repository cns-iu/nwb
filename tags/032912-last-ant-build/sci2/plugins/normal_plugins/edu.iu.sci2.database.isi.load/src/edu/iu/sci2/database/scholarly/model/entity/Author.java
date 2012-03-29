package edu.iu.sci2.database.scholarly.model.entity;

import java.util.Collection;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import edu.iu.cns.database.load.framework.DBField;
import edu.iu.cns.database.load.framework.DerbyFieldType;
import edu.iu.cns.database.load.framework.RowItem;
import edu.iu.cns.database.load.framework.Schema;
import edu.iu.nwb.shared.isiutil.database.ISI;

import static edu.iu.sci2.database.scopus.load.EntityUtils.*;

public class Author extends RowItem<Author> {
	public static enum Field implements DBField {
		AUTHORS_DOCUMENT_FK(DerbyFieldType.FOREIGN_KEY),
		AUTHORS_PERSON_FK(DerbyFieldType.FOREIGN_KEY),
		EMAIL_ADDRESS(DerbyFieldType.TEXT),
		ORDER_LISTED(DerbyFieldType.INTEGER);

		private DerbyFieldType type;

		private Field(DerbyFieldType type) {
			this.type = type;
		}

		public DerbyFieldType type() {
			return this.type;
		}
	}

	public static final Schema<Author> SCHEMA = new Schema<Author>(
			false,
			Field.values()).
			FOREIGN_KEYS(
					Field.AUTHORS_DOCUMENT_FK.name(), ISI.DOCUMENT_TABLE_NAME,
					Field.AUTHORS_PERSON_FK.name(), ISI.PERSON_TABLE_NAME);
	
	public Author(Dictionary<String, Object> attributes) {
		super(attributes);
	}

	
	// ? what to do about email address?
	public static Collection<Author> makeAuthors(Document doc, List<Person> people) {
		List<Author> authors = new LinkedList<Author>();

		int authorOrder = 1;
		for (Person p : people) {
			Dictionary<String, Object> attribs = new Hashtable<String, Object>();
			putPK(attribs, Field.AUTHORS_DOCUMENT_FK, doc);
			putPK(attribs, Field.AUTHORS_PERSON_FK, p);
			attribs.put(Field.ORDER_LISTED.name(), authorOrder++);
			
			authors.add(new Author(attribs));
		}
		
		return authors;
	}
	
}
