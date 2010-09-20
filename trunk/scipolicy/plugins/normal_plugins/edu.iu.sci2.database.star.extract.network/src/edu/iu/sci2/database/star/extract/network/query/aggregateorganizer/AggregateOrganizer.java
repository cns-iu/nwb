package edu.iu.sci2.database.star.extract.network.query.aggregateorganizer;

import java.util.Collection;
import java.util.Map;

import org.cishell.utility.datastructure.datamodel.group.DataModelGroup;

import edu.iu.sci2.database.star.common.parameter.ColumnDescriptor;
import edu.iu.sci2.database.star.extract.common.aggregate.Aggregate;
import edu.iu.sci2.database.star.extract.common.aggregate.LeafAggregate;

public interface AggregateOrganizer<T extends Aggregate> {
	public void organizeAggregates(
			String coreTableName,
			String objectType,
			DataModelGroup aggregateFunctionGroup,
			DataModelGroup coreEntityColumnGroup,
			DataModelGroup attributeGroup,
			Map<String, ColumnDescriptor> columnDescriptors);
	public Collection<ColumnDescriptor> getResultingNonAggregatedColumns();
	public Collection<T> getResultingCoreAggregates();
	public Collection<LeafAggregate> getResultingLeafTableAggregates();
}