package edu.iu.sci2.visualization.geomaps.utility;

import edu.iu.sci2.visualization.geomaps.utility.RelativeDifferences;
import junit.framework.TestCase;

public class RelativeDifferencesTest extends TestCase {
	private static final double EPSILON = 0.00001;
	
	public static void testRelativeDifferenceIsZero() {
		assertEquals(0.0, RelativeDifferences.relativeDifference(3.33, 3.33)); // TODO No epsilon here, right?
	}
	
	public static void testRelativeDifferenceIsOneHalf() {
		assertEquals(0.5, RelativeDifferences.relativeDifference(1.0, 2.0), EPSILON);
	}
	
	public static void testEqualNumbersAreNearlyEqualAtDefaultTolerance() {
		assertTrue(RelativeDifferences.nearlyEqual(3.33, 3.33));
	}
	
	public static void testEqualNumbersAreNearlyEqualAtEpsilon() {
		assertTrue(RelativeDifferences.nearlyEqual(3.33, 3.33, EPSILON));
	}
	
	public static void testOneAndTenAreNotNearlyEqualAtDefaultTolerance() {
		assertFalse(RelativeDifferences.nearlyEqual(1.0, 10.0));
	}
	
	public static void testOneAndTenAreNotNearlyEqualAtEpsilon() {
		assertFalse(RelativeDifferences.nearlyEqual(1.0, 10.0, EPSILON));
	}
	
	public static void testEqualNumbersAreEquivalentByDefault() {
		assertTrue(RelativeDifferences.DEFAULT_EQUIVALENCE.equivalent(3.33, 3.33));
	}
	
	public static void testOneAndTenAreNotEquivalentByDefault() {
		assertFalse(RelativeDifferences.DEFAULT_EQUIVALENCE.equivalent(1.0, 10.0));
	}
}
