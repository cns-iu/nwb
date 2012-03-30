package edu.iu.sci2.visualization.temporalbargraph.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.cishell.utilities.DateUtilities;
import org.osgi.service.metatype.ObjectClassDefinition;

public abstract class AbstractTemporalBarGraphAlgorithmFactory implements AlgorithmFactory, ParameterMutator {
	public static final String LABEL_FIELD_ID = "label";
	public static final String START_DATE_FIELD_ID = "start_date";
	public static final String END_DATE_FIELD_ID = "end_date";
	public static final String SIZE_BY_FIELD_ID = "size_by";
	public static final String DATE_FORMAT_FIELD_ID = "date_format";
	public static final String PAGE_ORIENTATION_ID = "page_orientation";
	public static final String SHOULD_SCALE_OUTPUT_FIELD_ID = "should_scale_output";
	public static final String CATEGORY_FIELD_ID = "category";
	public static final String DO_NOT_PROCESS_CATEGORY_VALUE = "No Category Coloring";
	
	@Override
	public abstract Algorithm createAlgorithm(
    		Data[] data, Dictionary<String, Object> parameters, CIShellContext ciShellContext);
    
    @Override
	public abstract ObjectClassDefinition mutateParameters(
    		Data[] data, ObjectClassDefinition oldParameters);
    

    public static Collection<String> formDateFormatOptionLabels() {
    	Collection<String> dateFormatOptionLabels = new ArrayList<String>();
    	dateFormatOptionLabels.add(
    		DateUtilities.MONTH_DAY_YEAR_DATE_FORMAT + " (U.S., e.g. 10/15/2010)");
    	dateFormatOptionLabels.add(
    		DateUtilities.DAY_MONTH_YEAR_DATE_FORMAT + " (Europe, e.g. 15/10/2010)");

    	return dateFormatOptionLabels;
    }
    
    public static Collection<String> formDateFormatOptionValues() {
    	Collection<String> dateFormatOptionValues = new ArrayList<String>();
    	dateFormatOptionValues.add(DateUtilities.MONTH_DAY_YEAR_DATE_FORMAT);
    	dateFormatOptionValues.add(DateUtilities.DAY_MONTH_YEAR_DATE_FORMAT);

    	return dateFormatOptionValues;
    }
}