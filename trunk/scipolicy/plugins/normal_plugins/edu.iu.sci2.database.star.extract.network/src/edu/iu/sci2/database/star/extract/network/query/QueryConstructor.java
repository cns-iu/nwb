package edu.iu.sci2.database.star.extract.network.query;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.cishell.utilities.MapUtilities;
import org.cishell.utilities.StringUtilities;
import org.cishell.utility.swt.model.GUIModel;
import org.cishell.utility.swt.model.GUIModelField;
import org.cishell.utility.swt.model.GUIModelGroup;
import org.cishell.utility.swt.model.datasynchronizer.ModelDataSynchronizer;
import org.eclipse.swt.widgets.Widget;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

import edu.iu.sci2.database.star.common.StarDatabaseMetadata;
import edu.iu.sci2.database.star.common.parameter.ColumnDescriptor;
import edu.iu.sci2.database.star.extract.network.aggregate.Aggregate;

public abstract class QueryConstructor {
	public static final String STRING_TEMPLATE_BASE_FILE_PATH =
		"/edu/iu/sci2/database/star/extract/network/query/stringtemplate/";

	public static final String DEFAULT_NODE_ID_COLUMN = "ID";
	public static final String DEFAULT_SOURCE_COLUMN = "SOURCE";
	public static final String DEFAULT_TARGET_COLUMN = "TARGET";

	public static final String NODE_OBJECT_TYPE = "NODE";
	public static final String EDGE_OBJECT_TYPE = "EDGE";

	public static final int ID_CHARACTER_COUNT = 10;
	public static final int MAXIMUM_LABEL_SIZE = 32000;

	public static final String NODE_QUERY_STRING_TEMPLATE_NAME = "nodeQuery";
	public static final String EDGE_QUERY_STRING_TEMPLATE_NAME = "edgeQuery";

	private String coreTableName;
	private Collection<ColumnDescriptor> nodeNonAggregatedCoreColumns;
	private Collection<ColumnDescriptor> edgeNonAggregatedCoreColumns;
	private Collection<Aggregate> nodeAggregates;
	private Collection<Aggregate> edgeAggregates;

	private String nodeAggregatesForQuery;
	private String emptyNodeaggregatesForQuery;
	private String edgeAggregatesForQuery;

	private StringTemplateGroup aggregatesStringTemplateGroup;
	private StringTemplateGroup noAggregatesStringTemplateGroup;

	public QueryConstructor(
			String headerGroupName,
			String nodeAttributeFunctionGroupName,
			String nodeCoreEntityColumnGroupName,
			String nodeResultNameGroupName,
			String edgeAttributeFunctionGroupName,
			String edgeCoreEntityColumnGroupName,
			String edgeResultNameGroupName,
			GUIModel model,
			StarDatabaseMetadata metadata) {
		this.coreTableName = metadata.getCoreEntityTableName();

		organizeNodeAggregateStuff(
			model.getGroup(nodeAttributeFunctionGroupName),
			model.getGroup(nodeCoreEntityColumnGroupName),
			model.getGroup(nodeResultNameGroupName),
			metadata.getColumnDescriptorsByDatabaseName());
		organizeEdgeAggregateStuff(
			model.getGroup(edgeAttributeFunctionGroupName),
			model.getGroup(edgeCoreEntityColumnGroupName),
			model.getGroup(edgeResultNameGroupName),
			metadata.getColumnDescriptorsByDatabaseName());

		this.nodeAggregatesForQuery = formAggregateQuerySection(this.nodeAggregates);
		this.emptyNodeaggregatesForQuery = formEmptyAggregateQuerySection(this.nodeAggregates);
		this.edgeAggregatesForQuery = formAggregateQuerySection(this.edgeAggregates);

		this.aggregatesStringTemplateGroup = getAggregatesStringTemplateGroup();
		this.noAggregatesStringTemplateGroup = getNoAggregatesStringTemplateGroup();
	}

	public String getCoreTableName() {
		return this.coreTableName;
	}

	public Collection<ColumnDescriptor> getNodeNonAggregatedCoreColumns() {
		return this.nodeNonAggregatedCoreColumns;
	}

	public Collection<ColumnDescriptor> getEdgeNonAggregatedCoreColumns() {
		return this.edgeNonAggregatedCoreColumns;
	}

	public String getNodeAggregatesForQuery() {
		return this.nodeAggregatesForQuery;
	}

	public String getEmptyNodeAggregatesForQuery() {
		return this.emptyNodeaggregatesForQuery;
	}

