package edu.iu.scipolicy.filtering.topncommon;

import java.io.IOException;

import org.cishell.framework.data.Data;
import org.cishell.reference.service.metatype.BasicObjectClassDefinition;
import org.cishell.utilities.ColumnNotFoundException;
import org.cishell.utilities.MutateParameterUtilities;
import org.cishell.utilities.TableUtilities;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.ObjectClassDefinition;

import prefuse.data.Table;
import prefuse.util.collections.IntIterator;

public class TopNUtilities {
	// Must agree with id in METADATA.XML.
	public static final String TOP_N_ID = "topN";
	public static final String TOP_N_PERCENT_ID = "topNPercent";
	public static final String COLUMN_TO_SORT_BY_ID = "columnToSortBy";
	public static final String SHOULD_SORT_ID = "shouldSort";
	public static final String IS_DESCENDING_ID = "isDescending";
	
	public static ObjectClassDefinition mutateParameters
		(Data[] data, ObjectClassDefinition parameters)
	{
		// Mutating parameters to allow user to choose which column they want to
		// sort the table rows by.

		ObjectClassDefinition oldDefinition = parameters;

		// Create a new empty object class definition.
		BasicObjectClassDefinition newDefinition;
		
		try {
			newDefinition =
				new BasicObjectClassDefinition(oldDefinition.getID(),
											   oldDefinition.getName(),
											   oldDefinition.getDescription(),
											   oldDefinition.getIcon(16));
		}
		catch (IOException e) {
			newDefinition =
				new BasicObjectClassDefinition(oldDefinition.getID(),
											   oldDefinition.getName(),
											   oldDefinition.getDescription(),
											   null);
		}

		// Fill the new object class definition with the old definition's
		// attributes.
		// As we pass old attributes into the new definition, modify as desired.

		Table table = (Table)data[0].getData();
		
		AttributeDefinition[] definitions =
			oldDefinition.getAttributeDefinitions(ObjectClassDefinition.ALL);

		for (int ii = 0; ii < definitions.length; ii++) {
			AttributeDefinition oldAttribute = definitions[ii];
			String attributeID = oldAttribute.getID();
			AttributeDefinition newAttribute = oldAttribute;
				
			if (attributeID.equals(COLUMN_TO_SORT_BY_ID)) {
				newAttribute = 
					 MutateParameterUtilities.formIntegerAttributeDefinition
					 	(oldAttribute, table);
			}
			
			newDefinition.addAttributeDefinition
				(ObjectClassDefinition.REQUIRED, newAttribute);
		}

		return newDefinition;
	}
	
	public static Table sortTableWithOnlyTopN(Table originalTable,
											  String columnToSortBy,
											  boolean isDescending,
											  int topN)
		throws ColumnNotFoundException
	{
		// Prefuse doesn't handle columns not being found well, so let's do it
		// ourselves.
		if (originalTable.getColumnNumber(columnToSortBy) == -1) {
			throw new ColumnNotFoundException("The column \'" + columnToSortBy +
				"\' could not be found in table: " + originalTable);
		}
		
		// If the original table has no rows, just return a new empty table with the
		// original table's schema.
		if ((originalTable.getRowCount() == 0) || (topN <= 0))
			return TableUtilities.createTableUsingSchema(originalTable.getSchema());
		
		// Prefuse has a bug where it only returns one row if the second value
		// passed in to rowsSortedBy is false, so we have to pass in true and take
		// care of the ascending/descending issue elsewhere.
		IntIterator iteratorOverSortedTableTuples =
			originalTable.rowsSortedBy(columnToSortBy, true);
		
		return TableUtilities.
			copyNRowsFromTableUsingIntIterator(originalTable,
											   iteratorOverSortedTableTuples,
											   topN,
											   isDescending);
	}
}