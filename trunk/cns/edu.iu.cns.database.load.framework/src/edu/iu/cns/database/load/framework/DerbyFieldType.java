package edu.iu.cns.database.load.framework;

import java.sql.Types;

public enum DerbyFieldType {
	PRIMARY_KEY(Types.INTEGER, "INT NOT NULL", false),
	FOREIGN_KEY(Types.INTEGER, "INT", false),
	// ARRAY(Types.ARRAY, "NOT VALID"),
	BIGINT(Types.BIGINT, "BIGINT", false),
	BINARY(Types.BINARY, "CHAR") {
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
	BLOB(Types.BLOB, "BLOB"),
	// BOOLEAN(Types.BOOLEAN, "ENUM('false', 'true')"),
	BOOLEAN(Types.BOOLEAN, "VARCHAR(6)", false),
	CHAR(Types.CHAR, "CHAR"),
	CLOB(Types.CLOB, "CLOB"),
	// DATALINK(Types.DATALINK, "NOT VALID"),
	DATE(Types.DATE, "DATE", false),
	DECIMAL(Types.DECIMAL, "DECIMAL") {
		// TODO: Override original createDerbyQueryStringRepresentation?
		public String createDerbyQueryStringRepresentation(int precision, int scale) {
			// TODO: Add error checking for constraints?
			return createDerbyQueryStringRepresentation(precision + ", " + scale);
		}
	},
	// DISTINCT(Types.DISTINCT, "NOT VALID"),
	DOUBLE(Types.DOUBLE, "DOUBLE", false),
	FLOAT(Types.FLOAT, "FLOAT", false),
	INTEGER(Types.INTEGER, "INTEGER", false),
	// JAVA_OBJECT(Types.JAVA_OBJECT, "NOT VALID"),
	LONGVARBINARY(Types.LONGVARBINARY, "LONG VARCHAR FOR BIT DATA", false),
	LONGVARCHAR(Types.LONGVARCHAR, "LONG VARCHAR", false),
	// NULL(Types.NULL, "NOT VALID"),
	NUMERIC(Types.NUMERIC, "NUMERIC") {
		// TODO: Override original createDerbyQueryStringRepresentation?
		public String createDerbyQueryStringRepresentation(int precision, int scale) {
			// TODO: Add error checking for constraints?
			return createDerbyQueryStringRepresentation(precision + ", " + scale);
		}
	},
	// OTHER(Types.OTHER, "NOT VALID"),
	REAL(Types.REAL, "REAL", false),
	// REF(Types.REF, "NOT VALID"),
	SMALLINT(Types.SMALLINT, "SMALLINT", false),
	// STRUCT(Types.STRUCT, "NOT VALID"),
	TEXT(Types.VARCHAR, "VARCHAR(" + DerbyFieldType.MAX_VARCHAR_SIZE + ")", false),
	TIME(Types.TIME, "TIME", false),
	TIMESTAMP(Types.TIMESTAMP, "TIMESTAMP", false),
	// TINYINT(Types.TINYINT, "NOT VALID"),
	VARBINARY(Types.VARBINARY, "VARCHAR") {
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
	VARCHAR(Types.VARCHAR, "VARCHAR");

	public static final int MAX_VARCHAR_SIZE = 32000;

	private int sqlType;
	private String derbyQueryStringRepresentation;
	private boolean shouldAppendSpecifications;

	private DerbyFieldType(int sqlType, String derbyQueryStringRepresentation) {
		this(sqlType, derbyQueryStringRepresentation, true);
	}

	private DerbyFieldType(
			int sqlType,
			String derbyQueryStringRepresentation,
			boolean shouldAppendSpecifications) {
		this.sqlType = sqlType;
		this.derbyQueryStringRepresentation = derbyQueryStringRepresentation;
		this.shouldAppendSpecifications = shouldAppendSpecifications;
	}

	public int getSQLType() {
		return this.sqlType;
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
}