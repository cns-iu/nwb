package edu.iu.cns.database.load.framework;

import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import edu.iu.cns.database.load.framework.exception.InvalidDerbyFieldTypeException;

public enum DerbyFieldType {
	PRIMARY_KEY(Types.INTEGER, "Primary Key", "INT NOT NULL", false),
	FOREIGN_KEY(Types.INTEGER, "Foreign Key", "INT", false),
	// ARRAY(Types.ARRAY, "NOT VALID"),
	BIGINT(Types.BIGINT, "Big Integer", "BIGINT", false),
	BINARY(Types.BINARY, "Binary Blob", "CHAR") {
		public String getDerbyQueryStringRepresentation() {
			return super.getDerbyQueryStringRepresentation() + " FOR BIT DATA";
		}

		public String createDerbyQueryStringRepresentation(String specificationsString) {
			return
				super.getDerbyQueryStringRepresentation() +
				"(" + specificationsString + ")" +
				" FOR BIT DATA";
		}
	},
	// BIT(Types.BIT, "NOT VALID"),
	BLOB(Types.BLOB, "Blob", "BLOB"),
	// BOOLEAN(Types.BOOLEAN, "ENUM('false', 'true')"),
	BOOLEAN(Types.BOOLEAN, "Boolean", "VARCHAR(6)", false),
	CHAR(Types.CHAR, "Character", "CHAR"),
	CLOB(Types.CLOB, "Clob", "CLOB"),
	// DATALINK(Types.DATALINK, "NOT VALID"),
	DATE(Types.DATE, "Date", "DATE", false),
	DECIMAL(Types.DECIMAL, "Decimal", "DECIMAL") {
		// TODO: Override original createDerbyQueryStringRepresentation?
		@SuppressWarnings("unused")
		public String createDerbyQueryStringRepresentation(int precision, int scale) {
			// TODO: Add error checking for constraints?
			return createDerbyQueryStringRepresentation(precision + ", " + scale);
		}
	},
	// DISTINCT(Types.DISTINCT, "NOT VALID"),
	DOUBLE(Types.DOUBLE, "Double", "DOUBLE", false),
	FLOAT(Types.FLOAT, "Float", "FLOAT", false),
	INTEGER(Types.INTEGER, "Integer", "INTEGER", false),
	// JAVA_OBJECT(Types.JAVA_OBJECT, "NOT VALID"),
	LONGVARBINARY(Types.LONGVARBINARY, "Long Bit String", "LONG VARCHAR FOR BIT DATA", false),
	LONGVARCHAR(Types.LONGVARCHAR, "Long String", "LONG VARCHAR", false),
	// NULL(Types.NULL, "NOT VALID"),
	NUMERIC(Types.NUMERIC, "Number", "NUMERIC") {
		// TODO: Override original createDerbyQueryStringRepresentation?
		@SuppressWarnings("unused")
		public String createDerbyQueryStringRepresentation(int precision, int scale) {
			// TODO: Add error checking for constraints?
			return createDerbyQueryStringRepresentation(precision + ", " + scale);
		}
	},
	// OTHER(Types.OTHER, "NOT VALID"),
	REAL(Types.REAL, "Real", "REAL", false),
	// REF(Types.REF, "NOT VALID"),
	SMALLINT(Types.SMALLINT, "Small Integer", "SMALLINT", false),
	// STRUCT(Types.STRUCT, "NOT VALID"),
	TEXT(
		Types.VARCHAR,
		"Maximum-Lengthed String",
		"VARCHAR(" + 32000 + ")",
		false),
	TIME(Types.TIME, "Time", "TIME", false),
	TIMESTAMP(Types.TIMESTAMP, "Time Stamp", "TIMESTAMP", false),
	// TINYINT(Types.TINYINT, "NOT VALID"),
	VARBINARY(Types.VARBINARY, "Bit String", "VARCHAR") {
		public String getDerbyQueryStringRepresentation() {
			return super.getDerbyQueryStringRepresentation() + " FOR BIT DATA";
		}

		public String createDerbyQueryStringRepresentation(String specificationsString) {
			return
				super.getDerbyQueryStringRepresentation() +
				"(" + specificationsString + ")" +
				" FOR BIT DATA";
		}
	},
	VARCHAR(Types.VARCHAR, "String", "VARCHAR(" + 32000 + ")");

	public static final int MAX_VARCHAR_SIZE = 32000;

	private static final Map<String, DerbyFieldType> TYPES_BY_HUMAN_READABLE_NAME =
		constructTypesByHumanReadableName();

	private int sqlType;
	private String humanReadableName;
	private String derbyQueryStringRepresentation;
	private boolean shouldAppendSpecifications;

	private DerbyFieldType(
			int sqlType, String humanReadableName, String derbyQueryStringRepresentation) {
		this(sqlType, humanReadableName, derbyQueryStringRepresentation, true);
	}

	private DerbyFieldType(
			int sqlType,
			String humanReadableName,
			String derbyQueryStringRepresentation,
			boolean shouldAppendSpecifications) {
		this.sqlType = sqlType;
		this.humanReadableName = humanReadableName;
		this.derbyQueryStringRepresentation = derbyQueryStringRepresentation;
		this.shouldAppendSpecifications = shouldAppendSpecifications;
	}

	public int getSQLType() {
		return this.sqlType;
	}

	public String getHumanReadableName() {
		return this.humanReadableName;
	}

	public String getDerbyQueryStringRepresentation() {
		return this.derbyQueryStringRepresentation;
	}

	public boolean shouldAppendSpecifications() {
		return this.shouldAppendSpecifications;
	}

	public String createDerbyQueryStringRepresentation(String specificationsString) {
		if (this.shouldAppendSpecifications) {
			return this.derbyQueryStringRepresentation + "(" + specificationsString + ")";
		} else {
			return getDerbyQueryStringRepresentation();
		}
	}

	public static DerbyFieldType getFieldTypeByHumanReadableName(String humanReadableName)
			throws InvalidDerbyFieldTypeException {
		if (TYPES_BY_HUMAN_READABLE_NAME.containsKey(humanReadableName)) {
			return TYPES_BY_HUMAN_READABLE_NAME.get(humanReadableName);
		} else {
			String exceptionMessage =
				"No DerbyFieldType could be found for the human-readable name \"" +
				humanReadableName +
				"\".";
			throw new InvalidDerbyFieldTypeException(exceptionMessage);
		}
	}

	private static Map<String, DerbyFieldType> constructTypesByHumanReadableName() {
		Map<String, DerbyFieldType> typesByHumanReadableName =
			new HashMap<String, DerbyFieldType>();

		for (DerbyFieldType fieldType : DerbyFieldType.values()) {
			typesByHumanReadableName.put(fieldType.humanReadableName, fieldType);
		}

		return Collections.unmodifiableMap(typesByHumanReadableName);
	}
}