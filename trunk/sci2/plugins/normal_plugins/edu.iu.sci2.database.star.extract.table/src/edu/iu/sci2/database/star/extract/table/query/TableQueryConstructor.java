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
	private Collection<String> coreAggregateElements;
	private Collection<String> leafTableAggregateElements;
	private Collection<String> leafTableAggregateJoinElements;
	
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
		this.coreAggregateElements =
			StarDatabaseExtractionUtilities.formAggregateElements(aggregates);
		this.leafTableAggregateElements =
			StarDatabaseExtractionUtilities.formAggregateElements(leafAggregates);
		this.leafTableAggregateJoinElements =
			StarDatabaseExtractionUtilities.formJoinElements(leafAggregates);
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

	private StringTemplate getQueryStringTemplate() {
		return STRING_TEMPLATE_GROUP.getInstanceOf(
			QUERY_STRING_TEMPLATE_NAME, formStringTemplateArguments());
	}

	private Map<String, Object> formStringTemplateArguments() {
		Map<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("leafTableName", this.leafTableName);
		arguments.put("coreTableName", this.coreTableName);
		arguments.put(
			"sentenceCased_LeafTableName", StringUtilities.toSentenceCase(this.leafTableName));
		StarDatabaseExtractionUtilities.putIfNotEmpty(
			arguments, "coreAggregates", this.coreAggregateElements);
		StarDatabaseExtractionUtilities.putIfNotEmpty(
			arguments, "leafTableAggregates", this.leafTableAggregateElements);
		StarDatabaseExtractionUtilities.putIfNotEmpty(
			arguments, "leafTableAggregates_Joins", this.leafTableAggregateJoinElements);

		return arguments;
	}
}