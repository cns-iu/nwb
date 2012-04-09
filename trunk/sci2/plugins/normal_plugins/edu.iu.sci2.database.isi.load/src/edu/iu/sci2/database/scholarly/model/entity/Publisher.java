package edu.iu.sci2.database.scholarly.model.entity;

import static edu.iu.cns.database.load.framework.DerbyFieldType.FOREIGN_KEY;
import static edu.iu.cns.database.load.framework.DerbyFieldType.TEXT;

import java.util.Dictionary;

import edu.iu.cns.database.load.framework.DBField;
import edu.iu.cns.database.load.framework.DerbyFieldType;
import edu.iu.cns.database.load.framework.Entity;
import edu.iu.cns.database.load.framework.Schema;
import edu.iu.cns.database.load.framework.utilities.DatabaseTableKeyGenerator;
import edu.iu.nwb.shared.isiutil.database.ISI;

public class Publisher extends Entity<Publisher> {
	public static enum Field implements DBField {
		CITY(TEXT),
		NAME(TEXT),
		SOURCE_ID(FOREIGN_KEY),
		WEB_ADDRESS(TEXT);

		private final DerbyFieldType type;
		private Field(DerbyFieldType type) {
			this.type = type;
		}
		
		@Override
		public DerbyFieldType type() {
			return this.type;
		}
	}
	
	public static final Schema<Publisher> SCHEMA = new Schema<Publisher>(true, Field.values())
			.FOREIGN_KEYS(Field.SOURCE_ID.toString(), ISI.SOURCE_TABLE_NAME);

	public Publisher(DatabaseTableKeyGenerator keyGenerator,
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
	public void merge(Publisher otherItem) {
	}

}
