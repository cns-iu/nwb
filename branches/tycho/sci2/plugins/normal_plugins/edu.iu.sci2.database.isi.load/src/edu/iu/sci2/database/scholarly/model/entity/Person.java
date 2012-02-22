package edu.iu.sci2.database.scholarly.model.entity;

import java.util.Dictionary;

import edu.iu.cns.database.load.framework.DBField;
import edu.iu.cns.database.load.framework.DerbyFieldType;
import edu.iu.cns.database.load.framework.Entity;
import edu.iu.cns.database.load.framework.Schema;
import edu.iu.cns.database.load.framework.utilities.DatabaseTableKeyGenerator;

public class Person extends Entity<Person> {
	public static enum Field implements DBField {
		ADDITIONAL_NAME,
		FAMILY_NAME,
		FIRST_INITIAL,
		FULL_NAME,
		MIDDLE_INITIAL,
		PERSONAL_NAME,
		UNSPLIT_NAME;

		public DerbyFieldType type() {
			return DerbyFieldType.TEXT;
		}
	}

	public static final Schema<Person> SCHEMA = new Schema<Person>(
			true,
			Field.values());
	
	public Person(DatabaseTableKeyGenerator keyGenerator,
			Dictionary<String, Object> attributes) {
		super(keyGenerator, attributes);
	}

	@Override
	public Object createMergeKey() {
		return getPrimaryKey();
	}

	@Override
	public void merge(Person otherPerson) {
	}

}
