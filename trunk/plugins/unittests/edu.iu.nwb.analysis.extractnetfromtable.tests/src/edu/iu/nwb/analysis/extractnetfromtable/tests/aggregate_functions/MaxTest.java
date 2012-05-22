package edu.iu.nwb.analysis.extractnetfromtable.tests.aggregate_functions;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

import edu.iu.nwb.analysis.extractnetfromtable.aggregate.AbstractAggregateFunction;
import edu.iu.nwb.analysis.extractnetfromtable.aggregate.MaxFunctionFactory;
import edu.iu.nwb.analysis.extractnetfromtable.tests.aggregate_functions.common.AggregateFunctionTests;

public class MaxTest {

	/**
	 * Test that the maxFactory returns appropriately typed functions.
	 */
	@Test
	public void testMaxFactoryTypes() {

		MaxFunctionFactory maxFactory = new MaxFunctionFactory();

		testMaxFactoryInteger(maxFactory);

		testMaxFactoryFloat(maxFactory);

		testMaxFactoryDouble(maxFactory);

		testMaxFactoryBoolean(maxFactory);
	}

	@SuppressWarnings("static-method")
	private void testMaxFactoryBoolean(MaxFunctionFactory maxFactory) {
		// Boolean is not implemented yet, so null should be returned.
		for (Object testObject : AggregateFunctionTests.goodBooleanTypes) {
			assertEquals(null, maxFactory.getFunction(testObject.getClass()));
		}
	}

	@SuppressWarnings("static-method")
	private void testMaxFactoryDouble(MaxFunctionFactory maxFactory) {
		for (Object testObject : AggregateFunctionTests.goodDoubleTypes) {
			assertEquals(Double.class,
					maxFactory.getFunction(testObject.getClass()).getType());
		}
	}

	@SuppressWarnings("static-method")
	private void testMaxFactoryFloat(MaxFunctionFactory maxFactory) {
		for (Object testObject : AggregateFunctionTests.goodFloatTypes) {
			assertEquals(Float.class,
					maxFactory.getFunction(testObject.getClass()).getType());
		}
	}

	@SuppressWarnings("static-method")
	private void testMaxFactoryInteger(MaxFunctionFactory maxFactory) {
		for (Object testObject : AggregateFunctionTests.goodIntegerTypes) {
			assertEquals(Integer.class,
					maxFactory.getFunction(testObject.getClass()).getType());
		}
	}

	/**
	 * Test that the actual operation of the max function works.
	 */
	@SuppressWarnings("static-method")
	@Test
	public void testMaxingInteger() {
		MaxFunctionFactory maxFactory = new MaxFunctionFactory();
		AbstractAggregateFunction function = maxFactory
				.getFunction(Integer.class);

		Integer[] numbers = new Integer[] { 2, 4, 6, 8, 10, 12 };

		for (int number : numbers) {
			function.operate(number);
		}

		assertEquals(Collections.max(Arrays.asList(numbers)), function.getResult());
	}

	/**
	 * Test that the actual operation of the max function works.
	 */
	@SuppressWarnings("static-method")
	@Test
	public void testMaxingFloat() {
		MaxFunctionFactory maxFactory = new MaxFunctionFactory();
		AbstractAggregateFunction function = maxFactory
				.getFunction(Float.class);

		Float[] numbers = new Float[] { 2.3f, 4.5f, 6.9f, 8.11f, 10.13f, 12.15f };

		for (float number : numbers) {
			function.operate(number);
		}

		assertEquals(Collections.max(Arrays.asList(numbers)), function.getResult());
	}

	/**
	 * Test that the actual operation of the max function works.
	 */
	@SuppressWarnings("static-method")
	@Test
	public void testMaxingDouble() {
		MaxFunctionFactory maxFactory = new MaxFunctionFactory();
		AbstractAggregateFunction function = maxFactory
				.getFunction(Double.class);

		Double[] numbers = new Double[] { 2.3d, 4.5d, 6.9d, 8.11d, 10.13d,
				12.15d };

		for (double number : numbers) {
			function.operate(number);
		}

		assertEquals(Collections.max(Arrays.asList(numbers)), function.getResult());
	}
}
