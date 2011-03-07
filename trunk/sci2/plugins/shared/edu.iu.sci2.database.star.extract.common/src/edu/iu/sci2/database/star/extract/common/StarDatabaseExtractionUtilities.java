package edu.iu.sci2.database.star.extract.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import edu.iu.sci2.database.star.common.parameter.ColumnDescriptor;
import edu.iu.sci2.database.star.extract.common.aggregate.Aggregate;
import edu.iu.sci2.database.star.extract.common.aggregate.LeafAggregate;

public class StarDatabaseExtractionUtilities {
	public static final int ID_CHARACTER_COUNT = 10;
	public static final int MAXIMUM_LABEL_SIZE = 32000;

	public static Collection<String> formAggregateElements(
			Collection<? extends Aggregate> aggregates) {
		Collection<String> querySections = new ArrayList<String>();

		for (Aggregate aggregate : aggregates) {
			querySections.add(aggregate.databaseRepresentation());
		}

		return querySections;
	}

	public static Collection<String> formEmptyAggregateElements(
			Collection<? extends Aggregate> aggregates) {
		Collection<String> querySections = new ArrayList<String>();

		for (Aggregate aggregate : aggregates) {
			querySections.add(aggregate.emptyDatabaseRepresentation());
		}

		return querySections;
	}

	public static Collection<String> formJoinElements(Collection<LeafAggregate> leafAggregates) {
		Collection<String> joins = new HashSet<String>();

		for (LeafAggregate leafAggregate : leafAggregates) {
			String columnName = leafAggregate.getTargetColumnDescriptor().getNameForDatabase();
			joins.add(columnName);
		}

		return joins;
	}

	public static Collection<String> formCoreColumnElements(
			Collection<ColumnDescriptor> nonAggregatedColumns) {
		Collection<String> querySections = new ArrayList<String>();

		for (ColumnDescriptor columnDescriptor : nonAggregatedColumns) {
			querySections.add(String.format(
				"VARCHAR (CHAR (\"%s\"), %d) AS \"%s\"",
				columnDescriptor.getNameForDatabase(),
				MAXIMUM_LABEL_SIZE,
				columnDescriptor.getName()));
		}

		return querySections;
	}

	public static Collection<String> formEmptyCoreColumnNameElements(
			Collection<ColumnDescriptor> nonAggregatedColumns) {
		Collection<String> querySections = new ArrayList<String>();

		for (ColumnDescriptor columnDescriptor : nonAggregatedColumns) {
			querySections.add(String.format(
				"VARCHAR (CHAR (\'\'), %d) AS \"%s\"",
				MAXIMUM_LABEL_SIZE,
				columnDescriptor.getName()));
		}

		return querySections;
	}

	public static Collection<String> formCoreColumnNameElementsForGroupBy(
			Collection<ColumnDescriptor> nonAggregatedColumns) {
		Collection<String> querySections = new ArrayList<String>();

		for (ColumnDescriptor columnDescriptor : nonAggregatedColumns) {
			querySections.add(String.format("\"%s\"", columnDescriptor.getNameForDatabase()));
		}

		return querySections;
	}

	public static void putIfNotEmpty(Map<String, Object> target, String key, Collection<?> items) {
		if (items.size() > 0) {
			target.put(key, items);
		}
	}
}