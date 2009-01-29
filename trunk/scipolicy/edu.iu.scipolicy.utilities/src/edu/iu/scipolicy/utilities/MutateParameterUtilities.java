package edu.iu.scipolicy.utilities;

import org.cishell.reference.service.metatype.BasicAttributeDefinition;
import org.osgi.service.metatype.AttributeDefinition;

import prefuse.data.Table;

public class MutateParameterUtilities {
	public static AttributeDefinition formLabelAttributeDefinition
		(AttributeDefinition oldAttributeDefinition, Table table)
	{
		String[] validStringColumnsInTable =
			TableUtilities.getValidStringColumnNamesInTable(table);
	
		AttributeDefinition labelAttributeDefinition =
			new BasicAttributeDefinition(oldAttributeDefinition.getID(),
										 oldAttributeDefinition.getName(),
										 oldAttributeDefinition.getDescription(),
										 AttributeDefinition.STRING,
										 validStringColumnsInTable,
										 validStringColumnsInTable);
	
		return labelAttributeDefinition;
	}

	public static AttributeDefinition formDateAttributeDefinition
		(AttributeDefinition oldAttributeDefinition, Table table)
	{
		String[] validDateColumnsInTable =
			TableUtilities.getValidDateColumnNamesInTable(table);

		AttributeDefinition dateAttributeDefinition =
			new BasicAttributeDefinition(oldAttributeDefinition.getID(),
										 oldAttributeDefinition.getName(),
										 oldAttributeDefinition.getDescription(),
										 AttributeDefinition.STRING,
										 validDateColumnsInTable,
										 validDateColumnsInTable);

		return dateAttributeDefinition;
	}

	public static AttributeDefinition formIntegerAttributeDefinition
		(AttributeDefinition oldAttributeDefinition, Table table)
	{
		String[] validIntegerColumnsInTable =
			TableUtilities.getValidIntegerColumnNamesInTable(table);

		AttributeDefinition integerAttributeDefinition =
			new BasicAttributeDefinition(oldAttributeDefinition.getID(),
										 oldAttributeDefinition.getName(),
										 oldAttributeDefinition.getDescription(),
										 AttributeDefinition.STRING,
										 validIntegerColumnsInTable,
										 validIntegerColumnsInTable);

		return integerAttributeDefinition;
	}
}