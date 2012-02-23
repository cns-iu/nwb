package edu.iu.sci2.database.scholarly.model.entity;

import static edu.iu.sci2.database.scopus.load.EntityUtils.putPK;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import edu.iu.cns.database.load.framework.DBField;
import edu.iu.cns.database.load.framework.DerbyFieldType;
import edu.iu.cns.database.load.framework.RowItem;
import edu.iu.cns.database.load.framework.Schema;
import edu.iu.nwb.shared.isiutil.database.ISI;

public class Editor extends RowItem<Editor> {
	public static enum Field implements DBField {
		EDITORS_DOCUMENT_FK(DerbyFieldType.INTEGER),
		EDITORS_PERSON_FK(DerbyFieldType.INTEGER);

		private final DerbyFieldType type;
		
		private Field(DerbyFieldType type) {
			this.type = type;
		}
		public DerbyFieldType type() {
			return this.type;
		}
	}
	
	public static final Schema<Editor> SCHEMA = new Schema<Editor>(
			false,
			Field.values()).
			FOREIGN_KEYS(
					Field.EDITORS_DOCUMENT_FK.name(), ISI.DOCUMENT_TABLE_NAME,
					Field.EDITORS_PERSON_FK.name(), ISI.PERSON_TABLE_NAME);
	
	public Editor(Dictionary<String, Object> attributes) {
		super(attributes);
	}

	public static List<Editor> makeEditors(Document document,
			List<Person> people) {
		List<Editor> editors = new LinkedList<Editor>();

		for (Person p : people) {
			Dictionary<String, Object> attribs = new Hashtable<String, Object>();
			putPK(attribs, Field.EDITORS_DOCUMENT_FK, document);
			putPK(attribs, Field.EDITORS_PERSON_FK, p);
			
			editors.add(new Editor(attribs));
		}
		
		return editors;
	}
}
