package edu.iu.sci2.database.star.extract.network.attribute;

import java.util.HashMap;
import java.util.Map;

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

	public static final Map<String, AttributeFunction> ATTRIBUTE_FUNCTIONS_BY_NAME =
		createAttributeFunctionsByName();

	private static Map<String, AttributeFunction> createAttributeFunctionsByName() {
		Map<String, AttributeFunction> attributeFunctionsByName =
			new HashMap<String, AttributeFunction>();

		for (AttributeFunction attributeFunction : AttributeFunction.values()) {
			attributeFunctionsByName.put(
				attributeFunction.getHumanReadableName(), attributeFunction);
		}

		return attributeFunctionsByName;
	}

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