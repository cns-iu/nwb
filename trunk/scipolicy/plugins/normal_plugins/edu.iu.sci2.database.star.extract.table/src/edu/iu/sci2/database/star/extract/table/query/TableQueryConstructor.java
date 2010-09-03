package edu.iu.sci2.database.star.extract.table.query;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.cishell.utilities.StringUtilities;
import org.cishell.utility.datastructure.datamodel.DataModel;
import org.cishell.utility.datastructure.datamodel.field.DataModelField;
import org.cishell.utility.datastructure.datamodel.group.DataModelGroup;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;

import edu.iu.sci2.database.star.common.StarDatabaseMetadata;
import edu.iu.sci2.database.star.common.parameter.ColumnDescriptor;
import edu.iu.sci2.database.star.extract.common.StarDatabaseExtractionUtilities;
import edu.iu.sci2.database.star.extract.common.aggregate.Aggregate;
import edu.iu.sci2.database.star.extract.common.aggregate.LeafAggregate;

public class TableQueryConstructor {
	public static final String STRING_TEMPLATE_BASE_FILE_PATH =
		"/edu/iu/sci2/database/star/extract/table/query/stringtemplate/";
	public static final String TABLE_STRING_TEMPLATE_FILE_PATH = "table.st";
	public static final String QUERY_STRING_TEMPLATE_NAME = "query";

	public static final StringTemplateGroup STRING_TEMPLATE_GROUP = new StringTemplateGroup(
		new InputStreamReader(TableQueryConstructor.class.getResourceAsStream(
			STRING_TEMPLATE_BASE_FILE_PATH + TABLE_STRING_TEMPLATE_FILE_PATH)));

	public static final int MAXIMUM_LABEL_SIZE = 32000;

	private String leafTableName;
	private String coreTableName;
	private String coreNonAggregatesForQuery;
	private String coreAggregatesForQuery;
	private String leafTableAggregatesForQuery;
	private String leafTableAggregates_JoinsForQuery;
	private String groupByForQuery;

	/**
	 * to avoid stringifying at java level:
	 * 
	 * template:
	 * <coreAggregates.it(), sep=", ">
	 * 
	 * java:
	 * Collecton<Aggregate> coreAggregates; // fill up
	 * template.set("coreAggregates", coreAggregates)
	 * 
	 */
	
	public TableQueryConstructor(
			String leafTableFieldName,
			String headerGroupName,
			String tableAttributeFunctionGroupName,
			String tableColumnGroupName,
			String tableAttributeNameGroupName,
			DataModel model,
			StarDatabaseMetadata metadata) {
		List<ColumnDescriptor> nonAggregatedCoreColumns = new ArrayList<ColumnDescriptor>();
		List<Aggregate> aggregates = new ArrayList<Aggregate>();
		List<LeafAggregate> leafAggregates = new ArrayList<LeafAggregate>();
		determineTableAggregateData(
			nonAggregatedCoreColumns,
			aggregates,
			leafAggregates,
			model.getGroup(tableAttributeFunctionGroupName),
			model.getGroup(tableColumnGroupName),
			model.getGroup(tableAttributeNameGroupName),
			metadata.getColumnDescriptorsByDatabaseName(),
			metadata.getCoreEntityTableName());

		this.leafTableName =
			(String) model.getGroup(headerGroupName).getField(leafTableFieldName).getValue();
		this.coreTableName = metadata.getCoreEntityTableName();
		this.coreNonAggregatesForQuery = formCoreColumnsSelectionQuerySection(nonAggregatedCoreColumns);
		this.coreAggregatesForQuery = formAggregateQuerySection(aggregates);
		this.leafTableAggregatesForQuery = formAggregateQuerySection(leafAggregates);
		this.leafTableAggregates_JoinsForQuery = formJoinsForQuery(leafAggregates);
		this.groupByForQuery = formGroupBy(this.leafTableName, nonAggregatedCoreColumns);
	}

	public String constructQuery() {
		return getQueryStringTemplate().toString();
	}

