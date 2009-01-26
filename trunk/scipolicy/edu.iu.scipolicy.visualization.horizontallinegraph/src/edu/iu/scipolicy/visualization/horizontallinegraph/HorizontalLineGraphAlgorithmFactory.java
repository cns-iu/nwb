package edu.iu.scipolicy.visualization.horizontallinegraph;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.cishell.reference.service.metatype.BasicAttributeDefinition;
import org.cishell.reference.service.metatype.BasicObjectClassDefinition;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.ObjectClassDefinition;

import prefuse.data.Schema;
import prefuse.data.Table;

public class HorizontalLineGraphAlgorithmFactory implements AlgorithmFactory, ParameterMutator {
    public Algorithm createAlgorithm(Data[] data,
    								 Dictionary parameters,
    								 CIShellContext context)
    {
        return new HorizontalLineGraphAlgorithm(data, parameters, context);
    }
    
    public ObjectClassDefinition mutateParameters(Data[] data,
    											  ObjectClassDefinition oldParameters)
    {
    	Data inData = data[0];
    	Table table = (Table)inData.getData();
    	BasicObjectClassDefinition newParameters;
    	
		try {
			newParameters =
				new BasicObjectClassDefinition(oldParameters.getID(),
											   oldParameters.getName(),
											   oldParameters.getDescription(),
											   oldParameters.getIcon(16));
		}
		catch (IOException e) {
			newParameters =
				new BasicObjectClassDefinition(oldParameters.getID(),
											   oldParameters.getName(),
											   oldParameters.getDescription(), null);
		}
		
		AttributeDefinition[] oldAttributeDefinitions =
			oldParameters.getAttributeDefinitions(ObjectClassDefinition.ALL);
		
		for (AttributeDefinition oldAttributeDefinition : oldAttributeDefinitions) {
			String oldAttributeDefinitionID = oldAttributeDefinition.getID();
			AttributeDefinition newAttributeDefinition = oldAttributeDefinition;
			
			if (oldAttributeDefinitionID.equals
				(HorizontalLineGraphAlgorithm.LABEL_FIELD_ID))
			{
				newAttributeDefinition =
					formLabelAttributeDefinition(oldAttributeDefinition, table);
			}
			else if (oldAttributeDefinitionID.equals
				(HorizontalLineGraphAlgorithm.START_DATE_FIELD_ID) ||
					 oldAttributeDefinitionID.equals
				(HorizontalLineGraphAlgorithm.END_DATE_FIELD_ID))
			{
				newAttributeDefinition =
					formDateAttributeDefinition(oldAttributeDefinition, table);
			}
			else if (oldAttributeDefinitionID.equals
				(HorizontalLineGraphAlgorithm.SIZE_BY_FIELD_ID))
			{
				newAttributeDefinition =
					formIntegerAttributeDefinition(oldAttributeDefinition, table);
			}
			
			newParameters.addAttributeDefinition(ObjectClassDefinition.REQUIRED,
												 newAttributeDefinition);
		}
		
    	return newParameters;
    }
    
    private String[] filterSchemaColumnNamesByClass(Schema schema, Class objectClass) {
    	ArrayList workingColumnNames = new ArrayList();
    	
    	for (int ii = 0; ii < schema.getColumnCount(); ii++) {
    		if (objectClass.isAssignableFrom(schema.getColumnType(ii)))
    			workingColumnNames.add(schema.getColumnName(ii));
    	}
    	
    	String[] finalColumnNames = new String [workingColumnNames.size()];
    	
    	return (String[])workingColumnNames.toArray(finalColumnNames);
    }
    
    private String[] getValidStringColumnNamesInTable(Table table) {
    	return filterSchemaColumnNamesByClass(table.getSchema(), String.class);
    }
    
    private String[] getValidDateColumnNamesInTable(Table table) {
    	return filterSchemaColumnNamesByClass(table.getSchema(), Date.class);
    }
    
    private String[] getValidIntegerColumnNamesInTable(Table table) {
    	return filterSchemaColumnNamesByClass(table.getSchema(), int.class);
    }
    
    private AttributeDefinition formLabelAttributeDefinition
    	(AttributeDefinition oldAttributeDefinition, Table table)
    {
    	String[] validStringColumnsInTable =
    		getValidStringColumnNamesInTable(table);
    	
    	AttributeDefinition labelAttributeDefinition =
    		new BasicAttributeDefinition(oldAttributeDefinition.getID(),
    									 oldAttributeDefinition.getName(),
    									 oldAttributeDefinition.getDescription(),
    									 AttributeDefinition.STRING,
    									 validStringColumnsInTable,
    									 validStringColumnsInTable);
    	
    	return labelAttributeDefinition;
    }
    
    private AttributeDefinition formDateAttributeDefinition
		(AttributeDefinition oldAttributeDefinition, Table table)
    {
    	String[] validDateColumnsInTable =
    		getValidDateColumnNamesInTable(table);
	
    	AttributeDefinition dateAttributeDefinition =
    		new BasicAttributeDefinition(oldAttributeDefinition.getID(),
    									 oldAttributeDefinition.getName(),
    									 oldAttributeDefinition.getDescription(),
    									 AttributeDefinition.STRING,
    									 validDateColumnsInTable,
    									 validDateColumnsInTable);
	
    	return dateAttributeDefinition;
    }
    
    private AttributeDefinition formIntegerAttributeDefinition
		(AttributeDefinition oldAttributeDefinition, Table table)
    {
    	String[] validIntegerColumnsInTable =
    		getValidIntegerColumnNamesInTable(table);

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