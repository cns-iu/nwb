package edu.iu.sci2.database.star.extract.network.attribute;

import java.util.Arrays;
import java.util.Collection;

public enum AttributeFunction {
	ARITHMETIC_MEAN("Arithmetic Mean", "") {
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

	public static final String AGGREGATE_FUNCTION_COUNT = "Count";
	public static final String AGGREGATE_FUNCTION_ARITHMETIC_MEAN = "Arithmetic Mean";
	public static final String AGGREGATE_FUNCTION_GEOMETRIC_MEAN = "Geometric Mean";
	public static final String AGGREGATE_FUNCTION_MIN = "Min";
	public static final String AGGREGATE_FUNCTION_MAX = "Max";
	public static final String AGGREGATE_FUNCTION_MODE = "Mode";
	public static final String AGGREGATE_FUNCTION_SUM = "Sum";
	public static final Collection<String> AGGREGATE_FUNCTION_NAMES = Arrays.asList(
		AGGREGATE_FUNCTION_COUNT,
		AGGREGATE_FUNCTION_ARITHMETIC_MEAN,
		AGGREGATE_FUNCTION_GEOMETRIC_MEAN,
		AGGREGATE_FUNCTION_MIN,
		AGGREGATE_FUNCTION_MAX,
		AGGREGATE_FUNCTION_MODE,
		AGGREGATE_FUNCTION_SUM);

	private String humanReadableName;
	private String sqlName;

	private AttributeFunction(String humanReadableName, String sqlName) {
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
}