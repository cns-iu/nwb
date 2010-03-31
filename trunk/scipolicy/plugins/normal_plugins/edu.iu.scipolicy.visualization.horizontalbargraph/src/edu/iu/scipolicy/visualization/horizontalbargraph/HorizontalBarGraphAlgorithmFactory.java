package edu.iu.scipolicy.visualization.horizontalbargraph;

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

public class HorizontalBarGraphAlgorithmFactory implements AlgorithmFactory, ParameterMutator {
    public Algorithm createAlgorithm(
    		Data[] data, Dictionary parameters, CIShellContext ciShellContext) {
        return new HorizontalBarGraphAlgorithm(data, parameters, ciShellContext);
    }
    
    public ObjectClassDefinition mutateParameters(
    		Data[] data, ObjectClassDefinition oldParameters) {
//    	try {
    		Data inData = data[0];
    		Table table = (Table) inData.getData();

			BasicObjectClassDefinition newParameters =
				MutateParameterUtilities.createNewParameters(oldParameters);

			AttributeDefinition[] oldAttributeDefinitions =
				oldParameters.getAttributeDefinitions(ObjectClassDefinition.ALL);

			for (AttributeDefinition oldAttributeDefinition : oldAttributeDefinitions) {
				String oldAttributeDefinitionID = oldAttributeDefinition.getID();
				AttributeDefinition newAttributeDefinition = oldAttributeDefinition;

				if (oldAttributeDefinitionID.equals(HorizontalBarGraphAlgorithm.LABEL_FIELD_ID)) {
					newAttributeDefinition = MutateParameterUtilities.formLabelAttributeDefinition(
						oldAttributeDefinition, table);
				} else if (oldAttributeDefinitionID.equals(
							HorizontalBarGraphAlgorithm.START_DATE_FIELD_ID) ||
						oldAttributeDefinitionID.equals(
							HorizontalBarGraphAlgorithm.END_DATE_FIELD_ID)) {
					newAttributeDefinition = MutateParameterUtilities.formDateAttributeDefinition(
						oldAttributeDefinition, table);
				} else if (oldAttributeDefinitionID.equals(
						HorizontalBarGraphAlgorithm.SIZE_BY_FIELD_ID)) {
					newAttributeDefinition = MutateParameterUtilities.formNumberAttributeDefinition(
						oldAttributeDefinition, table);
				} else if (oldAttributeDefinitionID.equals(
						HorizontalBarGraphAlgorithm.DATE_FORMAT_FIELD_ID)) {
					String[] dateFormatLabels = formDateFormatLabels();
					String[] dateFormatOptions = formDateFormatOptions();
					newAttributeDefinition =
						MutateParameterUtilities.cloneToDropdownAttributeDefinition(
							oldAttributeDefinition, dateFormatLabels, dateFormatOptions);
				}

				/* TODO This can take optional ADs and mutate them needlessly into
				 * required ones, so be careful.
				 */
				newParameters.addAttributeDefinition(
					ObjectClassDefinition.REQUIRED, newAttributeDefinition);
			}

    		return newParameters;
//    	} catch (Exception e) {
//    		String exceptionMessage =
//    			"An error occurred when preparing to generate your visualization.  " +
//    			" Please submit this entire message to the Help Desk: \"" + e.getMessage() + "\"";
//    		throw new RuntimeException(exceptionMessage, e);
//    	}
    }
    
    private static String[] formDateFormatLabels() {
    	return new String[] {
    		(DateUtilities.MONTH_DAY_YEAR_DATE_FORMAT + " (U.S., e.g. 10/31/2010)"),
    		(DateUtilities.DAY_MONTH_YEAR_DATE_FORMAT + " (Europe, e.g. 31/10/2010)")
    	};
    }
    
    private static String[] formDateFormatOptions() {
    	return new String[] {
    		DateUtilities.MONTH_DAY_YEAR_DATE_FORMAT, DateUtilities.DAY_MONTH_YEAR_DATE_FORMAT,
    	};
    }
}