package edu.iu.nwb.analysis.extractnetfromtable.tests.aggregate_functions;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import edu.iu.nwb.analysis.extractnetfromtable.aggregate.AbstractAggregateFunction;
import edu.iu.nwb.analysis.extractnetfromtable.aggregate.SumFunctionFactory;
import edu.iu.nwb.analysis.extractnetfromtable.tests.aggregate_functions.common.AggregateFunctionTests;

/**
 * Test the sum functions.
 * 
 * @author dmcoe
 * 
 */
public class SumTest {

	/**
	 * Test that the sumFactory returns appropriately typed functions.
	 */
	@Test
	public void testSumFactoryTypes() {

		SumFunctionFactory sumFactory = new SumFunctionFactory();

		testSumFactoryInteger(sumFactory);

		testSumFactoryFloat(sumFactory);

		testSumFactoryDouble(sumFactory);

		testSumFactoryBoolean(sumFactory);
	}

	@SuppressWarnings("static-method")
	private void testSumFactoryBoolean(SumFunctionFactory sumFactory) {
		// Boolean is not implemented yet, so null should be returned.
		for (Object testObject : AggregateFunctionTests.goodBooleanTypes) {
			assertEquals(null, sumFactory.getFunction(testObject.getClass()));
		}
	}

	@SuppressWarnings("static-method")
	private void testSumFactoryDouble(SumFunctionFactory sumFactory) {
		for (Object testObject : AggregateFunctionTests.goodDoubleTypes) {
			assertEquals(Double.class,
					sumFactory.getFunction(testObject.getClass()).getType());
		}
	}

	@SuppressWarnings("static-method")
	private void testSumFactoryFloat(SumFunctionFactory sumFactory) {
		for (Object testObject : AggregateFunctionTests.goodFloatTypes) {
			assertEquals(Float.class,
					sumFactory.getFunction(testObject.getClass()).getType());
		}
	}

	@SuppressWarnings("static-method")
	private void testSumFactoryInteger(SumFunctionFactory sumFactory) {
		for (Object testObject : AggregateFunctionTests.goodIntegerTypes) {
			assertEquals(Integer.class,
					sumFactory.getFunction(testObject.getClass()).getType());
		}
	}

	/**
	 * Test that the actual operation of the sum function works.
	 */
	@SuppressWarnings("static-method")
	@Test
	public void testSumingInteger() {
		SumFunctionFactory sumFactory = new SumFunctionFactory();
		AbstractAggregateFunction function = sumFactory
				.getFunction(Integer.class);

		int[] numbers = new int[] { 2, 4, 6, 8, 10, 12 };

		int sum = 0;
		for (int number : numbers) {
			function.operate(number);
			sum += number;
			assertEquals(sum, function.getResult());
		}

		assertEquals(sum, function.getResult());
	}

	/**
	 * Test that the actual operation of the sum function works.
	 */
	@SuppressWarnings("static-method")
	@Test
	public void testSumingFloat() {
		SumFunctionFactory sumFactory = new SumFunctionFactory();
		AbstractAggregateFunction function = sumFactory
				.getFunction(Float.class);

		float[] numbers = new float[] { 2.3f, 4.5f, 6.9f, 8.11f, 10.13f, 12.15f };

		float sum = 0;
		for (float number : numbers) {
			function.operate(number);
			sum += number;
			assertEquals(sum, function.getResult());
		}

		assertEquals(sum, function.getResult());
	}

	/**
	 * Test that the actual operation of the sum function works.
	 */
	@SuppressWarnings("static-method")
	@Test
	public void testSumingDouble() {
		SumFunctionFactory sumFactory = new SumFunctionFactory();
		AbstractAggregateFunction function = sumFactory
				.getFunction(Double.class);

		double[] numbers = new double[] { 2.3d, 4.5d, 6.9d, 8.11d, 10.13d,
				12.15d };

		double sum = 0;
		for (double number : numbers) {
			function.operate(number);
			sum += number;
			assertEquals(sum, function.getResult());
		}

		assertEquals(sum, function.getResult());
	}
}
