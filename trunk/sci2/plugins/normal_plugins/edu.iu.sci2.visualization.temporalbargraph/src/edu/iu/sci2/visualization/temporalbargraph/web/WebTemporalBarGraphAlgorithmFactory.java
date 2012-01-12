package edu.iu.sci2.visualization.temporalbargraph.web;

import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Dictionary;

import org.antlr.stringtemplate.StringTemplateGroup;
import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.Data;
import org.cishell.reference.service.metatype.BasicObjectClassDefinition;
import org.cishell.utilities.MutateParameterUtilities;
import org.osgi.service.log.LogService;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.ObjectClassDefinition;

import edu.iu.sci2.visualization.temporalbargraph.common.AbstractTemporalBarGraphAlgorithmFactory;

import prefuse.data.Table;

public class WebTemporalBarGraphAlgorithmFactory extends
		AbstractTemporalBarGraphAlgorithmFactory {
	public static final String STRING_TEMPLATE_FILE_PATH =		 
			"/edu/iu/sci2/visualization/temporalbargraph/stringtemplates/web_temporal_bar_graph.st";
	
	public static StringTemplateGroup loadTemplates() {
		return new StringTemplateGroup(
				new InputStreamReader(
					AbstractTemporalBarGraphAlgorithmFactory.class.getResourceAsStream(
						STRING_TEMPLATE_FILE_PATH)));
	}
	
	@Override
	public Algorithm createAlgorithm(Data[] data,
			Dictionary<String, Object> parameters, CIShellContext ciShellContext) {
		Data inputData = data[0];
    	Table inputTable = (Table) inputData.getData();
    	LogService logger = (LogService) ciShellContext.getService(LogService.class.getName());
    	String labelColumn = parameters.get(LABEL_FIELD_ID).toString();
    	String startDateColumn = parameters.get(START_DATE_FIELD_ID).toString();
    	String endDateColumn = parameters.get(END_DATE_FIELD_ID).toString();
    	String sizeByColumn = parameters.get(SIZE_BY_FIELD_ID).toString();
    	String startDateFormat = (String) parameters.get(DATE_FORMAT_FIELD_ID);
    	String endDateFormat = (String) parameters.get(DATE_FORMAT_FIELD_ID);
    	boolean shouldScaleOutput =
            	((Boolean) parameters.get(SHOULD_SCALE_OUTPUT_FIELD_ID)).booleanValue();
    	
    	return new WebTemporalBarGraphAlgorithm(inputData,
            	inputTable,
            	logger,
            	labelColumn,
            	startDateColumn,
            	endDateColumn,
            	sizeByColumn,
            	startDateFormat,
            	endDateFormat,
            	shouldScaleOutput);
	}

	@Override
	public ObjectClassDefinition mutateParameters(Data[] data,
			ObjectClassDefinition oldParameters) {
		Data inputData = data[0];
    	Table table = (Table) inputData.getData();
    	
		BasicObjectClassDefinition newParameters =
			MutateParameterUtilities.createNewParameters(oldParameters);
		
		AttributeDefinition[] oldAttributeDefinitions =
			oldParameters.getAttributeDefinitions(ObjectClassDefinition.ALL);
		
		for (AttributeDefinition oldAttributeDefinition : oldAttributeDefinitions) {
			String oldAttributeDefinitionID = oldAttributeDefinition.getID();
			AttributeDefinition newAttributeDefinition = oldAttributeDefinition;
			
			if (oldAttributeDefinitionID.equals(LABEL_FIELD_ID)) {
				newAttributeDefinition = MutateParameterUtilities.formLabelAttributeDefinition(
					oldAttributeDefinition, table);
			} else if (oldAttributeDefinitionID.equals(START_DATE_FIELD_ID) ||
					oldAttributeDefinitionID.equals(END_DATE_FIELD_ID)) {
				newAttributeDefinition = MutateParameterUtilities.formDateAttributeDefinition(
					oldAttributeDefinition, table);
			} else if (oldAttributeDefinitionID.equals(SIZE_BY_FIELD_ID)) {
				newAttributeDefinition = MutateParameterUtilities.formNumberAttributeDefinition(
					oldAttributeDefinition, table);
			} else if (oldAttributeDefinitionID.equals(DATE_FORMAT_FIELD_ID)) {
				Collection<String> dateFormatLabels = formDateFormatOptionLabels();
				Collection<String> dateFormatOptions = formDateFormatOptionValues();
				newAttributeDefinition =
					MutateParameterUtilities.cloneToDropdownAttributeDefinition(
						oldAttributeDefinition, dateFormatLabels, dateFormatOptions);
			}
			
			/* TODO: This can take optional ADs and mutate them needlessly into
			 * required ones, so be careful.
			 */
			newParameters.addAttributeDefinition(
				ObjectClassDefinition.REQUIRED, newAttributeDefinition);
		}
		
    	return newParameters;
	}

}
