package edu.iu.nwb.analysis.extractnetfromtable.tests.aggregate_functions;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.iu.nwb.analysis.extractnetfromtable.aggregate.AbstractAggregateFunction;
import edu.iu.nwb.analysis.extractnetfromtable.aggregate.ModeFunctionFactory;
import edu.iu.nwb.analysis.extractnetfromtable.tests.aggregate_functions.common.AggregateFunctionTests;

public class ModeTest {

	/**
	 * Test that the modeFactory returns appropriately typed functions.
	 */
	@Test
	public void testModeFactoryTypes() {

		ModeFunctionFactory modeFactory = new ModeFunctionFactory();

		testModeFactoryInteger(modeFactory);

		testModeFactoryFloat(modeFactory);

		testModeFactoryDouble(modeFactory);

		testModeFactoryBoolean(modeFactory);
	}

	@SuppressWarnings("static-method")
	private void testModeFactoryBoolean(ModeFunctionFactory modeFactory) {
		for (Object testObject : AggregateFunctionTests.goodBooleanTypes) {
			assertEquals(Boolean.class,
					modeFactory.getFunction(testObject.getClass()).getType());
		}
	}

	@SuppressWarnings("static-method")
	private void testModeFactoryDouble(ModeFunctionFactory modeFactory) {
		for (Object testObject : AggregateFunctionTests.goodDoubleTypes) {
			assertEquals(String.class,
					modeFactory.getFunction(testObject.getClass()).getType());
		}
	}

	@SuppressWarnings("static-method")
	private void testModeFactoryFloat(ModeFunctionFactory modeFactory) {
		for (Object testObject : AggregateFunctionTests.goodFloatTypes) {
			assertEquals(String.class,
					modeFactory.getFunction(testObject.getClass()).getType());
		}
	}

	@SuppressWarnings("static-method")
	private void testModeFactoryInteger(ModeFunctionFactory modeFactory) {
		for (Object testObject : AggregateFunctionTests.goodIntegerTypes) {
			assertEquals(Integer.class,
					modeFactory.getFunction(testObject.getClass()).getType());
		}
	}

	/**
	 * Test that the actual operation of the mode function works.
	 */
	@SuppressWarnings("static-method")
	@Test
	public void testModeingInteger() {
		ModeFunctionFactory modeFactory = new ModeFunctionFactory();
		AbstractAggregateFunction function = modeFactory
				.getFunction(Integer.class);

		int[] numbers = new int[] { 2, 4, 6, 8, 10, 12, 8 };

		for (int number : numbers) {
			function.operate(number);
		}

		assertEquals(8, function.getResult());
	}

	/**
	 * Test that the actual operation of the mode function works.
	 */
	@SuppressWarnings("static-method")
	@Test
	public void testModeingFloat() {
		ModeFunctionFactory modeFactory = new ModeFunctionFactory();
		AbstractAggregateFunction function = modeFactory
				.getFunction(Float.class);

		float[] numbers = new float[] { 2.3f, 4.5f, 6.9f, 8.11f, 10.13f,
				12.15f, 8.11f };

		for (float number : numbers) {
			function.operate(number);
		}

		assertEquals(String.valueOf(8.11f), function.getResult());
	}

	/**
	 * Test that the actual operation of the mode function works.
	 */
	@SuppressWarnings("static-method")
	@Test
	public void testModeingDouble() {
		ModeFunctionFactory modeFactory = new ModeFunctionFactory();
		AbstractAggregateFunction function = modeFactory
				.getFunction(Double.class);

		double[] numbers = new double[] { 2.3d, 4.5d, 6.9d, 8.11d, 10.13d,
				12.15d, 8.11d };

		for (double number : numbers) {
			function.operate(number);
		}

		assertEquals(String.valueOf(8.11d), function.getResult());
	}
}
