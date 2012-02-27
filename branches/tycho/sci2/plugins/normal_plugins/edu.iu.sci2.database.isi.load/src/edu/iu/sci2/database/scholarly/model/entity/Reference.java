package edu.iu.sci2.database.scholarly.model.entity;

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
		REFERENCE_ARTICLE_NUMBER(TEXT),
		REFERENCE_AUTHOR_FK(INTEGER),
		DIGITAL_OBJECT_IDENTIFIER(TEXT),
		REFERENCE_OTHER_INFORMATION(TEXT),
		REFERENCE_PAGE_NUMBER(INTEGER),
		PAPER_FK(INTEGER),
		RAW_REFERENCE_STRING(TEXT),
		REFERENCE_VOLUME(INTEGER),
		REFERENCE_SOURCE_FK(INTEGER),
		REFERENCE_YEAR(INTEGER),
		REFERENCE_WAS_STARRED(TEXT);

		private final DerbyFieldType type;
		private Field(DerbyFieldType type) {
			this.type = type;
		}
		
		public DerbyFieldType type() {
			return type;
		}
	}
	
	public static final Schema<Reference> SCHEMA = new Schema<Reference>(true, Field.values())
			.FOREIGN_KEYS(
					Field.REFERENCE_AUTHOR_FK.toString(), ISI.PERSON_TABLE_NAME,
					Field.PAPER_FK.toString(), ISI.DOCUMENT_TABLE_NAME,
					Field.REFERENCE_SOURCE_FK.toString(), ISI.SOURCE_TABLE_NAME);

	public Reference(DatabaseTableKeyGenerator keyGenerator,
			Dictionary<String, Object> attributes) {
		super(keyGenerator, attributes);
	}

	@Override
	public Object createMergeKey() {
		return getPrimaryKey();
	}

	@Override
	public void merge(Reference otherItem) {
	}

}
