package edu.iu.sci2.database.scholarly.model.entity;

import static edu.iu.cns.database.load.framework.DerbyFieldType.TEXT;

import java.util.Dictionary;

import edu.iu.cns.database.load.framework.DBField;
import edu.iu.cns.database.load.framework.DerbyFieldType;
import edu.iu.cns.database.load.framework.Entity;
import edu.iu.cns.database.load.framework.Schema;
import edu.iu.cns.database.load.framework.utilities.DatabaseTableKeyGenerator;

public class ISIFile extends Entity<ISIFile> {
	public static enum Field implements DBField {
		FORMAT_VERSION_NUMBER(TEXT),
		NAME(TEXT),
		FILE_TYPE(TEXT);

		private final DerbyFieldType type;
		private Field(DerbyFieldType type) {
			this.type = type;
		}
		
		@Override
		public DerbyFieldType type() {
			return this.type;
		}
	}
	
	public static final Schema<ISIFile> SCHEMA = new Schema<ISIFile>(true, Field.values());

	public ISIFile(DatabaseTableKeyGenerator keyGenerator,
			Dictionary<String, Object> attributes) {
		super(keyGenerator, attributes);
	}

	@Override
	public Object createMergeKey() {
		return getPrimaryKey();
	}

	/**
	 * Warning! Unimplemented!!
	 */
	@Override
	public void merge(ISIFile otherItem) {
	}

}
