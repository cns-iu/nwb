package edu.iu.sci2.database.scholarly.model.entity;

import static edu.iu.cns.database.load.framework.DerbyFieldType.FOREIGN_KEY;
import static edu.iu.cns.database.load.framework.DerbyFieldType.INTEGER;
import static edu.iu.cns.database.load.framework.DerbyFieldType.TEXT;

import java.util.Dictionary;

import edu.iu.cns.database.load.framework.DBField;
import edu.iu.cns.database.load.framework.DerbyFieldType;
import edu.iu.cns.database.load.framework.Entity;
import edu.iu.cns.database.load.framework.Schema;
import edu.iu.cns.database.load.framework.utilities.DatabaseTableKeyGenerator;
import edu.iu.nwb.shared.isiutil.database.ISI;

public class Reference extends Entity<Reference> {
	public static enum Field implements DBField {
		ANNOTATION(TEXT),
		ARTICLE_NUMBER(TEXT),
		AUTHOR_ID(FOREIGN_KEY),
		DIGITAL_OBJECT_IDENTIFIER(TEXT),
		OTHER_INFORMATION(TEXT),
		PAGE_NUMBER(INTEGER),
		DOCUMENT_ID(FOREIGN_KEY),
		RAW_REFERENCE(TEXT),
		VOLUME(INTEGER),
		SOURCE_ID(FOREIGN_KEY),
		YEAR(INTEGER),
		STARRED(TEXT);

		private final DerbyFieldType type;
		private Field(DerbyFieldType type) {
			this.type = type;
		}
		
		@Override
		public DerbyFieldType type() {
			return this.type;
		}
	}
	
	public static final Schema<Reference> SCHEMA = new Schema<Reference>(true, Field.values())
			.FOREIGN_KEYS(
					Field.AUTHOR_ID.toString(), ISI.PERSON_TABLE_NAME,
					Field.DOCUMENT_ID.toString(), ISI.DOCUMENT_TABLE_NAME,
					Field.SOURCE_ID.toString(), ISI.SOURCE_TABLE_NAME);

	public Reference(DatabaseTableKeyGenerator keyGenerator,
			Dictionary<String, Object> attributes) {
		super(keyGenerator, attributes);
	}

	@Override
	public Object createMergeKey() {
		return getPrimaryKey();
	}

	/**
	 * Warning!  Unimplemented!
	 */
	@Override
	public void merge(Reference otherItem) {
	}

}
