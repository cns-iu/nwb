package edu.iu.sci2.visualization.geomaps.utility;

import com.google.common.base.Equivalence;

public class RelativeDifferences {
	public static final double DEFAULT_RELATIVE_DIFFERENCE_TOLERANCE = 0.01;
	public static final Equivalence<Double> DEFAULT_EQUIVALENCE =
			equivalenceUsingTolerance(DEFAULT_RELATIVE_DIFFERENCE_TOLERANCE);
	
	private RelativeDifferences() {}
	
	
	public static Equivalence<Double> equivalenceUsingTolerance(final double tolerance) {
		return new Equivalence<Double>() {
			@Override
			protected boolean doEquivalent(Double a, Double b) {
				return nearlyEqual(a, b, tolerance);
			}

			@Override
			protected int doHash(Double t) {
				return 0; // TODO document very strongly or else throw new UnsupportedOperationException();
			}
		};
	}
	
	/**
	 * The relative difference of two doubles.
	 * <p/>
	 * The relative difference of x and y is |x-y|/max(|x|,|y|) or 0 for equal x and y.
	 */
	public static double relativeDifference(double d1, double d2) {
		return (d1 == d2) ? 0 : Math.abs(d1 - d2) / Math.max(Math.abs(d1), Math.abs(d2));
	}

	/**
	 * Determines whether the given doubles are nearly equal.
	 * @see #nearlyEqual(double, double, double)
	 * @see #DEFAULT_RELATIVE_DIFFERENCE_TOLERANCE
	 */
	public static boolean nearlyEqual(double d1, double d2) {
		return nearlyEqual(d1, d2, DEFAULT_RELATIVE_DIFFERENCE_TOLERANCE);
	}

	/**
	 * Determines whether the given doubles are nearly equal.
	 * <p/>
	 * Here "nearly equal" means that the relative difference does not exceed
	 * the given {@code tolerance}.
	 */
	public static boolean nearlyEqual(double d1, double d2, double tolerance) {
		return (relativeDifference(d1, d2) <= tolerance);
	}

//	/**
//	 * Determines whether two double arrays contain nearly equal elements in the same order.
//	 * @see {@link #elementsNearlyEqual(List, List)}
//	 * @see {@link #DEFAULT_RELATIVE_DIFFERENCE_TOLERANCE}
//	 */
//	public static boolean elementsNearlyEqual(double[] array1, double[] array2) {
//		return elementsNearlyEqual(Doubles.asList(array1), Doubles.asList(array2));
//	}
//	
//	/**
//	 * Determines whether two double arrays contain nearly equal elements in the same order.
//	 * @see {@link #elementsNearlyEqual(List, List, double)}
//	 */
//	public static boolean elementsNearlyEqual(double[] array1, double[] array2, double tolerance) {
//		return elementsNearlyEqual(Doubles.asList(array1), Doubles.asList(array2), tolerance);
//	}
//	
//	/**
//	 * Determines whether two Lists of Doubles contain nearly equal elements in the same order.
//	 * @see {@link #elementsNearlyEqual(List, List, double)}
//	 * @see {@link #DEFAULT_RELATIVE_DIFFERENCE_TOLERANCE}
//	 */
//	public static boolean elementsNearlyEqual(List<Double> list1, List<Double> list2) {
//		return elementsNearlyEqual(list1, list2, DEFAULT_RELATIVE_DIFFERENCE_TOLERANCE);
//	}
//	
//	/**
//	 * Determines whether two Lists of Doubles contain nearly equal elements in the same order.
//	 * <p/>
//	 * Returns true if {@code list1} and {@code list2} contain the same number of elements and every
//	 * element of iterable1 is nearly equal to the corresponding element of iterable2.
//	 * <p/>
//	 * Here "nearly equal" means that the relative difference does not exceed
//	 * the given {@code tolerance}.
//	 */
//	public static boolean elementsNearlyEqual(
//			List<Double> list1, List<Double> list2, double tolerance) {
//		final int size1 = list1.size();
//		
//		if (size1 != list2.size()) {
//			return false;
//		}
//		
//		for (int ii = 0; ii < size1; ii++) {
//			if (!nearlyEqual(list1.get(ii), list2.get(ii), tolerance)) {
//				return false;
//			}
//		}
//		
//		return true;
//	}
}
