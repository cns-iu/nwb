package edu.iu.sci2.preprocessing.aggregatedata;

import java.io.Serializable;

import com.google.common.collect.ImmutableSet;

public final class GlobalConstants {

	public static final String NONE_NUMERICAL_AGGREGATION_TYPE_VALUE = "NONE";
	public static final String SUM_AGGREGATION_TYPE_VALUE = "SUM";
	public static final String DIFFERENCE_AGGREGATION_TYPE_VALUE = "DIFFERENCE";
	public static final String AVERAGE_AGGREGATION_TYPE_VALUE = "AVERAGE";
	public static final String MIN_AGGREGATION_TYPE_VALUE = "MIN";
	public static final String MAX_AGGREGATION_TYPE_VALUE = "MAX";
	
	public static final String NONE_AGGREGATION_TEXT_DELIMITER = "";
	
	public static final ImmutableSet<Class<? extends Serializable>> INTEGER_CLASS_TYPES 
		= ImmutableSet.of(int.class, int[].class, Integer.class, Integer[].class);	
	
	public static final ImmutableSet<Class<? extends Serializable>> FLOAT_CLASS_TYPES 
		= ImmutableSet.of(float.class, float[].class, Float.class, Float[].class);
	
	public static final ImmutableSet<Class<? extends Serializable>> DOUBLE_CLASS_TYPES 
		= ImmutableSet.of(double.class, double[].class, Double.class, Double[].class);
	
	public static final ImmutableSet<Class<? extends Serializable>> LONG_CLASS_TYPES 
		= ImmutableSet.of(long.class, long[].class, Long.class, Long[].class);
	
	public static final ImmutableSet<Class<? extends Serializable>> NUMBER_CLASS_TYPES = 
			new ImmutableSet.Builder<Class<? extends Serializable>>()
				.addAll(INTEGER_CLASS_TYPES)
				.addAll(FLOAT_CLASS_TYPES)
				.addAll(DOUBLE_CLASS_TYPES)
				.addAll(LONG_CLASS_TYPES)
				.build();
	
	// Utility class, so don't instantiate.
	private GlobalConstants() {}	
}