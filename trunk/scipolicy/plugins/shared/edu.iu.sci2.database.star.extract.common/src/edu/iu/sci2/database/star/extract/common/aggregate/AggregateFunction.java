package edu.iu.sci2.database.star.extract.common.aggregate;

import java.util.HashMap;
import java.util.Map;

public enum AggregateFunction {
	ARITHMETIC_MEAN("Arithmetic Mean", "ARITHMETIC_MEAN") {
		public String databaseRepresentation(
				String expressionToAggregate, String resultColumnName) {
			return String.format(
				"SUM (\"%s\") / COUNT (\"%s\") AS \"%s\"",
				expressionToAggregate,
				expressionToAggregate,
				resultColumnName);
		}
	},
	COUNT("Count", "COUNT"),
//	GEOMETRIC_MEAN("Geometric Mean", "") {
//		public String databaseRepresentation(
//				String expressionToAggregate, String resultColumnName) {
//			return "";
//		}
//	},
	MIN("Min", "MIN"),
	MAX("Max", "MAX"),
//	MODE("Mode", "") {}...
	SUM("Sum", "SUM");

	public static final Map<String, AggregateFunction> FUNCTIONS_BY_HUMAN_READABLE_NAMES =
		createFunctionsByHumanReadableNames();
	public static final Map<String, AggregateFunction> FUNCTIONS_BY_SQL_NAMES =
		createFunctionsBySQLNames();
	public static final Map<String, String> SQL_NAMES_BY_HUMAN_READABLE_NAMES =
		createSQLNamesByHumanReadableNames();

	private static Map<String, AggregateFunction> createFunctionsByHumanReadableNames() {
		Map<String, AggregateFunction> functionsByHumanReadableNames =
			new HashMap<String, AggregateFunction>();

		for (AggregateFunction attributeFunction : AggregateFunction.values()) {
			functionsByHumanReadableNames.put(
				attributeFunction.getHumanReadableName(), attributeFunction);
		}

		return functionsByHumanReadableNames;
	}

	private static Map<String, AggregateFunction> createFunctionsBySQLNames() {
		Map<String, AggregateFunction> functionsBySQLNames =
			new HashMap<String, AggregateFunction>();

		for (AggregateFunction attributeFunction : AggregateFunction.values()) {
			functionsBySQLNames.put(attributeFunction.getSQLName(), attributeFunction);
		}

		return functionsBySQLNames;
	}

	private static Map<String, String> createSQLNamesByHumanReadableNames() {
		Map<String, String> namesByHumanReadableNames = new HashMap<String, String>();

		for (AggregateFunction attributeFunction : AggregateFunction.values()) {
			namesByHumanReadableNames.put(
				attributeFunction.getHumanReadableName(), attributeFunction.getSQLName());
		}

		return namesByHumanReadableNames;
	}

	private String humanReadableName;
	private String sqlName;

	private AggregateFunction(String humanReadableName, String sqlName) {
		this.humanReadableName = humanReadableName;
		this.sqlName = sqlName;
	}

	public String getHumanReadableName() {
		return this.humanReadableName;
	}

	public String getSQLName() {
		return this.sqlName;
	}

	public String databaseRepresentation(String expressionToAggregate, String resultColumnName) {
		// TODO: Escape stuff.
//		String escaped = StringUtilities.e

		return String.format(
			"%s (\"%s\") AS \"%s\"", this.sqlName, expressionToAggregate, resultColumnName);
	}

	public String emptyDatabaseRepresentation(String resultColumnName) {
		return String.format("0 AS \"%s\"", resultColumnName);
	}
}