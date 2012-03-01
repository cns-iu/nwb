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
}
