package edu.iu.epic.visualization.linegraph;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.cishell.reference.service.metatype.BasicAttributeDefinition;
import org.cishell.reference.service.metatype.BasicObjectClassDefinition;
import org.cishell.utilities.MutateParameterUtilities;
import org.cishell.utilities.TableUtilities;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.ObjectClassDefinition;

import prefuse.data.Table;

public class LineGraphAlgorithmFactory
	implements AlgorithmFactory, ParameterMutator {
    public Algorithm createAlgorithm(
    		Data[] data,
    		Dictionary parameters,
    		CIShellContext ciShellContext) {
        return new LineGraphAlgorithm(data, parameters, ciShellContext);
    }
    
    public ObjectClassDefinition mutateParameters(
    		Data[] data, ObjectClassDefinition oldParameters) {
    	Table inputTable = (Table)data[0].getData();
    
    	/*
    	 * In the input table, 
    	 * we must choose one numeric or date column to be the 'time step' column,
    	 * and we must decide which of the other numeric columns should be graphed as lines.
    	 * All other columns will be ignored.
    	 */
    	
    	BasicObjectClassDefinition newParameters =
    		MutateParameterUtilities.createNewParameters(oldParameters);
    	
    	AttributeDefinition[] oldAttributeDefinitions =
			oldParameters.getAttributeDefinitions(ObjectClassDefinition.ALL);
    	
    	for (AttributeDefinition oldAttributeDefinition : oldAttributeDefinitions) {
    		String oldAttributeDefinitionID = oldAttributeDefinition.getID();
    		AttributeDefinition newAttributeDefinition = oldAttributeDefinition;
    		
    		if (LineGraphAlgorithm.TIME_STEP_COLUMN_NAME_KEY.equals(oldAttributeDefinitionID)) {
    			newAttributeDefinition =
					MutateParameterUtilities.formDateAttributeDefinition(
						oldAttributeDefinition, inputTable);
    		}
    		
    		newParameters.addAttributeDefinition(
    			ObjectClassDefinition.REQUIRED, newAttributeDefinition);
    	}
    	
    	String[] validNumberColumnsInTable =
			TableUtilities.getValidNumberColumnNamesInTable(inputTable);
    	
    	for (String numberColumn : validNumberColumnsInTable) {
    		String id = LineGraphAlgorithm.BASE_LINE_COLUMN_NAME_KEY + numberColumn;
    		String name = numberColumn;
    		String description = "Graph column " + numberColumn + "?";
    		int type = AttributeDefinition.BOOLEAN;
    		String defaultValue = Boolean.TRUE.toString();//"true";
    		AttributeDefinition attributeDefinition = new BasicAttributeDefinition(
    			id, name, description, type, defaultValue);
    		
    		newParameters.addAttributeDefinition(
    			ObjectClassDefinition.REQUIRED, attributeDefinition);
    	}
    	
    	//TODO: Stub. Fill this out
    	return newParameters;
    }
}