package edu.iu.scipolicy.utilities;

import java.util.ArrayList;
import java.util.Date;

import prefuse.data.Schema;
import prefuse.data.Table;
import prefuse.util.collections.IntIterator;

public class TableUtilities {
	public static String[] filterSchemaColumnNamesByClass
		(Schema schema, Class objectClass) throws ColumnNotFoundException
	{
		ArrayList workingColumnNames = new ArrayList();

		for (int ii = 0; ii < schema.getColumnCount(); ii++) {
			if (objectClass.isAssignableFrom(schema.getColumnType(ii)))
				workingColumnNames.add(schema.getColumnName(ii));
		}
		
		if (workingColumnNames.size() == 0) {
			throw new ColumnNotFoundException
				("No column of type " + objectClass.getName() + " was found.");
		}

		String[] finalColumnNames = new String [workingColumnNames.size()];

		return (String[])workingColumnNames.toArray(finalColumnNames);
	}
	
	public static String[] filterSchemaColumnNamesByClasses
		(Schema schema, Class[] objectClasses) throws ColumnNotFoundException
	{
		ArrayList workingColumnNames = new ArrayList();
		
		for (int ii = 0; ii < schema.getColumnCount(); ii++) {
			for (Class objectClass : objectClasses) {
				if (objectClass.isAssignableFrom(schema.getColumnType(ii))) {
					workingColumnNames.add(schema.getColumnName(ii));
					
					break;
				}
			}
		}
		
		if (workingColumnNames.size() > 0) {
			String[] finalColumnNames = new String [workingColumnNames.size()];

			return (String[])workingColumnNames.toArray(finalColumnNames);
		}
		// An exception is thrown if there is not at least 1 column name.
		else {
			StringBuffer objectClassesString = new StringBuffer();
			objectClassesString.append("[");
			
			for (int ii = 0; ii < objectClasses.length; ii++) {
				objectClassesString.append(objectClasses[ii].getName());
				
				if ((ii + 1) < objectClasses.length) {
					objectClassesString.append(", ");
				}
			}
			
			objectClassesString.append("]");
			
			throw new ColumnNotFoundException
				("No column of types " + objectClassesString + " was found.");
		}
	}
	
	public static String[] getValidStringColumnNamesInTable(Table table)
		throws ColumnNotFoundException
	{
    	return filterSchemaColumnNamesByClass(table.getSchema(), String.class);
    }
    
	public static String[] getValidDateColumnNamesInTable(Table table)
		throws ColumnNotFoundException
	{
		Class[] possibleDateClasses = {
			Date.class,
			int.class,
			Integer.class,
			int[].class,
			Integer[].class
		};
		
    	return filterSchemaColumnNamesByClasses(table.getSchema(),
    											possibleDateClasses);
    }
    
	public static String[] getValidIntegerColumnNamesInTable(Table table)
		throws ColumnNotFoundException
	{
		Class[] possibleIntegerClasses = {
			int.class,
			Integer.class,
			int[].class,
			Integer[].class
		};
		
    	return filterSchemaColumnNamesByClasses(table.getSchema(),
    											possibleIntegerClasses);
    }
	
	public static String[] getValidNumberColumnNamesInTable(Table table)
			throws ColumnNotFoundException {
		Class[] possibleNumberClasses = {
			byte.class,
			byte[].class,
			Byte.class,
			Byte[].class,
			short.class,
			short[].class,
			Short.class,
			Short[].class,
			int.class,
			int[].class,
			Integer.class,
			Integer[].class,
			long.class,
			long[].class,
			Long.class,
			Long[].class,
			float.class,
			float[].class,
			Float.class,
			Float[].class,
			double.class,
			double[].class,
			Double.class,
			Double[].class
		};
		
		return filterSchemaColumnNamesByClasses(table.getSchema(),
												possibleNumberClasses);
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
		// TODO: Add a couple comments in this method
		
		Schema tableSchema = originalTable.getSchema();
		final int numTableRows = originalTable.getRowCount();
		Table newTable = createTableUsingSchema(tableSchema);
		final int numRowsToCopy = Math.min(numTableRows, topN);
		int[] originalTableRowsToCopy = new int [numTableRows];
		
		newTable.addRows(numRowsToCopy);
		
		for (int ii = 0; ii < numTableRows; ii++)
			originalTableRowsToCopy[ii] = iterator.nextInt();

		// TODO: Comment the side-effects here
		
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