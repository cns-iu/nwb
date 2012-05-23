package edu.iu.sci2.visualization.temporalbargraph.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.List;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.Data;
import org.cishell.reference.service.metatype.BasicObjectClassDefinition;
import org.cishell.utilities.MutateParameterUtilities;
import org.osgi.service.log.LogService;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.ObjectClassDefinition;

import prefuse.data.Table;
import edu.iu.sci2.visualization.temporalbargraph.common.AbstractTemporalBarGraphAlgorithmFactory;

public class WebTemporalBarGraphAlgorithmFactory extends
		AbstractTemporalBarGraphAlgorithmFactory {

	@Override
	public Algorithm createAlgorithm(Data[] data,
			Dictionary<String, Object> parameters, CIShellContext ciShellContext) {
		Data inputData = data[0];
		Table inputTable = (Table) inputData.getData();
		LogService logger = (LogService) ciShellContext
				.getService(LogService.class.getName());
		/**
		 * XXX
		 * It is critical that no parameters are added here that are not
		 * accessible for the Print AlgorithmFactory because it has a parameter
		 * that will invoke this algorithm factory with its parameters!
		 */
		String labelColumn = parameters.get(LABEL_FIELD_ID).toString();
		String startDateColumn = parameters.get(START_DATE_FIELD_ID).toString();
		String endDateColumn = parameters.get(END_DATE_FIELD_ID).toString();
		String sizeByColumn = parameters.get(SIZE_BY_FIELD_ID).toString();
		String startDateFormat = (String) parameters.get(DATE_FORMAT_FIELD_ID);
		String endDateFormat = (String) parameters.get(DATE_FORMAT_FIELD_ID);
		boolean shouldScaleOutput = ((Boolean) parameters
				.get(SHOULD_SCALE_OUTPUT_FIELD_ID)).booleanValue();
		String category = (String) parameters.get(CATEGORY_FIELD_ID);

		return new WebTemporalBarGraphAlgorithm(inputData, inputTable, logger,
				labelColumn, startDateColumn, endDateColumn, sizeByColumn,
				startDateFormat, endDateFormat, shouldScaleOutput, category);
	}

	@Override
	public ObjectClassDefinition mutateParameters(Data[] data,
			ObjectClassDefinition oldParameters) {
		Data inputData = data[0];
		Table table = (Table) inputData.getData();

		BasicObjectClassDefinition newParameters = MutateParameterUtilities
				.createNewParameters(oldParameters);

		AttributeDefinition[] oldAttributeDefinitions = oldParameters
				.getAttributeDefinitions(ObjectClassDefinition.ALL);

		for (AttributeDefinition oldAttributeDefinition : oldAttributeDefinitions) {
			String oldAttributeDefinitionID = oldAttributeDefinition.getID();
			AttributeDefinition newAttributeDefinition = oldAttributeDefinition;

			if (oldAttributeDefinitionID.equals(LABEL_FIELD_ID)) {
				newAttributeDefinition = MutateParameterUtilities
						.formLabelAttributeDefinition(oldAttributeDefinition,
								table);
			} else if (oldAttributeDefinitionID.equals(START_DATE_FIELD_ID)
					|| oldAttributeDefinitionID.equals(END_DATE_FIELD_ID)) {
				newAttributeDefinition = MutateParameterUtilities
						.formDateAttributeDefinition(oldAttributeDefinition,
								table);
			} else if (oldAttributeDefinitionID.equals(SIZE_BY_FIELD_ID)) {
				newAttributeDefinition = MutateParameterUtilities
						.formNumberAttributeDefinition(oldAttributeDefinition,
								table);
			} else if (oldAttributeDefinitionID.equals(DATE_FORMAT_FIELD_ID)) {
				Collection<String> dateFormatLabels = formDateFormatOptionLabels();
				Collection<String> dateFormatOptions = formDateFormatOptionValues();
				newAttributeDefinition = MutateParameterUtilities
						.cloneToDropdownAttributeDefinition(
								oldAttributeDefinition, dateFormatLabels,
								dateFormatOptions);
			} else if (oldAttributeDefinitionID.equals(CATEGORY_FIELD_ID)) {
				List<String> additionalOptions = new ArrayList<String>();
				additionalOptions.add(DO_NOT_PROCESS_CATEGORY_VALUE);

				newAttributeDefinition = MutateParameterUtilities
						.formLabelAttributeDefinition(oldAttributeDefinition,
								table, additionalOptions);
				MutateParameterUtilities.mutateDefaultValue(oldParameters,
						CATEGORY_FIELD_ID, DO_NOT_PROCESS_CATEGORY_VALUE);
			}

			/*
			 * This can take optional ADs and mutate them needlessly into
			 * required ones, so be careful.
			 */
			newParameters.addAttributeDefinition(
					ObjectClassDefinition.REQUIRED, newAttributeDefinition);
		}

		return newParameters;
	}

}
