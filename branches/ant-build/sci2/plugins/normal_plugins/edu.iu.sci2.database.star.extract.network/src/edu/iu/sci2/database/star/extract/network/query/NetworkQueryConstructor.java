package edu.iu.sci2.database.star.extract.network.query;

import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.cishell.utility.datastructure.datamodel.DataModel;

import edu.iu.sci2.database.star.common.parameter.ColumnDescriptor;
import edu.iu.sci2.database.star.extract.common.StarDatabaseDescriptor;
import edu.iu.sci2.database.star.extract.common.StarDatabaseExtractionUtilities;
import edu.iu.sci2.database.star.extract.common.aggregate.Aggregate;
import edu.iu.sci2.database.star.extract.common.aggregate.LeafAggregate;
import edu.iu.sci2.database.star.extract.network.query.aggregateorganizer.AggregateOrganizer;

public abstract class NetworkQueryConstructor<
		NodeAggregateType extends Aggregate, EdgeAggregateType extends Aggregate> {
	public static final String STRING_TEMPLATE_BASE_FILE_PATH =
		"/edu/iu/sci2/database/star/extract/network/query/stringtemplate/";

	public static final String DEFAULT_NODE_ID_COLUMN = "ID";
	public static final String DEFAULT_SOURCE_COLUMN = "SOURCE";
	public static final String DEFAULT_TARGET_COLUMN = "TARGET";

	public static final String NODE_OBJECT_TYPE = "NODE";
	public static final String EDGE_OBJECT_TYPE = "EDGE";

	public static final String NODE_QUERY_STRING_TEMPLATE_NAME = "nodeQuery";
	public static final String EDGE_QUERY_STRING_TEMPLATE_NAME = "edgeQuery";

	private String coreTableName;
	private Collection<ColumnDescriptor> nodeNonAggregatedCoreColumns;
	private Collection<ColumnDescriptor> edgeNonAggregatedCoreColumns;

	private Collection<String> nodeAggregateElements;
	private Collection<String> nodeLeafTableAggregateElements;
	private Collection<String> nodeLeafTableAggregateJoinElements;
	private Collection<String> emptyNodeAggregateElements;

	private Collection<String> edgeAggregateElements;
	private Collection<String> edgeLeafTableAggregateElements;
	private Collection<String> edgeLeafTableAggregateJoinElements;

	private StringTemplateGroup stringTemplateGroup;

	public NetworkQueryConstructor(
			String headerGroupName,
			String nodeAttributeFunctionGroupName,
			String nodeCoreEntityColumnGroupName,
			String nodeResultNameGroupName,
			String edgeAttributeFunctionGroupName,
			String edgeCoreEntityColumnGroupName,
			String edgeResultNameGroupName,
			DataModel model,
			StarDatabaseDescriptor databaseDescriptor,
			AggregateOrganizer<NodeAggregateType> nodeAggregateOrganizer,
			AggregateOrganizer<EdgeAggregateType> edgeAggregateOrganizer) {
		this.coreTableName = databaseDescriptor.getMetadata().getCoreEntityTableName();

		nodeAggregateOrganizer.organizeAggregates(
			this.coreTableName,
			NODE_OBJECT_TYPE,
			model.getGroup(nodeAttributeFunctionGroupName),
			model.getGroup(nodeCoreEntityColumnGroupName),
			model.getGroup(nodeResultNameGroupName),
			databaseDescriptor.getAllColumnDescriptorsBySQLName());
		Collection<NodeAggregateType> nodeAggregates =
			nodeAggregateOrganizer.getResultingCoreAggregates();
		Collection<LeafAggregate> nodeLeafAggregates =
			nodeAggregateOrganizer.getResultingLeafTableAggregates();
		this.nodeNonAggregatedCoreColumns =
			nodeAggregateOrganizer.getResultingNonAggregatedColumns();

		edgeAggregateOrganizer.organizeAggregates(
			this.coreTableName,
			EDGE_OBJECT_TYPE,
			model.getGroup(edgeAttributeFunctionGroupName),
			model.getGroup(edgeCoreEntityColumnGroupName),
			model.getGroup(edgeResultNameGroupName),
			databaseDescriptor.getAllColumnDescriptorsBySQLName());
		Collection<EdgeAggregateType> edgeAggregates =
			edgeAggregateOrganizer.getResultingCoreAggregates();
		Collection<LeafAggregate> edgeLeafAggregates =
			edgeAggregateOrganizer.getResultingLeafTableAggregates();
		this.edgeNonAggregatedCoreColumns =
			edgeAggregateOrganizer.getResultingNonAggregatedColumns();

		this.nodeAggregateElements =
			StarDatabaseExtractionUtilities.formAggregateElements(nodeAggregates);
		this.nodeLeafTableAggregateElements =
			StarDatabaseExtractionUtilities.formAggregateElements(nodeLeafAggregates);
		this.nodeLeafTableAggregateJoinElements =
			StarDatabaseExtractionUtilities.formJoinElements(nodeLeafAggregates);
		this.emptyNodeAggregateElements =
			StarDatabaseExtractionUtilities.formEmptyAggregateElements(nodeAggregates);

		this.edgeAggregateElements =
			StarDatabaseExtractionUtilities.formAggregateElements(edgeAggregates);
		this.edgeLeafTableAggregateElements =
			StarDatabaseExtractionUtilities.formAggregateElements(edgeLeafAggregates);
		this.edgeLeafTableAggregateJoinElements =
			StarDatabaseExtractionUtilities.formJoinElements(edgeLeafAggregates);

		this.stringTemplateGroup = getStringTemplateGroup();
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

	public Collection<String> getNodeAggregateElements() {
		return this.nodeAggregateElements;
	}

	public Collection<String> getNodeLeafTableAggregateElements() {
		return this.nodeLeafTableAggregateElements;
	}

	public Collection<String> getNodeLeafTableAggregateJoinElements() {
		return this.nodeLeafTableAggregateJoinElements;
	}

	public Collection<String> getEmptyNodeAggregateElements() {
		return this.emptyNodeAggregateElements;
	}

	public Collection<String> getEdgeAggregateElements() {
		return this.edgeAggregateElements;
	}

	public Collection<String> getEdgeLeafTableAggregateElements() {
		return this.edgeLeafTableAggregateElements;
	}

	public Collection<String> getEdgeLeafTableAggregateJoinElements() {
		return this.edgeLeafTableAggregateJoinElements;
	}

	public abstract StringTemplateGroup getStringTemplateGroup();

	public abstract Map<String, Object> formNodeQueryStringTemplateArguments(
			Map<String, Object> arguments);

	public abstract Map<String, Object> formEdgeQueryStringTemplateArguments(
			Map<String, Object> arguments);

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

	private StringTemplate getNodeQueryStringTemplate() {
		return this.stringTemplateGroup.getInstanceOf(
			NODE_QUERY_STRING_TEMPLATE_NAME,
			formNodeQueryStringTemplateArguments(new HashMap<String, Object>()));
	}

	private StringTemplate getEdgeQueryStringTemplate() {
		return this.stringTemplateGroup.getInstanceOf(
			EDGE_QUERY_STRING_TEMPLATE_NAME,
			formEdgeQueryStringTemplateArguments(new HashMap<String, Object>()));
	}

	public static StringTemplateGroup loadTemplate(String relativeTemplatePath) {
		String absoluteTemplatePath = STRING_TEMPLATE_BASE_FILE_PATH + relativeTemplatePath;

		return new StringTemplateGroup(new InputStreamReader(
			NetworkQueryConstructor.class.getResourceAsStream(absoluteTemplatePath)));
	}
}