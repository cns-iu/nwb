package edu.iu.sci2.visualization.horizontalbargraph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.List;

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
	public static final List<String> LABELS_TO_ADD_FOR_COLORIZATION =
		Collections.unmodifiableList(Arrays.asList(HorizontalBarGraphAlgorithm.NO_COLORIZED_BY));

    public Algorithm createAlgorithm(
    		Data[] data, Dictionary<String, Object> parameters, CIShellContext ciShellContext) {
        return new HorizontalBarGraphAlgorithm(data, parameters, ciShellContext);
    }
    
    public ObjectClassDefinition mutateParameters(
    		Data[] data, ObjectClassDefinition oldParameters) {
    	try {
    		Data inData = data[0];
    		Table table = (Table) inData.getData();

			BasicObjectClassDefinition newParameters =
				MutateParameterUtilities.createNewParameters(oldParameters);

			AttributeDefinition[] oldAttributeDefinitions =
				oldParameters.getAttributeDefinitions(ObjectClassDefinition.ALL);
			Collection<String> scalingFunctionLabels = formScalingFunctionLabels();
			Collection<String> scalingFunctionOptions = formScalingFunctionOptions();
			Collection<String> dateFormatLabels = formDateFormatLabels();
			Collection<String> dateFormatOptions = formDateFormatOptions();

			for (AttributeDefinition oldAttributeDefinition : oldAttributeDefinitions) {
				String oldAttributeDefinitionID = oldAttributeDefinition.getID();
				AttributeDefinition newAttributeDefinition = oldAttributeDefinition;

				if (oldAttributeDefinitionID.equals(HorizontalBarGraphAlgorithm.LABEL_FIELD_ID)) {
					newAttributeDefinition = MutateParameterUtilities.formLabelAttributeDefinition(
						oldAttributeDefinition, table);
				} else if (oldAttributeDefinitionID.equals(HorizontalBarGraphAlgorithm.COLORIZED_BY_FIELD_ID)) {
					newAttributeDefinition = MutateParameterUtilities.formLabelAttributeDefinition(
							oldAttributeDefinition, table, LABELS_TO_ADD_FOR_COLORIZATION);
				}else if (oldAttributeDefinitionID.equals(
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
						HorizontalBarGraphAlgorithm.SCALING_FUNCTION_FIELD_ID)) {
					newAttributeDefinition =
						MutateParameterUtilities.cloneToDropdownAttributeDefinition(
							oldAttributeDefinition, scalingFunctionLabels, scalingFunctionOptions);
				} else if (oldAttributeDefinitionID.equals(
						HorizontalBarGraphAlgorithm.DATE_FORMAT_FIELD_ID)) {
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
    	} catch (Exception e) {
    		String exceptionMessage =
    			"An error occurred when preparing to generate your visualization.  " +
    			" Please submit this entire message to the Help Desk: \"" + e.getMessage() + "\"";
    		throw new RuntimeException(exceptionMessage, e);
    	}
    }

    private static Collection<String> formScalingFunctionLabels() {
    	Collection<String> scalingFunctionLabels = new HashSet<String>();
    	scalingFunctionLabels.add(ScalingFunction.LINEAR_SCALING_FUNCTION_NAME);
    	scalingFunctionLabels.add(ScalingFunction.LOGARITHMIC_SCALING_FUNCTION_NAME);

    	return scalingFunctionLabels;
    }

    private static Collection<String> formScalingFunctionOptions() {
    	Collection<String> scalingFunctionOptions = new HashSet<String>();
    	scalingFunctionOptions.add(ScalingFunction.LINEAR_SCALING_FUNCTION_NAME);
    	scalingFunctionOptions.add(ScalingFunction.LOGARITHMIC_SCALING_FUNCTION_NAME);

    	return scalingFunctionOptions;
    }
    
    private static Collection<String> formDateFormatLabels() {
    	Collection<String> dateFormatLabels = new HashSet<String>();
    	dateFormatLabels.add(
    		DateUtilities.MONTH_DAY_YEAR_DATE_FORMAT + " (U.S., e.g. 10/31/2010)");
    	dateFormatLabels.add(
    		DateUtilities.DAY_MONTH_YEAR_DATE_FORMAT + " (Europe, e.g. 31/10/2010)");

    	return dateFormatLabels;
    }
    
    private static Collection<String> formDateFormatOptions() {
    	Collection<String> dateFormatOptions = new HashSet<String>();
    	dateFormatOptions.add(DateUtilities.MONTH_DAY_YEAR_DATE_FORMAT);
    	dateFormatOptions.add(DateUtilities.DAY_MONTH_YEAR_DATE_FORMAT);

    	return dateFormatOptions;
    }
}