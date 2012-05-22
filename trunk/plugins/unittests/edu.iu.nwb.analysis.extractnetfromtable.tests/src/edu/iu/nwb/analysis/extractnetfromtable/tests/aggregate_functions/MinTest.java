package edu.iu.nwb.analysis.extractnetfromtable.tests.aggregate_functions;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

import edu.iu.nwb.analysis.extractnetfromtable.aggregate.AbstractAggregateFunction;
import edu.iu.nwb.analysis.extractnetfromtable.aggregate.MinFunctionFactory;
import edu.iu.nwb.analysis.extractnetfromtable.tests.aggregate_functions.common.AggregateFunctionTests;

public class MinTest {

	/**
	 * Test that the minFactory returns appropriately typed functions.
	 */
	@Test
	public void testMinFactoryTypes() {

		MinFunctionFactory minFactory = new MinFunctionFactory();

		testMinFactoryInteger(minFactory);

		testMinFactoryFloat(minFactory);

		testMinFactoryDouble(minFactory);

		testMinFactoryBoolean(minFactory);
	}

	@SuppressWarnings("static-method")
	private void testMinFactoryBoolean(MinFunctionFactory minFactory) {
		// Boolean is not implemented yet, so null should be returned.
		for (Object testObject : AggregateFunctionTests.goodBooleanTypes) {
			assertEquals(null, minFactory.getFunction(testObject.getClass()));
		}
	}

	@SuppressWarnings("static-method")
	private void testMinFactoryDouble(MinFunctionFactory minFactory) {
		for (Object testObject : AggregateFunctionTests.goodDoubleTypes) {
			assertEquals(Double.class,
					minFactory.getFunction(testObject.getClass()).getType());
		}
	}

	@SuppressWarnings("static-method")
	private void testMinFactoryFloat(MinFunctionFactory minFactory) {
		for (Object testObject : AggregateFunctionTests.goodFloatTypes) {
			assertEquals(Float.class,
					minFactory.getFunction(testObject.getClass()).getType());
		}
	}

	@SuppressWarnings("static-method")
	private void testMinFactoryInteger(MinFunctionFactory minFactory) {
		for (Object testObject : AggregateFunctionTests.goodIntegerTypes) {
			assertEquals(Integer.class,
					minFactory.getFunction(testObject.getClass()).getType());
		}
	}

	/**
	 * Test that the actual operation of the min function works.
	 */
	@SuppressWarnings("static-method")
	@Test
	public void testMiningInteger() {
		MinFunctionFactory minFactory = new MinFunctionFactory();
		AbstractAggregateFunction function = minFactory
				.getFunction(Integer.class);

		Integer[] numbers = new Integer[] { 2, 4, 6, 8, 10, 12 };

		for (int number : numbers) {
			function.operate(number);
		}

		assertEquals(Collections.min(Arrays.asList(numbers)), function.getResult());
	}

	/**
	 * Test that the actual operation of the min function works.
	 */
	@SuppressWarnings("static-method")
	@Test
	public void testMiningFloat() {
		MinFunctionFactory minFactory = new MinFunctionFactory();
		AbstractAggregateFunction function = minFactory
				.getFunction(Float.class);

		Float[] numbers = new Float[] { 2.3f, 4.5f, 6.9f, 8.11f, 10.13f, 12.15f };

		for (float number : numbers) {
			function.operate(number);
		}

		assertEquals(Collections.min(Arrays.asList(numbers)), function.getResult());
	}

	/**
	 * Test that the actual operation of the min function works.
	 */
	@SuppressWarnings("static-method")
	@Test
	public void testMiningDouble() {
		MinFunctionFactory minFactory = new MinFunctionFactory();
		AbstractAggregateFunction function = minFactory
				.getFunction(Double.class);

		Double[] numbers = new Double[] { 2.3d, 4.5d, 6.9d, 8.11d, 10.13d,
				12.15d };

		for (double number : numbers) {
			function.operate(number);
		}

		assertEquals(Collections.min(Arrays.asList(numbers)), function.getResult());
	}
}