	public String getEdgeAggregatesForQuery() {
		return this.edgeAggregatesForQuery;
	}

	public abstract StringTemplateGroup getAggregatesStringTemplateGroup();
	public abstract StringTemplateGroup getNoAggregatesStringTemplateGroup();

	public abstract Map<String, String> formNodeQueryWithAggregatesStringTemplateArguments(
			Map<String, String> arguments);
	public abstract Map<String, String> formNodeQueryWithoutAggregatesStringTemplateArguments(
			Map<String, String> arguments);

	public abstract Map<String, String> formEdgeQueryWithAggregatesStringTemplateArguments(
			Map<String, String> arguments);
	public abstract Map<String, String> formEdgeQueryWithoutAggregatesStringTemplateArguments(
			Map<String, String> arguments);

	public String constructNodeQuery() {
		StringTemplate template = getNodeQueryStringTemplate();

		return template.toString();
	}

	public String constructEdgeQuery() {
		StringTemplate template = getEdgeQueryStringTemplate();

		return template.toString();
	}
	
	public String getNodeIDColumn() {
		return DEFAULT_NODE_ID_COLUMN;
	}

	public String getSourceNodeName() {
		return DEFAULT_SOURCE_COLUMN;
	}

	public String getTargetNodeName() {
		return DEFAULT_TARGET_COLUMN;
	}
	
	public abstract boolean isDirected();

	private void organizeNodeAggregateStuff(
			GUIModelGroup aggregateFunctionGroup,
			GUIModelGroup coreEntityColumnGroup,
			GUIModelGroup resultColumnLabelGroup,
			Map<String, ColumnDescriptor> columnDescriptors) {
		List<ColumnDescriptor> nonAggregatedColumns = new ArrayList<ColumnDescriptor>();
		List<Aggregate> aggregates = new ArrayList<Aggregate>();
		organizeAggregateStuff(
			nonAggregatedColumns,
			NODE_OBJECT_TYPE,
			aggregates,
			aggregateFunctionGroup,
			coreEntityColumnGroup,
			resultColumnLabelGroup,
			columnDescriptors);
		this.nodeNonAggregatedCoreColumns =
			Collections2.filter(nonAggregatedColumns, new Predicate<ColumnDescriptor>() {
				public boolean apply(ColumnDescriptor columnDescriptor){
					return columnDescriptor.isCoreColumn();
				}
			});
		this.nodeAggregates = aggregates;
	}

	private void organizeEdgeAggregateStuff(
			GUIModelGroup aggregateFunctionGroup,
			GUIModelGroup coreEntityColumnGroup,
			GUIModelGroup resultColumnLabelGroup,
			Map<String, ColumnDescriptor> columnDescriptors) {
		List<ColumnDescriptor> nonAggregatedColumns = new ArrayList<ColumnDescriptor>();
		List<Aggregate> aggregates = new ArrayList<Aggregate>();
		organizeAggregateStuff(
			nonAggregatedColumns,
			EDGE_OBJECT_TYPE,
			aggregates,
			aggregateFunctionGroup,
			coreEntityColumnGroup,
			resultColumnLabelGroup,
			columnDescriptors);
		this.edgeNonAggregatedCoreColumns =
			Collections2.filter(nonAggregatedColumns, new Predicate<ColumnDescriptor>() {
				public boolean apply(ColumnDescriptor columnDescriptor){
					return columnDescriptor.isCoreColumn();
				}
			});
		this.edgeAggregates = aggregates;
	}

	/// Mutates nonAggregatedColumns and aggregates.
	// TODO: Document more (on how/why it mutates them)?
	private void organizeAggregateStuff(
			List<ColumnDescriptor> nonAggregatedColumns,
			String objectType,
			List<Aggregate> aggregates,
			GUIModelGroup aggregateFunctionGroup,
			GUIModelGroup coreEntityColumnGroup,
			GUIModelGroup resultColumnLabelGroup,
			Map<String, ColumnDescriptor> columnDescriptors) {
		Set<String> aggregatedColumns = new HashSet<String>();

		for (GUIModelField<
				?, ? extends Widget, ? extends ModelDataSynchronizer<?>> aggregateFunctionField :
					aggregateFunctionGroup.getFields()) {
			String id = aggregateFunctionField.getName();
			String coreEntityColumnName = (String) coreEntityColumnGroup.getField(id).getValue();
			String aggregateFunctionName = (String) aggregateFunctionField.getValue();
			String resultColumnLabelName = (String) resultColumnLabelGroup.getField(id).getValue();

			aggregatedColumns.add(coreEntityColumnName);
			aggregates.add(new Aggregate(
				objectType + "_" + resultColumnLabelName,
				aggregateFunctionName,
				coreEntityColumnName,
				columnDescriptors));
		}

		MapUtilities.valuesByKeys(columnDescriptors, aggregatedColumns, nonAggregatedColumns);
	}

