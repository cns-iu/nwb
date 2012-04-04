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
		PUBLISHER_CITY(TEXT),
		PUBLISHER_NAME(TEXT),
		PUBLISHER_SOURCE_FK(FOREIGN_KEY),
		PUBLISHER_WEB_ADDRESS(TEXT);

		private final DerbyFieldType type;
		private Field(DerbyFieldType type) {
			this.type = type;
		}
		
		public DerbyFieldType type() {
			return type;
		}
	}
	
	public static final Schema<Publisher> SCHEMA = new Schema<Publisher>(true, Field.values())
			.FOREIGN_KEYS(Field.PUBLISHER_SOURCE_FK.toString(), ISI.SOURCE_TABLE_NAME);

	public Publisher(DatabaseTableKeyGenerator keyGenerator,
			Dictionary<String, Object> attributes) {
		super(keyGenerator, attributes);
	}

	@Override
	public Object createMergeKey() {
		return getPrimaryKey();
	}

	@Override
	public void merge(Publisher otherItem) {
	}

}
