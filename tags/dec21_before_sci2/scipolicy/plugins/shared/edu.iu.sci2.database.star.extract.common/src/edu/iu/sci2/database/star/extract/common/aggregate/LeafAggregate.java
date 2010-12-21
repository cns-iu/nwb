package edu.iu.sci2.database.star.extract.common.aggregate;

import java.util.Map;

import edu.iu.sci2.database.star.common.parameter.ColumnDescriptor;

public class LeafAggregate extends Aggregate {
	private String leafTableName;
	private String coreTableName;

	public LeafAggregate(
			String name,
			String functionName,
			String targetColumnName,
			Map<String, ColumnDescriptor> columnDescriptors,
			String leafTableName,
			String coreTableName) {
		super(name, functionName, targetColumnName, columnDescriptors);

		this.leafTableName = leafTableName;
		this.coreTableName = coreTableName;
	}

	@Override
	public String databaseRepresentation() {
		String nameForDatabase = getTargetColumnDescriptor().getNameForDatabase();

		return getFunction().databaseRepresentation(
			String.format("%1$s_1.\"%1$s\"", nameForDatabase), false, getName());
	}

//	public String joinExpression() {
//		String format =
//			"LEFT JOIN %2$s_TO_%1$s %2$s_TO_%1$s_1 ON " +
//				"(\"%2$s\".PK = %2$s_TO_%1$s_1.%2$s_TO_%1$s_%2$s_FOREIGN_KEY)%n" +
//			"LEFT JOIN \"%1$s\" %1$s_1 ON (%2$s_TO_%1$s_1.%2$s_TO_%1$s_%1$s_FOREIGN_KEY = %1$s1.PK)";
//
//		return String.format(format, this.leafTableName, this.coreTableName);
//	}
}