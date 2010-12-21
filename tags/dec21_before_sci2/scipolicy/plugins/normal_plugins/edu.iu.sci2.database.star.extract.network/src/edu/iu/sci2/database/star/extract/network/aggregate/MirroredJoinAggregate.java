package edu.iu.sci2.database.star.extract.network.aggregate;

import java.util.Map;

import edu.iu.sci2.database.star.common.parameter.ColumnDescriptor;
import edu.iu.sci2.database.star.extract.common.aggregate.Aggregate;

public class MirroredJoinAggregate extends Aggregate {
	private String coreTableName;

	public MirroredJoinAggregate(
			String name,
			String functionName,
			String targetColumnName,
			Map<String, ColumnDescriptor> columnDescriptors,
			String leafTableName,
			String coreTableName) {
		super(name, functionName, targetColumnName, columnDescriptors);
		this.coreTableName = coreTableName;
	}

	@Override
	public String databaseRepresentation() {
		ColumnDescriptor targetColumnDescriptor = getTargetColumnDescriptor();
		String nameForDatabase = targetColumnDescriptor.getNameForDatabase();

		return getFunction().databaseRepresentation(
			String.format("%2$s1.\"%1$s\"", nameForDatabase, this.coreTableName),
			false,
			getName());
	}
}