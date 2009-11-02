package edu.iu.scipolicy.preprocessing.aggregatedata;

import java.util.HashSet;
import java.util.Set;

public class GlobalConstants {

	public static final String NONE_NUMERICAL_AGGREGATION_TYPE_VALUE = "NONE";
	public static final String SUM_AGGREGATION_TYPE_VALUE = "SUM";
	public static final String DIFFERENCE_AGGREGATION_TYPE_VALUE = "DIFFERENCE";
	public static final String AVERAGE_AGGREGATION_TYPE_VALUE = "AVERAGE";
	public static final String MIN_AGGREGATION_TYPE_VALUE = "MIN";
	public static final String MAX_AGGREGATION_TYPE_VALUE = "MAX";
	
	public static final String NONE_AGGREGATION_TEXT_DELIMITER = "";
	
	public static final Set INTEGER_CLASS_TYPES = new HashSet() {{
		add(int.class);
		add(Integer.class);
	}};
	
	public static final Set FLOAT_CLASS_TYPES = new HashSet() {{
		add(float.class);
		add(Float.class);
	}}; 
	
	public static final Set DOUBLE_CLASS_TYPES = new HashSet() {{
		add(double.class);
		add(Double.class);
	}}; 
	
	
	public static final Set NUMBER_CLASS_TYPES = new HashSet() {
		{
			addAll(INTEGER_CLASS_TYPES);
			addAll(DOUBLE_CLASS_TYPES);
			addAll(FLOAT_CLASS_TYPES);
		}
	};
	

	

}