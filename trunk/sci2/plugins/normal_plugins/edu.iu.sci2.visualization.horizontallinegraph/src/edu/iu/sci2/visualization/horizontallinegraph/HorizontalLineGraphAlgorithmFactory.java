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
	public static final String PAGE_ORIENTATION_ID = "page_orientation";
	public static final String SHOULD_SCALE_OUTPUT_FIELD_ID = "should_scale_output";
	private static final String QUERY_ID = "query";
	private static final double PAGE_SHORT_DIMENTION = 8.5;
	private static final double PAGE_LONG_DIMENTION = 11;
	/**
	 * Pages can be landscapes or portrait. They are fixed at letter size for
	 * now, but all of the code correctly uses the height and width for
	 * arbitrary page sizes if this needs to be changed later
	 * 
	 */
	public enum PageOrientation {
		LANDSCAPE(PAGE_SHORT_DIMENTION, PAGE_LONG_DIMENTION), PORTRAIT(PAGE_LONG_DIMENTION, PAGE_SHORT_DIMENTION);
		private final double height;
		private final double width;
		
		PageOrientation(double height, double width){
			this.height = height;
			this.width = width;
		}
		
		public String getLabel() {
			
			String label = this.toString();
			String firstLetter = label.substring(0, 1);
			String labelWithoutFirstLetter = label.toLowerCase().substring(1, label.length());
			firstLetter.toUpperCase();
			
			return firstLetter + labelWithoutFirstLetter;

		}
		
		public String getValue() {
			return this.toString();
		}

		public double getHeight() {
			return height;
		}

		public double getWidth() {
			return width;
		}				
	}
	
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
    	String pageOrientation = (String) parameters.get(PAGE_ORIENTATION_ID);
    	String query = (String) parameters.get(QUERY_ID);
    	boolean shouldScaleOutput =
        	((Boolean) parameters.get(SHOULD_SCALE_OUTPUT_FIELD_ID)).booleanValue();
    	
    	PageOrientation orientation = PageOrientation.valueOf(pageOrientation);

    	Double pageWidth = orientation.getWidth();
    	Double pageHeight = orientation.getHeight();
    	
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
        	query,
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
			} else if  (oldAttributeDefinitionID.equals(PAGE_ORIENTATION_ID)) {
				newAttributeDefinition = MutateParameterUtilities.cloneToDropdownAttributeDefinition(oldAttributeDefinition, formOrientationLabels(), formOrientationValues());
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
    
    private static Collection<String> formOrientationLabels() {
    	Collection<String> orientationLabels = new ArrayList<String>(PageOrientation.values().length);
    	for (PageOrientation orientation : PageOrientation.values()){
    		orientationLabels.add(orientation.getLabel());
    	}
    	return orientationLabels;
    }
    
    private static Collection<String> formOrientationValues() {
    	Collection<String> orientationValues = new ArrayList<String>(PageOrientation.values().length);
    	for (PageOrientation orientation : PageOrientation.values()){
    		orientationValues.add(orientation.getValue());
    	}
    	return orientationValues;
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