package edu.iu.scipolicy.visualization.horizontallinegraph;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.cishell.reference.service.metatype.BasicObjectClassDefinition;
import org.cishell.utilities.DateUtilities;
import org.cishell.utilities.MutateParameterUtilities;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.ObjectClassDefinition;

import prefuse.data.Table;

public class HorizontalLineGraphAlgorithmFactory
		implements AlgorithmFactory, ParameterMutator {
    @SuppressWarnings("unchecked") // Raw Dictionary
    public Algorithm createAlgorithm(Data[] data,
    								 Dictionary parameters,
    								 CIShellContext ciShellContext) {
        return new HorizontalLineGraphAlgorithm(
        	data, parameters, ciShellContext);
    }
    
    public ObjectClassDefinition mutateParameters(
    		Data[] data, ObjectClassDefinition oldParameters) {
    	Data inData = data[0];
    	Table table = (Table) inData.getData();
    	
		BasicObjectClassDefinition newParameters =
			MutateParameterUtilities.createNewParameters(oldParameters);
		
		AttributeDefinition[] oldAttributeDefinitions =
			oldParameters.getAttributeDefinitions(ObjectClassDefinition.ALL);
		
		for (AttributeDefinition oldAttributeDefinition :
				oldAttributeDefinitions) {
			String oldAttributeDefinitionID = oldAttributeDefinition.getID();
			AttributeDefinition newAttributeDefinition =
				oldAttributeDefinition;
			
			if (oldAttributeDefinitionID.equals(
					HorizontalLineGraphAlgorithm.LABEL_FIELD_ID)) {
				newAttributeDefinition =
					MutateParameterUtilities.formLabelAttributeDefinition(
						oldAttributeDefinition, table);
			} else if (oldAttributeDefinitionID.equals(
						HorizontalLineGraphAlgorithm.START_DATE_FIELD_ID) ||
					oldAttributeDefinitionID.equals(
						HorizontalLineGraphAlgorithm.END_DATE_FIELD_ID)) {
				newAttributeDefinition =
					MutateParameterUtilities.formDateAttributeDefinition(
						oldAttributeDefinition, table);
			} else if (oldAttributeDefinitionID.equals(
					HorizontalLineGraphAlgorithm.SIZE_BY_FIELD_ID)) {
				newAttributeDefinition =
					MutateParameterUtilities.formNumberAttributeDefinition(
						oldAttributeDefinition, table);
			} else if (oldAttributeDefinitionID.equals(
					HorizontalLineGraphAlgorithm.DATE_FORMAT_FIELD_ID)) {
				String[] dateFormatOptions = formDateFormatOptions();
				// TODO: form labels (so the tool displays "NIH Date Format")
				newAttributeDefinition = MutateParameterUtilities.
					cloneToDropdownAttributeDefinition(
						oldAttributeDefinition,
						dateFormatOptions,
						dateFormatOptions);
			}
			
			/* TODO This can take optional ADs and mutate them needlessly into
			 * required ones, so be careful.
			 */
			newParameters.addAttributeDefinition(
				ObjectClassDefinition.REQUIRED, newAttributeDefinition);
		}
		
    	return newParameters;
    }
    
    private static String[] formDateFormatOptions() {
    	return new String[] {
    		DateUtilities.MONTH_DAY_DATE_FORMAT,
    		DateUtilities.DAY_MONTH_DATE_FORMAT,
    	};
    }
}