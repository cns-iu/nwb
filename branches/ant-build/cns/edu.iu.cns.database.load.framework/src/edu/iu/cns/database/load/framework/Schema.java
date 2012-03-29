package edu.iu.cns.database.load.framework;

import java.util.ArrayList;
import java.util.List;

import org.cishell.utilities.NumberUtilities;

public class Schema <T extends RowItem<T>> {
	public static final String PRIMARY_KEY = "PK";

	private List<DBField> fields = new ArrayList<DBField>();
	private List<PrimaryKey> primaryKeys = new ArrayList<PrimaryKey>();
	private List<ForeignKey> foreignKeys = new ArrayList<ForeignKey>();

	/* TODO: Refactor Schema/the constants/framework/etc. as discussed with Micah, and then Joseph.
	 * TODO: I would very strongly prefer something along these lines: */
	public Schema(boolean addPrimaryKey, DBField... fields) {
		if (addPrimaryKey) {
			this.fields.add(new Field(PRIMARY_KEY, DerbyFieldType.PRIMARY_KEY));
			PRIMARY_KEYS(PRIMARY_KEY);
		}
		
		for (DBField f : fields) {
			addField(f);
		}
	}
	
	public Schema(boolean addPrimaryKey, Object...  objects) throws IllegalArgumentException {
		if (NumberUtilities.isOdd(objects.length)) {
			String exceptionMessage =
				"An even number of arguments must be supplied to Schema().  " +
				objects.length +
				" arguments were supplied.";
			throw new IllegalArgumentException(exceptionMessage);
		}

		if (addPrimaryKey) {
			this.fields.add(new Field(PRIMARY_KEY, DerbyFieldType.PRIMARY_KEY));
			PRIMARY_KEYS(PRIMARY_KEY);
		}

		for (int ii = 0; ii < objects.length; ii += 2) {
			String fieldName = (String)objects[ii];
			DerbyFieldType fieldType = (DerbyFieldType)objects[ii + 1];
			addField(fieldName, fieldType);
		}
	}

	public List<DBField> getFields() {
		return fields;
	}

	public List<PrimaryKey> getPrimaryKeys() {
		return this.primaryKeys;
	}

	public List<ForeignKey> getForeignKeys() {
		return this.foreignKeys;
	}

	public void addField(String fieldName, DerbyFieldType type) {
		this.fields.add(new Field(fieldName, type));
	}
	
	private void addField(DBField field) {
		this.fields.add(field);
	}

	public Schema<T> PRIMARY_KEYS(String... primaryKeys) {
		for (String primaryKey : primaryKeys) {
			this.primaryKeys.add(new PrimaryKey(primaryKey));
		}

		return this;
	}

	/* TODO I would discourage interleaving arguments in this way. */
	public Schema<T> FOREIGN_KEYS(String... foreignKeys) {
		if (NumberUtilities.isOdd(foreignKeys.length)) {
			String exceptionMessage =
				"An even number of arguments must be supplied to FORIEGN_KEYS().  " +
				foreignKeys.length + " arguments were supplied.";
			throw new IllegalArgumentException(exceptionMessage);
		}

		for (int ii = 0; ii < foreignKeys.length; ii += 2) {
			String fieldName = foreignKeys[ii];
			String referenceTo_TableName = foreignKeys[ii + 1];
			addForeignKey(fieldName, referenceTo_TableName);
		}

		return this;
	}

	public DBField findField(String fieldName) {
		for (DBField field : this.fields) {
			if (field.name().equals(fieldName)) {
				return field;
			}
		}

		return null;
	}

	private void addForeignKey(String fieldName, String referenceTo_TableName) {
		DBField field = findField(fieldName);

		if (field != null) {
			this.foreignKeys.add(new ForeignKey(fieldName, referenceTo_TableName));
		}
	}

	public static class Field implements DBField {
		private String name;
		private DerbyFieldType type;

		public Field(String name, DerbyFieldType type) {
			this.name = name;
			this.type = type;
		}

		public String name() {
			return this.name;
		}

		public DerbyFieldType type() {
			return this.type;
		}
	}

	public static class PrimaryKey {
		private String fieldName;

		public PrimaryKey(String fieldName) {
			this.fieldName = fieldName;
		}

		public String getFieldName() {
			return this.fieldName;
		}
	}

	public static class ForeignKey {
		private String fieldName;
		private String referenceTo_TableName;

		public ForeignKey(String fieldName, String referenceTo_TableName) {
			this.fieldName = fieldName;
			this.referenceTo_TableName = referenceTo_TableName;
		}

		public String getFieldName() {
			return this.fieldName;
		}

		public String getReferenceTo_TableName() {
			return this.referenceTo_TableName;
		}
	}

	private static class PrimaryKeyType {}

	private static class ForeignKeyType {}
}