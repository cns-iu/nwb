package edu.iu.sci2.database.star.extract.common.aggregate;

import java.util.Map;

import edu.iu.sci2.database.star.common.parameter.ColumnDescriptor;

public class LeafAggregate extends Aggregate {
	public LeafAggregate(
			String name,
			String functionName,
			String targetColumnName,
			Map<String, ColumnDescriptor> columnDescriptors,
			String leafTableName,
			String coreTableName) {
		super(name, functionName, targetColumnName, columnDescriptors);
	}

	/** In our current design, leaf tables can only have one column--the column from the original
	 * CSV file.
	 * Of course, the database table created from the original CSV column AND that table's only
	 * real column need names in the database.
	 * A natural way to create a name is based off of the original CSV's column name, but
	 * normalized for database compatibility.
	 * Because we use the same name for both the database table name and that table's column name,
	 * all we need is the target column's name (hence getTargetColumnDescriptor()) to also know
	 * the database table's name.
	 */
	@Override
	public String databaseRepresentation() {
		String nameForDatabase = getTargetColumnDescriptor().getNameForDatabase();

		return getFunction().databaseRepresentation(
			String.format("%1$s_1.\"%1$s\"", nameForDatabase), false, getName());
	}
}