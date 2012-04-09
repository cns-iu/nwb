package edu.iu.sci2.database.scholarly.model.entity;

import static edu.iu.cns.database.load.framework.DerbyFieldType.TEXT;

import java.util.Dictionary;

import edu.iu.cns.database.load.framework.DBField;
import edu.iu.cns.database.load.framework.DerbyFieldType;
import edu.iu.cns.database.load.framework.Entity;
import edu.iu.cns.database.load.framework.Schema;
import edu.iu.cns.database.load.framework.utilities.DatabaseTableKeyGenerator;

public class Patent extends Entity<Patent> {
	public static enum Field implements DBField {
		NUMBER(TEXT);

		private final DerbyFieldType type;
		private Field(DerbyFieldType type) {
			this.type = type;
		}
		
		@Override
		public DerbyFieldType type() {
			return this.type;
		}
	}
	
	public static final Schema<Patent> SCHEMA = new Schema<Patent>(true, Field.values());

	public Patent(DatabaseTableKeyGenerator keyGenerator,
			Dictionary<String, Object> attributes) {
		super(keyGenerator, attributes);
	}

	@Override
	public Object createMergeKey() {
		return getPrimaryKey();
	}

	/**
	 * Warning!  Unimplemented!!
	 */
	@Override
	public void merge(Patent otherItem) {
	}

}
