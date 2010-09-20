package edu.iu.sci2.database.star.extract.network.query;

import org.cishell.utility.datastructure.datamodel.DataModel;

import edu.iu.sci2.database.star.extract.common.StarDatabaseDescriptor;
import edu.iu.sci2.database.star.extract.common.aggregate.Aggregate;
import edu.iu.sci2.database.star.extract.network.query.aggregateorganizer.BasicAggregateOrganizer;

public abstract class BasicNetworkQueryConstructor
		extends NetworkQueryConstructor<Aggregate, Aggregate> {
	public BasicNetworkQueryConstructor(
			String headerGroupName,
			String nodeAttributeFunctionGroupName,
			String nodeCoreEntityColumnGroupName,
			String nodeResultNameGroupName,
			String edgeAttributeFunctionGroupName,
			String edgeCoreEntityColumnGroupName,
			String edgeResultNameGroupName,
			DataModel model,
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
			new BasicAggregateOrganizer());
	}
}