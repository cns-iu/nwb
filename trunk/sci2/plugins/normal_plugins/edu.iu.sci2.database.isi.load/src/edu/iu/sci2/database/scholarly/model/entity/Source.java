package edu.iu.sci2.database.scholarly.model.entity;

import java.util.Dictionary;

import edu.iu.cns.database.load.framework.DBField;
import edu.iu.cns.database.load.framework.DerbyFieldType;
import edu.iu.cns.database.load.framework.Entity;
import edu.iu.cns.database.load.framework.Schema;
import edu.iu.cns.database.load.framework.utilities.DatabaseTableKeyGenerator;

public class Source extends Entity<Source> {
	public static enum Field implements DBField {
		BOOK_SERIES_TITLE(DerbyFieldType.TEXT),
		BOOK_SERIES_SUBTITLE(DerbyFieldType.TEXT),
		CONFERENCE_HOST(DerbyFieldType.TEXT),
		CONFERENCE_LOCATION(DerbyFieldType.TEXT),
		CONFERENCE_SPONSORS(DerbyFieldType.TEXT),
		CONFERENCE_TITLE(DerbyFieldType.TEXT),
		CONFERENCE_DATES(DerbyFieldType.TEXT),
		FULL_TITLE(DerbyFieldType.TEXT),
		ISO_TITLE_ABBREVIATION(DerbyFieldType.TEXT),
		ISSN(DerbyFieldType.TEXT),
		PUBLICATION_TYPE(DerbyFieldType.TEXT),
		TWENTY_NINE_CHARACTER_SOURCE_TITLE_ABBREVIATION(DerbyFieldType.TEXT);

		private DerbyFieldType fieldType;
		private Field(DerbyFieldType fieldType) {
			this.fieldType = fieldType;
		}
		
		@Override
		public DerbyFieldType type() {
			return this.fieldType;
		}
	}
	
	public static final Schema<Source> SCHEMA = new Schema<Source>(
			true, // autogen primary key
			Field.values());

	public Source(DatabaseTableKeyGenerator keyGenerator,
			Dictionary<String, Object> attributes) {
		super(keyGenerator, attributes);
	}

	@Override
	public Object createMergeKey() {
		return getAttributes().get("PK");
	}

	/**
	 * Warning!  Unimplemented!!
	 */
	@Override
	public void merge(Source otherItem) {
	}
}
