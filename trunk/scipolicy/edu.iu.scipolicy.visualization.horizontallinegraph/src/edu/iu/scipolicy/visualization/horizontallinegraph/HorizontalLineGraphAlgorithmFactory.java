package edu.iu.scipolicy.visualization.horizontallinegraph;

import java.io.IOException;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.cishell.reference.service.metatype.BasicObjectClassDefinition;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.ObjectClassDefinition;

import prefuse.data.Table;
import edu.iu.scipolicy.utilities.MutateParameterUtilities;

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
					MutateParameterUtilities.formLabelAttributeDefinition
						(oldAttributeDefinition, table);
			}
			else if (oldAttributeDefinitionID.equals
				(HorizontalLineGraphAlgorithm.START_DATE_FIELD_ID) ||
					 oldAttributeDefinitionID.equals
				(HorizontalLineGraphAlgorithm.END_DATE_FIELD_ID))
			{
				newAttributeDefinition =
					MutateParameterUtilities.formDateAttributeDefinition
						(oldAttributeDefinition, table);
			}
			else if (oldAttributeDefinitionID.equals
				(HorizontalLineGraphAlgorithm.SIZE_BY_FIELD_ID))
			{
				newAttributeDefinition =
					MutateParameterUtilities.formIntegerAttributeDefinition
						(oldAttributeDefinition, table);
			}
			
			newParameters.addAttributeDefinition(ObjectClassDefinition.REQUIRED,
												 newAttributeDefinition);
		}
		
    	return newParameters;
    }
}