package edu.iu.sci2.database.scholarly.model.entity;

import java.util.Dictionary;

import edu.iu.cns.database.load.framework.DBField;
import edu.iu.cns.database.load.framework.DerbyFieldType;
import edu.iu.cns.database.load.framework.Entity;
import edu.iu.cns.database.load.framework.Schema;
import edu.iu.cns.database.load.framework.utilities.DatabaseTableKeyGenerator;

public class Address extends Entity<Address> {
	public static enum Field implements DBField {
		CITY(DerbyFieldType.TEXT),
		COUNTRY(DerbyFieldType.TEXT),
		POSTAL_CODE(DerbyFieldType.TEXT),
		RAW_ADDRESS(DerbyFieldType.TEXT),
		STATE_OR_PROVINCE(DerbyFieldType.TEXT),
		STREET_ADDRESS(DerbyFieldType.TEXT);
		

		private final DerbyFieldType fieldType;

		private Field(DerbyFieldType type) {
			this.fieldType = type;
		}

		@Override
		public DerbyFieldType type() {
			return this.fieldType;
		}
	}
	
	public static final Schema<Address> SCHEMA = new Schema<Address>(
			true,
			Field.values());
			

	public Address(DatabaseTableKeyGenerator keyGenerator,
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
	public void merge(Address otherItem) {
	}

}
