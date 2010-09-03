package edu.iu.sci2.database.star.extract.common.aggregate;

import java.util.HashMap;
import java.util.Map;

public enum AggregateFunction {
	ARITHMETIC_MEAN("Arithmetic Mean", "ARITHMETIC_MEAN") {
		@Override
		public String databaseRepresentation(
				String expressionToAggregate,
				boolean escapeExpressionToAggregate,
				String resultColumnName) {
			if (escapeExpressionToAggregate) {
				return String.format(
					"SUM (\"%1$s\") / COUNT (\"%1$s\") AS \"%2$s\"",
					expressionToAggregate,
					resultColumnName);
			} else {
				return String.format(
					"SUM (%1$s) / COUNT (%1$s) AS \"%2$s\"",
					expressionToAggregate,
					resultColumnName);
			}
		}
	},
	COUNT("Count", "COUNT"),
	COUNT_DISTINCT("Count Distinct", "COUNT_DISTINCT") {
		public String databaseRepresentation(
				String expressionToAggregate,
				boolean escapeExpressionToAggregate,
				String resultColumnName) {
			if (escapeExpressionToAggregate) {
				return String.format(
					"COUNT (DISTINCT \"%s\") AS \"%s\"",
					expressionToAggregate,
					resultColumnName);
			} else {
				return String.format(
					"COUNT (DISTINCT %s) AS \"%s\"",
					expressionToAggregate,
					resultColumnName);
			}
		}
	},
//	GEOMETRIC_MEAN("Geometric Mean", "") {
//		public String databaseRepresentation(
//				String expressionToAggregate, String resultColumnName) {
//			return "";
//		}	
	// TODO  (a_1 * a_2 * ... * a_n) ^ (1/n) or ((a_1)^(1/n) * (a_2)^(1/n) * ...), literally, but check if anyone has a smarter implementation to avoid overflow etc.
	
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

	public String databaseRepresentation(
			String expressionToAggregate,
			boolean escapeExpressionToAggregate,
			String resultColumnName) {
		if (escapeExpressionToAggregate) {
			return String.format(
				"%s (\"%s\") AS \"%s\"", this.sqlName, expressionToAggregate, resultColumnName);
		} else {
			return String.format(
				"%s (%s) AS \"%s\"", this.sqlName, expressionToAggregate, resultColumnName);
		}
	}

	public String emptyDatabaseRepresentation(String resultColumnName) {
		return String.format("0 AS \"%s\"", resultColumnName);
	}
}