	private StringTemplate getNodeQueryStringTemplate() {
		if (this.nodeAggregates.size() > 0) {
			return this.aggregatesStringTemplateGroup.getInstanceOf(
				NODE_QUERY_STRING_TEMPLATE_NAME,
				formNodeQueryWithAggregatesStringTemplateArguments(new HashMap<String, String>()));
		} else {
			return this.noAggregatesStringTemplateGroup.getInstanceOf(
				NODE_QUERY_STRING_TEMPLATE_NAME,
				formNodeQueryWithoutAggregatesStringTemplateArguments(
					new HashMap<String, String>()));
		}
	}

	private StringTemplate getEdgeQueryStringTemplate() {
		if (this.edgeAggregates.size() > 0) {
			return this.aggregatesStringTemplateGroup.getInstanceOf(
				EDGE_QUERY_STRING_TEMPLATE_NAME,
				formEdgeQueryWithAggregatesStringTemplateArguments(new HashMap<String, String>()));
		} else {
			return this.noAggregatesStringTemplateGroup.getInstanceOf(
				EDGE_QUERY_STRING_TEMPLATE_NAME,
				formEdgeQueryWithoutAggregatesStringTemplateArguments(
					new HashMap<String, String>()));
		}
	}

	public static StringTemplateGroup loadTemplate(String relativeTemplatePath) {
		String absoluteTemplatePath = STRING_TEMPLATE_BASE_FILE_PATH + relativeTemplatePath;

		return new StringTemplateGroup(new InputStreamReader(
			QueryConstructor.class.getResourceAsStream(absoluteTemplatePath)));
	}

	public static String formAggregateQuerySection(Collection<Aggregate> aggregates) {
		Collection<String> querySections = new ArrayList<String>();

		for (Aggregate aggregate : aggregates) {
			querySections.add(aggregate.databaseRepresentation());
		}

		return fixQuerySectionWithCommaPrefix(StringUtilities.implodeItems(querySections, ", "));
	}

	public static String formEmptyAggregateQuerySection(Collection<Aggregate> aggregates) {
		Collection<String> querySections = new ArrayList<String>();

		for (Aggregate aggregate : aggregates) {
			querySections.add(aggregate.emptyDatabaseRepresentation());
		}

		return fixQuerySectionWithCommaPrefix(StringUtilities.implodeItems(querySections, ", "));
	}

	public static String formCoreColumnsQuerySection(
			Collection<ColumnDescriptor> nonAggregatedColumns) {
		Collection<String> querySections = new ArrayList<String>();

		for (ColumnDescriptor columnDescriptor : nonAggregatedColumns) {
			querySections.add(String.format(
				"VARCHAR (CHAR (\"%s\"), %d) AS \"%s\"",
				columnDescriptor.getNameForDatabase(),
				MAXIMUM_LABEL_SIZE,
				columnDescriptor.getName()));
		}

		return fixQuerySectionWithCommaPrefix(StringUtilities.implodeItems(querySections, ", "));
	}

	public static String formCoreColumnsForGroupByQuerySection(
			Collection<ColumnDescriptor> nonAggregatedColumns) {
		Collection<String> querySections = new ArrayList<String>();

		for (ColumnDescriptor columnDescriptor : nonAggregatedColumns) {
			querySections.add(String.format("\"%s\"", columnDescriptor.getNameForDatabase()));
		}

		return fixQuerySectionWithCommaPrefix(StringUtilities.implodeItems(querySections, ", "));
	}

	public static String formEmptyCoreColumnsQuerySection(
			Collection<ColumnDescriptor> nonAggregatedColumns) {
		Collection<String> querySections = new ArrayList<String>();

		for (ColumnDescriptor columnDescriptor : nonAggregatedColumns) {
			querySections.add(String.format(
				"VARCHAR (CHAR (\'\'), %d) AS \"%s\"", MAXIMUM_LABEL_SIZE, columnDescriptor.getName()));
		}

		return fixQuerySectionWithCommaPrefix(StringUtilities.implodeItems(querySections, ", "));
	}

	private static String fixQuerySectionWithCommaPrefix(String querySection) {
		if (StringUtilities.isNull_Empty_OrWhitespace(querySection)) {
			return "";
		} else {
			return ", " + querySection;
		}
	}
}