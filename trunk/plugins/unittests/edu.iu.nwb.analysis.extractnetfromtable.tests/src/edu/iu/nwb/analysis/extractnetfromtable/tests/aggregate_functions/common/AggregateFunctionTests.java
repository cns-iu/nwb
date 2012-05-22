package edu.iu.nwb.analysis.extractnetfromtable.tests.aggregate_functions.common;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import edu.iu.nwb.analysis.extractnetfromtable.aggregate.AbstractAggregateFunction;

/**
 * Useful methods and variables for testing {@link AbstractAggregateFunction}s.
 * 
 * @author dmcoe
 * 
 */
public class AggregateFunctionTests {
	/**
	 * All the types of Integers
	 */
	public static final List<Object> goodIntegerTypes = Collections
			.unmodifiableList(Arrays.asList((Object) new int[] { 1 },
					(Object) new Integer[] { Integer.valueOf(1) }, (Object) 1,
					(Object) Integer.valueOf(1)));
	/**
	 * All the types of Floats
	 */
	public static final List<Object> goodFloatTypes = Collections
			.unmodifiableList(Arrays.asList((Object) new float[] { 1.1f },
					(Object) new Float[] { Float.valueOf(1.1f) },
					(Object) 1.1f, (Object) Float.valueOf(1.1f)));
	/**
	 * All the types of Doubles
	 */
	public static final List<Object> goodDoubleTypes = Collections
			.unmodifiableList(Arrays.asList((Object) new double[] { 1.1d },
					(Object) new Double[] { Double.valueOf(1.1d) },
					(Object) 1.1d, (Object) Double.valueOf(1.1d)));

	/**
	 * All the types of Booleans
	 */
	public static final List<Object> goodBooleanTypes = Collections
			.unmodifiableList(Arrays.asList((Object) new boolean[] { true },
					(Object) new Boolean[] { Boolean.valueOf(true) },
					(Object) true, (Object) Boolean.valueOf(true)));
}