	// TODO: Clean this up.  All of it.  It's ugly! *** cheese gr8r
	private static void determineTableAggregateData(
			List<ColumnDescriptor> nonAggregatedColumns,
			List<Aggregate> aggregates,
			List<LeafAggregate> leafAggregates,
			DataModelGroup aggregateFunctionGroup,
			DataModelGroup columnGroup,
			DataModelGroup attributeNameGroup,
			Map<String, ColumnDescriptor> columnDescriptors,
			String coreTableName) {
		Map<String, ColumnDescriptor> workingNonAggregatedColumns = Maps.newHashMap();
		workingNonAggregatedColumns.putAll(Maps.filterValues(
			columnDescriptors, new Predicate<ColumnDescriptor>() {
				public boolean apply(ColumnDescriptor columnDescriptor) {
					return columnDescriptor.isCoreColumn();
				}
			}));

		for (DataModelField<?> aggregateFunctionField : aggregateFunctionGroup.getFields()) {
			String id = aggregateFunctionField.getName();
			String columnName = (String) columnGroup.getField(id).getValue();
			String aggregateFunctionName = (String) aggregateFunctionField.getValue();
			String attributeName = (String) attributeNameGroup.getField(id).getValue();
			ColumnDescriptor columnUsed = columnDescriptors.get(columnName);

			if (columnUsed.isCoreColumn()) {
				aggregates.add(new Aggregate(
					attributeName,
					aggregateFunctionName,
					columnName,
					columnDescriptors));
			} else {
				leafAggregates.add(new LeafAggregate(
					attributeName,
					aggregateFunctionName,
					columnName,
					columnDescriptors,
					columnUsed.getNameForDatabase(),
					coreTableName));
			}

			workingNonAggregatedColumns.remove(columnName);
		}

		nonAggregatedColumns.addAll(workingNonAggregatedColumns.values());
	}

	public static String formCoreColumnsSelectionQuerySection(
			Collection<ColumnDescriptor> nonAggregatedColumns) {
		Collection<String> querySections = new ArrayList<String>();

		for (ColumnDescriptor columnDescriptor : nonAggregatedColumns) {
//			if (columnDescriptor.isCoreColumn()) {
				querySections.add(String.format(
					"VARCHAR (CHAR (\"%s\"), %d) AS \"%s\"",
					columnDescriptor.getNameForDatabase(),
					MAXIMUM_LABEL_SIZE,
					columnDescriptor.getName()));
//			}
		}

		// TODO hopefully not necessary after refactoring string template
		return StarDatabaseExtractionUtilities.fixQuerySectionWithCommaPrefix(
			StringUtilities.implodeItems(querySections, ", "));
	}

	public static String formAggregateQuerySection(Collection<? extends Aggregate> aggregates) {
		Collection<String> querySections = new ArrayList<String>();

		for (Aggregate aggregate : aggregates) {
			querySections.add(aggregate.databaseRepresentation());
		}

		return StarDatabaseExtractionUtilities.fixQuerySectionWithCommaPrefix(
			StringUtilities.implodeItems(querySections, ", "));
	}

	private static String formJoinsForQuery(Collection<LeafAggregate> leafAggregates) {
		Map<String, String> joins = new HashMap<String, String>();

		for (LeafAggregate leafAggregate : leafAggregates) {
			String columnName = leafAggregate.getTargetColumnDescriptor().getNameForDatabase();
			if (!joins.containsKey(columnName)) {
				joins.put(columnName, leafAggregate.joinExpression());
			}
		}

		return StringUtilities.implodeItems(joins.values(), "");
	}

	private static String formGroupBy(
			String leafTableName, List<ColumnDescriptor> nonAggregatedCoreColumns) {
		Collection<String> groupedBy = new ArrayList<String>();

//		for (ColumnDescriptor nonAggregatedCoreColumn : nonAggregatedCoreColumns) {
//			groupedBy.add(String.format("\"%s\"", nonAggregatedCoreColumn.getNameForDatabase()));
//		}

		String nonAggregatedColumnsForQuery =
			StarDatabaseExtractionUtilities.fixQuerySectionWithCommaPrefix(
				StringUtilities.implodeItems(groupedBy, ", "));

		return String.format(
			"GROUP BY \"%1$s\".\"%1$s\", \"%1$s\".PK %2$s",
			leafTableName,
			nonAggregatedColumnsForQuery);
	}

	private StringTemplate getQueryStringTemplate() {
		return STRING_TEMPLATE_GROUP.getInstanceOf(
			QUERY_STRING_TEMPLATE_NAME, formStringTemplateArguments());
	}

	private Map<String, String> formStringTemplateArguments() {
		Map<String, String> arguments = new HashMap<String, String>();
		arguments.put("leafTableName", this.leafTableName);
		arguments.put("coreTableName", this.coreTableName);
		arguments.put(
			"sentenceCased_LeafTableName", StringUtilities.toSentenceCase(this.leafTableName));
		arguments.put("coreNonAggregates", this.coreNonAggregatesForQuery);
		arguments.put("coreAggregates", this.coreAggregatesForQuery);
		arguments.put("leafTableAggregates", this.leafTableAggregatesForQuery);
		arguments.put("leafTableAggregates_Joins", this.leafTableAggregates_JoinsForQuery);
		arguments.put("groupBy", this.groupByForQuery);

		return arguments;
	}
}