package edu.iu.sci2.database.star.extract.common.aggregate;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.Sets;

import edu.iu.cns.database.load.framework.DerbyFieldType;

/* TODO: Maybe refactor this so we can express generic "Number-compatibility", instead of
 * specifying each number type?
 */
public enum AggregateFunction {
	ARITHMETIC_MEAN(
			"Arithmetic Mean",
			"ARITHMETIC_MEAN",
			Sets.newHashSet(DerbyFieldType.INTEGER, DerbyFieldType.DOUBLE)) {
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
	COUNT(
		"Count",
		"COUNT",
		Sets.newHashSet(DerbyFieldType.INTEGER, DerbyFieldType.DOUBLE, DerbyFieldType.VARCHAR)),
	COUNT_DISTINCT(
			"Count Distinct",
			"COUNT_DISTINCT",
			Sets.newHashSet(DerbyFieldType.INTEGER, DerbyFieldType.DOUBLE, DerbyFieldType.VARCHAR)) {
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
	MIN("Min", "MIN", Sets.newHashSet(DerbyFieldType.INTEGER, DerbyFieldType.DOUBLE)),
	MAX("Max", "MAX", Sets.newHashSet(DerbyFieldType.INTEGER, DerbyFieldType.DOUBLE)),
//	MODE("Mode", "") {}...
	SUM("Sum", "SUM", Sets.newHashSet(DerbyFieldType.INTEGER, DerbyFieldType.DOUBLE));

	public static final Map<String, AggregateFunction> FUNCTIONS_BY_HUMAN_READABLE_NAMES =
		createFunctionsByHumanReadableNames();
	// TODO: Hacky!
	public static final Map<String, AggregateFunction>
		FUNCTIONS_WITHOUT_COUNT_DISTINCT_BY_HUMAN_READABLE_NAMES =
			createFunctionsWithoutCountDistinctByHumanReadableNames();
	public static final Map<String, AggregateFunction> FUNCTIONS_BY_SQL_NAMES =
		createFunctionsBySQLNames();
	public static final Map<String, String> SQL_NAMES_BY_HUMAN_READABLE_NAMES =
		createSQLNamesByHumanReadableNames();
	public static final Map<String, String>
		SQL_NAMES_WITHOUT_COUNT_DISTINCT_BY_HUMAN_READABLE_NAMES =
			createSQLNamesWithoutCountDistinctByHumanReadableNames();

	private static Map<String, AggregateFunction> createFunctionsByHumanReadableNames() {
		Map<String, AggregateFunction> functionsByHumanReadableNames =
			new HashMap<String, AggregateFunction>();

		for (AggregateFunction attributeFunction : AggregateFunction.values()) {
			functionsByHumanReadableNames.put(
				attributeFunction.getHumanReadableName(), attributeFunction);
		}

		return functionsByHumanReadableNames;
	}

	private static Map<String, AggregateFunction>
			createFunctionsWithoutCountDistinctByHumanReadableNames() {
		Map<String, AggregateFunction> functionsByHumanReadableNames =
			new HashMap<String, AggregateFunction>();

		for (AggregateFunction attributeFunction : AggregateFunction.values()) {
			if (!attributeFunction.getHumanReadableName().equals(
					COUNT_DISTINCT.getHumanReadableName())) {
				functionsByHumanReadableNames.put(
					attributeFunction.getHumanReadableName(), attributeFunction);
			}
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

	private static Map<String, String> createSQLNamesWithoutCountDistinctByHumanReadableNames() {
		Map<String, String> namesByHumanReadableNames = new HashMap<String, String>();

		for (AggregateFunction attributeFunction : AggregateFunction.values()) {
			if (!attributeFunction.getHumanReadableName().equals(
					COUNT_DISTINCT.getHumanReadableName())) {
				namesByHumanReadableNames.put(
					attributeFunction.getHumanReadableName(), attributeFunction.getSQLName());
			}
		}

		return namesByHumanReadableNames;
	}

	private String humanReadableName;
	private String sqlName;
	private Collection<DerbyFieldType> compatibleTypes;

	private AggregateFunction(
			String humanReadableName, String sqlName, Collection<DerbyFieldType> compatibleTypes) {
		this.humanReadableName = humanReadableName;
		this.sqlName = sqlName;
		this.compatibleTypes = compatibleTypes;
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

	public Collection<DerbyFieldType> compatibleTypes() {
		return this.compatibleTypes;
	}
}