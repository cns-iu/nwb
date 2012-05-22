package edu.iu.nwb.analysis.extractnetfromtable.tests.aggregate_functions;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.iu.nwb.analysis.extractnetfromtable.aggregate.AbstractAggregateFunction;
import edu.iu.nwb.analysis.extractnetfromtable.aggregate.ArithmeticMeanFunctionFactory;
import edu.iu.nwb.analysis.extractnetfromtable.tests.aggregate_functions.common.AggregateFunctionTests;

public class ArithmeticMeanTest {

	/**
	 * Test that the arithmeticMeanFactory returns appropriately typed functions.
	 */
	@Test
	public void testArithmeticMeanFactoryTypes() {

		ArithmeticMeanFunctionFactory arithmeticMeanFactory = new ArithmeticMeanFunctionFactory();

		testArithmeticMeanFactoryInteger(arithmeticMeanFactory);

		testArithmeticMeanFactoryFloat(arithmeticMeanFactory);

		testArithmeticMeanFactoryDouble(arithmeticMeanFactory);

		testArithmeticMeanFactoryBoolean(arithmeticMeanFactory);
	}

	@SuppressWarnings("static-method")
	private void testArithmeticMeanFactoryBoolean(ArithmeticMeanFunctionFactory arithmeticMeanFactory) {
		// Boolean is not implemented yet, so null should be returned.
		for (Object testObject : AggregateFunctionTests.goodBooleanTypes) {
			assertEquals(null, arithmeticMeanFactory.getFunction(testObject.getClass()));
		}
	}

	@SuppressWarnings("static-method")
	private void testArithmeticMeanFactoryDouble(ArithmeticMeanFunctionFactory arithmeticMeanFactory) {
		for (Object testObject : AggregateFunctionTests.goodDoubleTypes) {
			assertEquals(Double.class,
					arithmeticMeanFactory.getFunction(testObject.getClass()).getType());
		}
	}

	@SuppressWarnings("static-method")
	private void testArithmeticMeanFactoryFloat(ArithmeticMeanFunctionFactory arithmeticMeanFactory) {
		for (Object testObject : AggregateFunctionTests.goodFloatTypes) {
			assertEquals(Float.class,
					arithmeticMeanFactory.getFunction(testObject.getClass()).getType());
		}
	}

	@SuppressWarnings("static-method")
	private void testArithmeticMeanFactoryInteger(ArithmeticMeanFunctionFactory arithmeticMeanFactory) {
		// The integer actually runs as a double.
		for (Object testObject : AggregateFunctionTests.goodIntegerTypes) {
			assertEquals(Double.class,
					arithmeticMeanFactory.getFunction(testObject.getClass()).getType());
		}
	}

	
	/**
	 * Test that the actual operation of the arithmeticMean function works.
	 */
	@SuppressWarnings("static-method")
	@Test
	public void testArithmeticMeaningInteger() {
		ArithmeticMeanFunctionFactory arithmeticMeanFactory = new ArithmeticMeanFunctionFactory();
		AbstractAggregateFunction function = arithmeticMeanFactory
				.getFunction(Integer.class);

		int[] numbers = new int[] { 2, 4, 6, 8, 10, 12 };

		int sum = 0;
		// The integer actual runs as a double
		double count = 0;
		for (int number : numbers) {
			function.operate(number);
			sum += number;
			count++;
			assertEquals(sum / count, function.getResult());
		}

		assertEquals(sum / count, function.getResult());
	}

	/**
	 * Test that the actual operation of the arithmeticMean function works.
	 */
	@SuppressWarnings("static-method")
	@Test
	public void testArithmeticMeaningFloat() {
		ArithmeticMeanFunctionFactory arithmeticMeanFactory = new ArithmeticMeanFunctionFactory();
		AbstractAggregateFunction function = arithmeticMeanFactory
				.getFunction(Float.class);

		float[] numbers = new float[] { 2.3f, 4.5f, 6.9f, 8.11f, 10.13f, 12.15f };

		float sum = 0;
		int count = 0;
		for (float number : numbers) {
			function.operate(number);
			sum += number;
			count++;
			assertEquals(sum / count, function.getResult());
		}

		assertEquals(sum / count, function.getResult());
	}

	/**
	 * Test that the actual operation of the arithmeticMean function works.
	 */
	@SuppressWarnings("static-method")
	@Test
	public void testArithmeticMeaningDouble() {
		ArithmeticMeanFunctionFactory arithmeticMeanFactory = new ArithmeticMeanFunctionFactory();
		AbstractAggregateFunction function = arithmeticMeanFactory
				.getFunction(Double.class);

		double[] numbers = new double[] { 2.3d, 4.5d, 6.9d, 8.11d, 10.13d,
				12.15d };

		double sum = 0;
		int count = 0;
		for (double number : numbers) {
			function.operate(number);
			sum += number;
			count++;
			assertEquals(sum / count, function.getResult());
		}

		assertEquals(sum / count, function.getResult());
	}
}
