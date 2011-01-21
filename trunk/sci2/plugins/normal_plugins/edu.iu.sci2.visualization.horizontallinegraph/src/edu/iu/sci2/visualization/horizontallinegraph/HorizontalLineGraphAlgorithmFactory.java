package edu.iu.sci2.visualization.horizontallinegraph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.cishell.reference.service.metatype.BasicObjectClassDefinition;
import org.cishell.utilities.DateUtilities;
import org.cishell.utilities.MutateParameterUtilities;
import org.osgi.service.log.LogService;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.ObjectClassDefinition;

import prefuse.data.Table;

public class HorizontalLineGraphAlgorithmFactory implements AlgorithmFactory, ParameterMutator {
	public static final String LABEL_FIELD_ID = "label";
	public static final String START_DATE_FIELD_ID = "start_date";
	public static final String END_DATE_FIELD_ID = "end_date";
	public static final String SIZE_BY_FIELD_ID = "size_by";
	public static final String DATE_FORMAT_FIELD_ID = "date_format";
	public static final String PAGE_WIDTH_FIELD_ID = "page_width";
	public static final String PAGE_HEIGHT_FIELD_ID = "page_height";
	public static final String SHOULD_SCALE_OUTPUT_FIELD_ID = "should_scale_output";

    public Algorithm createAlgorithm(
    		Data[] data, Dictionary<String, Object> parameters, CIShellContext ciShellContext) {
    	Data inputData = data[0];
    	Table inputTable = (Table) inputData.getData();
    	LogService logger = (LogService) ciShellContext.getService(LogService.class.getName());
    	String labelColumn = parameters.get(LABEL_FIELD_ID).toString();
    	String startDateColumn = parameters.get(START_DATE_FIELD_ID).toString();
    	String endDateColumn = parameters.get(END_DATE_FIELD_ID).toString();
    	String sizeByColumn = parameters.get(SIZE_BY_FIELD_ID).toString();
    	String startDateFormat = (String) parameters.get(DATE_FORMAT_FIELD_ID);
    	String endDateFormat = (String) parameters.get(DATE_FORMAT_FIELD_ID);
    	double pageWidth = ((Double) parameters.get(PAGE_WIDTH_FIELD_ID)).doubleValue();
    	double pageHeight = ((Double) parameters.get(PAGE_HEIGHT_FIELD_ID)).doubleValue();
    	boolean shouldScaleOutput =
        	((Boolean) parameters.get(SHOULD_SCALE_OUTPUT_FIELD_ID)).booleanValue();

        return new HorizontalLineGraphAlgorithm(
        	inputData,
        	inputTable,
        	logger,
        	labelColumn,
        	startDateColumn,
        	endDateColumn,
        	sizeByColumn,
        	startDateFormat,
        	endDateFormat,
        	pageWidth,
        	pageHeight,
        	shouldScaleOutput);
    }
    
    public ObjectClassDefinition mutateParameters(
    		Data[] data, ObjectClassDefinition oldParameters) {
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
    
    private static Collection<String> formDateFormatOptionLabels() {
    	Collection<String> dateFormatOptionLabels = new ArrayList<String>();
    	dateFormatOptionLabels.add(
    		DateUtilities.MONTH_DAY_YEAR_DATE_FORMAT + " (U.S., e.g. 10/15/2010)");
    	dateFormatOptionLabels.add(
    		DateUtilities.DAY_MONTH_YEAR_DATE_FORMAT + " (Europe, e.g. 15/10/2010)");

    	return dateFormatOptionLabels;
    }
    
    private static Collection<String> formDateFormatOptionValues() {
    	Collection<String> dateFormatOptionValues = new ArrayList<String>();
    	dateFormatOptionValues.add(DateUtilities.MONTH_DAY_YEAR_DATE_FORMAT);
    	dateFormatOptionValues.add(DateUtilities.DAY_MONTH_YEAR_DATE_FORMAT);

    	return dateFormatOptionValues;
    }
}