package edu.iu.cns.database.loader.framework;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.cishell.utilities.NumberUtilities;

public class Schema <T extends RowItem<T>> {
	public static final String PRIMARY_KEY = "pk";

	public static final Class<?> PRIMARY_KEY_CLASS = PrimaryKeyType.class;
	public static final Class<?> FOREIGN_KEY_CLASS = ForeignKeyType.class;
	public static final Class<?> TEXT_CLASS = String.class;
	public static final Class<?> INTEGER_CLASS = Integer.class;
	public static final Class<?> DATE_CLASS = Date.class;
	public static final Class<?> DOUBLE_CLASS = Double.class;

	private List<Field> fields;
	private List<ForeignKey> foreignKeys = new ArrayList<ForeignKey>();

	public Schema(List<Field> fields) {
		this.fields = fields;
	}

	public Schema(Field... fields) {
		this.fields = new ArrayList<Field>();

		for (Field field : fields) {
			this.fields.add(field);
		}
	}

	public Schema(Object...  objects) throws IllegalArgumentException {
		if (NumberUtilities.isOdd(objects.length)) {
			String exceptionMessage =
				"An even number of arguments must be supplied to Schema().  " +
				objects.length + " arguments were supplied.";
			throw new IllegalArgumentException(exceptionMessage);
		}

		this.fields = new ArrayList<Field>();

		for (int ii = 0; ii < objects.length; ii += 2) {
			String fieldName = (String)objects[ii];
			Class<?> fieldClass = (Class<?>)objects[ii + 1];
			this.fields.add(new Field(fieldName, fieldClass));
		}
	}

	public List<Field> getFields() {
		return this.fields;
	}

	public List<ForeignKey> getForeignKeys() {
		return this.foreignKeys;
	}

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

	private Field findField(String fieldName) {
		for (Field field : this.fields) {
			if (field.name.equals(fieldName)) {
				return field;
			}
		}

		return null;
	}

	private void addForeignKey(String fieldName, String referenceTo_TableName) {
		Field field = findField(fieldName);

		if (field != null) {
			this.foreignKeys.add(new ForeignKey(referenceTo_TableName, fieldName));
		}
	}

	public static class Field {
		private String name;
		private Class<?> clazz;

		public Field(String name, Class<?> clazz) {
			this.name = name;
			this.clazz = clazz;
		}

		public String getName() {
			return this.name;
		}

		public Class<?> getClazz() {
			return this.clazz;
		}
	}

	public static class ForeignKey {
		private String fieldName;
		private String referenceTo_TableName;

		public ForeignKey(String fieldName, String referenceTo_TableName) {
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