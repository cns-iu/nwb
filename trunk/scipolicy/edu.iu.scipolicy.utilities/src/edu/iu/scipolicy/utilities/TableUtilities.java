package edu.iu.scipolicy.utilities;

import java.util.ArrayList;
import java.util.Date;

import prefuse.data.Schema;
import prefuse.data.Table;
import prefuse.util.collections.IntIterator;

public class TableUtilities {
	public static String[] filterSchemaColumnNamesByClass(Schema schema,
														  Class objectClass)
	{
		ArrayList workingColumnNames = new ArrayList();

		for (int ii = 0; ii < schema.getColumnCount(); ii++) {
			if (objectClass.isAssignableFrom(schema.getColumnType(ii)))
				workingColumnNames.add(schema.getColumnName(ii));
		}

		String[] finalColumnNames = new String [workingColumnNames.size()];

		return (String[])workingColumnNames.toArray(finalColumnNames);
	}
	
	public static String[] getValidStringColumnNamesInTable(Table table) {
    	return filterSchemaColumnNamesByClass(table.getSchema(), String.class);
    }
    
	public static String[] getValidDateColumnNamesInTable(Table table) {
    	return filterSchemaColumnNamesByClass(table.getSchema(), Date.class);
    }
    
	public static String[] getValidIntegerColumnNamesInTable(Table table) {
    	return filterSchemaColumnNamesByClass(table.getSchema(), int.class);
    }
	
	public static Table createTableUsingSchema(Schema tableSchema) {
		final int numTableColumns = tableSchema.getColumnCount();
		Table table = new Table();
		
		for (int ii = 0; ii < numTableColumns; ii++) {
			table.addColumn(tableSchema.getColumnName(ii),
							tableSchema.getColumnType(ii));
		}
		
		return table;
	}
	
	public static void copyTableRow(int newTableRow,
									int originalTableRow,
									Table newTable,
									Table originalTable)
	{
		final int numTableColumns = originalTable.getColumnCount();
		
		for (int ii = 0; ii < numTableColumns; ii++)
			newTable.set(newTableRow, ii, originalTable.get(originalTableRow, ii));
	}
	
	public static Table
		copyNRowsFromTableUsingIntIterator(Table originalTable,
										   IntIterator iterator,
										   int topN,
										   boolean isDescending)
	{
		//TODO: Add a couple comments in this method
		
		Schema tableSchema = originalTable.getSchema();
		final int numTableRows = originalTable.getRowCount();
		Table newTable = createTableUsingSchema(tableSchema);
		final int numRowsToCopy = Math.min(numTableRows, topN);
		int[] originalTableRowsToCopy = new int [numTableRows];
		
		newTable.addRows(numRowsToCopy);
		
		for (int ii = 0; ii < numTableRows; ii++)
			originalTableRowsToCopy[ii] = iterator.nextInt();

		//TODO: Comment the side-effects here
		
		if (!isDescending) {
			for (int ii = 0; ii < numRowsToCopy; ii++) {
				copyTableRow
					(ii, originalTableRowsToCopy[ii], newTable, originalTable);
			}
		}
		else {
			for (int ii = 0; ii < numRowsToCopy; ii++)
			{
				copyTableRow(ii,
							 originalTableRowsToCopy[numTableRows - ii - 1],
							 newTable,
							 originalTable);
			}
		}

		return newTable;
	}
}