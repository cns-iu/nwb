package edu.iu.nwb.analysis.extractnetfromtable.tests.aggregate_functions;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.iu.nwb.analysis.extractnetfromtable.aggregate.AbstractAggregateFunction;
import edu.iu.nwb.analysis.extractnetfromtable.aggregate.CountFunctionFactory;
import edu.iu.nwb.analysis.extractnetfromtable.tests.aggregate_functions.common.AggregateFunctionTests;

public class CountTest {

	/**
	 * Test that the countFactory returns appropriately typed functions.
	 */
	@Test
	public void testCountFactoryTypes() {

		CountFunctionFactory countFactory = new CountFunctionFactory();

		testCountFactoryInteger(countFactory);

		testCountFactoryFloat(countFactory);

		testCountFactoryDouble(countFactory);

		testCountFactoryBoolean(countFactory);
	}

	@SuppressWarnings("static-method")
	private void testCountFactoryBoolean(CountFunctionFactory countFactory) {
		for (Object testObject : AggregateFunctionTests.goodBooleanTypes) {
			assertEquals(int.class,
					countFactory.getFunction(testObject.getClass()).getType());
		}
	}

	@SuppressWarnings("static-method")
	private void testCountFactoryDouble(CountFunctionFactory countFactory) {
		for (Object testObject : AggregateFunctionTests.goodDoubleTypes) {
			assertEquals(int.class,
					countFactory.getFunction(testObject.getClass()).getType());
		}
	}

	@SuppressWarnings("static-method")
	private void testCountFactoryFloat(CountFunctionFactory countFactory) {
		for (Object testObject : AggregateFunctionTests.goodFloatTypes) {
			assertEquals(int.class,
					countFactory.getFunction(testObject.getClass()).getType());
		}
	}

	@SuppressWarnings("static-method")
	private void testCountFactoryInteger(CountFunctionFactory countFactory) {
		for (Object testObject : AggregateFunctionTests.goodIntegerTypes) {
			assertEquals(int.class,
					countFactory.getFunction(testObject.getClass()).getType());
		}
	}

	/**
	 * Test that the actual operation of the count function works.
	 */
	@SuppressWarnings("static-method")
	@Test
	public void testCountingInteger() {
		CountFunctionFactory countFactory = new CountFunctionFactory();
		AbstractAggregateFunction function = countFactory
				.getFunction(Integer.class);

		int[] numbers = new int[] { 2, 4, 6, 8, 10, 12 };

		int count = 0;
		for (int number : numbers) {
			function.operate(number);
			count++;
			assertEquals(count, function.getResult());
		}

		assertEquals(count, function.getResult());
	}

	/**
	 * Test that the actual operation of the count function works.
	 */
	@SuppressWarnings("static-method")
	@Test
	public void testCountingFloat() {
		CountFunctionFactory countFactory = new CountFunctionFactory();
		AbstractAggregateFunction function = countFactory
				.getFunction(Float.class);

		float[] numbers = new float[] { 2.3f, 4.5f, 6.9f, 8.11f, 10.13f, 12.15f };

		int count = 0;
		for (float number : numbers) {
			function.operate(number);
			count++;
			assertEquals(count, function.getResult());
		}

		assertEquals(count, function.getResult());
	}

	/**
	 * Test that the actual operation of the count function works.
	 */
	@SuppressWarnings("static-method")
	@Test
	public void testCountingDouble() {
		CountFunctionFactory countFactory = new CountFunctionFactory();
		AbstractAggregateFunction function = countFactory
				.getFunction(Double.class);

		double[] numbers = new double[] { 2.3d, 4.5d, 6.9d, 8.11d, 10.13d,
				12.15d };

		int count = 0;
		for (double number : numbers) {
			function.operate(number);
			count++;
			assertEquals(count, function.getResult());
		}

		assertEquals(count, function.getResult());
	}
}
