package edu.iu.sci2.database.star.extract.network.query;

import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import org.antlr.stringtemplate.StringTemplateGroup;
import org.cishell.utility.swt.model.SWTModel;

import edu.iu.sci2.database.star.common.parameter.ColumnDescriptor;
import edu.iu.sci2.database.star.extract.common.StarDatabaseDescriptor;
import edu.iu.sci2.database.star.extract.common.aggregate.Aggregate;
import edu.iu.sci2.database.star.extract.network.aggregate.MirroredJoinAggregate;
import edu.iu.sci2.database.star.extract.network.query.aggregateorganizer.BasicAggregateOrganizer;
import edu.iu.sci2.database.star.extract.network.query.aggregateorganizer.MirroredJoinAggregateOrganizer;

public class CoEntityOccurrenceNetworkQueryConstructor
		extends NetworkQueryConstructor<Aggregate, MirroredJoinAggregate> {
	public static final String CO_ENTITY_OCCURRENCE_STRING_TEMPLATE_FILE_PATH =
		"co_entity_occurrence.st";

	public static final StringTemplateGroup STRING_TEMPLATE_GROUP = new StringTemplateGroup(
		new InputStreamReader(NetworkQueryConstructor.class.getResourceAsStream(
			STRING_TEMPLATE_BASE_FILE_PATH + CO_ENTITY_OCCURRENCE_STRING_TEMPLATE_FILE_PATH)));

	private String nodeLeafTableName;
	private String viaTableName;
	private Collection<String> edgeGroupByElements;

	public CoEntityOccurrenceNetworkQueryConstructor(
			String nodeLeafTableName,
			String viaTableName,
			String headerGroupName,
			String nodeAttributeFunctionGroupName,
			String nodeCoreEntityColumnGroupName,
			String nodeResultNameGroupName,
			String edgeAttributeFunctionGroupName,
			String edgeCoreEntityColumnGroupName,
			String edgeResultNameGroupName,
			SWTModel model,
			StarDatabaseDescriptor databaseDescriptor) {
		super(
			headerGroupName,
			nodeAttributeFunctionGroupName,
			nodeCoreEntityColumnGroupName,
			nodeResultNameGroupName,
			edgeAttributeFunctionGroupName,
			edgeCoreEntityColumnGroupName,
			edgeResultNameGroupName,
			model,
			databaseDescriptor,
			new BasicAggregateOrganizer(),
			new MirroredJoinAggregateOrganizer(
				nodeLeafTableName, databaseDescriptor.getMetadata().getCoreEntityTableName()));

		this.nodeLeafTableName = nodeLeafTableName;
		this.viaTableName = viaTableName;
		this.edgeGroupByElements = formGroupByElements(getEdgeNonAggregatedCoreColumns());
	}

	@Override
	public StringTemplateGroup getStringTemplateGroup() {
		return STRING_TEMPLATE_GROUP;
	}

	@Override
	public Map<String, Object> formNodeQueryStringTemplateArguments(
			Map<String, Object> arguments) {
		arguments.put("nodeLeafTableName", this.nodeLeafTableName);
		arguments.put("coreTableName", getCoreTableName());
		arguments.put("coreAggregates", getNodeAggregateElements());
		arguments.put("leafTableAggregates", getNodeLeafTableAggregateElements());
		arguments.put("leafTableAggregates_Joins", getNodeLeafTableAggregateJoinElements());

		return arguments;
	}

	@Override
	public Map<String, Object> formEdgeQueryStringTemplateArguments(
			Map<String, Object> arguments) {
		arguments.put("nodeLeafTableName", this.nodeLeafTableName);
		arguments.put("viaTableName", this.viaTableName);
		arguments.put("coreTableName", getCoreTableName());
		arguments.put("coreAggregates", getEdgeAggregateElements());
		arguments.put("leafTableAggregates", getEdgeLeafTableAggregateElements());
		arguments.put("leafTableAggregates_Joins", getEdgeLeafTableAggregateJoinElements());
		arguments.put("groupBy", this.edgeGroupByElements);

		return arguments;
	}

	@Override
	public boolean isDirected() {
		return false;
	}

	private static Collection<String> formGroupByElements(Collection<ColumnDescriptor> columns) {
		Collection<String> groupByElements = new HashSet<String>();

		for (ColumnDescriptor column : columns) {
			if (column.isCoreColumn()) {
				groupByElements.add(column.getNameForDatabase());
			}
		}

		return groupByElements;
	}
}