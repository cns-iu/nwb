package edu.iu.nwb.analysis.extractnetfromtable.tests.aggregate_functions;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.iu.nwb.analysis.extractnetfromtable.aggregate.AbstractAggregateFunction;
import edu.iu.nwb.analysis.extractnetfromtable.aggregate.GeometricMeanFunctionFactory;
import edu.iu.nwb.analysis.extractnetfromtable.tests.aggregate_functions.common.AggregateFunctionTests;

public class GeometricMeanTest {

	/**
	 * Test that the geometricMeanFactory returns appropriately typed functions.
	 */
	@Test
	public void testGeometricMeanFactoryTypes() {

		GeometricMeanFunctionFactory geometricMeanFactory = new GeometricMeanFunctionFactory();

		testGeometricMeanFactoryInteger(geometricMeanFactory);

		testGeometricMeanFactoryFloat(geometricMeanFactory);

		testGeometricMeanFactoryDouble(geometricMeanFactory);

		testGeometricMeanFactoryBoolean(geometricMeanFactory);
	}

	@SuppressWarnings("static-method")
	private void testGeometricMeanFactoryBoolean(GeometricMeanFunctionFactory geometricMeanFactory) {
		// Boolean is not implemented yet, so null should be returned.
		for (Object testObject : AggregateFunctionTests.goodBooleanTypes) {
			assertEquals(null, geometricMeanFactory.getFunction(testObject.getClass()));
		}
	}

	@SuppressWarnings("static-method")
	private void testGeometricMeanFactoryDouble(GeometricMeanFunctionFactory geometricMeanFactory) {
		for (Object testObject : AggregateFunctionTests.goodDoubleTypes) {
			assertEquals(Double.class,
					geometricMeanFactory.getFunction(testObject.getClass()).getType());
		}
	}

	@SuppressWarnings("static-method")
	private void testGeometricMeanFactoryFloat(GeometricMeanFunctionFactory geometricMeanFactory) {
		for (Object testObject : AggregateFunctionTests.goodFloatTypes) {
			assertEquals(Float.class,
					geometricMeanFactory.getFunction(testObject.getClass()).getType());
		}
	}

	@SuppressWarnings("static-method")
	private void testGeometricMeanFactoryInteger(GeometricMeanFunctionFactory geometricMeanFactory) {
		// The integer actually runs as a double.
		for (Object testObject : AggregateFunctionTests.goodIntegerTypes) {
			assertEquals(Double.class,
					geometricMeanFactory.getFunction(testObject.getClass()).getType());
		}
	}

	/**
	 * Test that the actual operation of the geometricMean function works.
	 */
	@SuppressWarnings("static-method")
	@Test
	public void testGeometricMeaningInteger() {
		GeometricMeanFunctionFactory geometricMeanFactory = new GeometricMeanFunctionFactory();
		AbstractAggregateFunction function = geometricMeanFactory
				.getFunction(Integer.class);

		int[] numbers = new int[] { 2, 4, 6, 8, 10, 12 };

		double product = 1;
		int count = 0;
		for (double number : numbers) {
			function.operate(number);
			product *= number;
			count++;
			assertEquals(Math.pow(product, 1.0 / count), function.getResult());
		}

		assertEquals(Math.pow(product, 1.0 / count), function.getResult());
	}

	/**
	 * Test that the actual operation of the geometricMean function works.
	 */
	@SuppressWarnings("static-method")
	@Test
	public void testGeometricMeaningFloat() {
		GeometricMeanFunctionFactory geometricMeanFactory = new GeometricMeanFunctionFactory();
		AbstractAggregateFunction function = geometricMeanFactory
				.getFunction(Float.class);

		float[] numbers = new float[] { 2.3f, 4.5f, 6.9f, 8.11f, 10.13f, 12.15f };

		float product = 1;
		long count = 0;
		for (float number : numbers) {
			function.operate(number);
			product *= number;
			count++;
			assertEquals((float) Math.pow(product, 1f / count), function.getResult());
		}

		assertEquals((float) Math.pow(product, 1f / count), function.getResult());
	}

	/**
	 * Test that the actual operation of the geometricMean function works.
	 */
	@SuppressWarnings("static-method")
	@Test
	public void testGeometricMeaningDouble() {
		GeometricMeanFunctionFactory geometricMeanFactory = new GeometricMeanFunctionFactory();
		AbstractAggregateFunction function = geometricMeanFactory
				.getFunction(Double.class);

		double[] numbers = new double[] { 2.3d, 4.5d, 6.9d, 8.11d, 10.13d,
				12.15d };

		double product = 1;
		int count = 0;
		for (double number : numbers) {
			product *= number;
			count++;
			function.operate(number);
			//assertEquals(Math.pow(product, 1.0 / count), function.getResult());
		}

		assertEquals(Math.pow(product, 1.0 / count), function.getResult());
	}
}
