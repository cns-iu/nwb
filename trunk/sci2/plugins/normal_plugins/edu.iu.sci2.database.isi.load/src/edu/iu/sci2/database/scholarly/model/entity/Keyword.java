package edu.iu.sci2.database.scholarly.model.entity;

import java.util.Dictionary;

import edu.iu.cns.database.load.framework.DBField;
import edu.iu.cns.database.load.framework.DerbyFieldType;
import edu.iu.cns.database.load.framework.Entity;
import edu.iu.cns.database.load.framework.Schema;
import edu.iu.cns.database.load.framework.utilities.DatabaseTableKeyGenerator;

public class Keyword extends Entity<Keyword> {
	public static enum Field implements DBField {
		KEYWORD,
		TYPE;
		
		public DerbyFieldType type() {
			return DerbyFieldType.TEXT;
		}

	}
	public static Schema<Keyword> SCHEMA = new Schema<Keyword>(true, Field.values());

	public Keyword(DatabaseTableKeyGenerator keyGenerator,
			Dictionary<String, Object> attributes) {
		super(keyGenerator, attributes);
	}

	@Override
	public Object createMergeKey() {
		return getPrimaryKey();
	}

	@Override
	public void merge(Keyword otherItem) {
	}

}
