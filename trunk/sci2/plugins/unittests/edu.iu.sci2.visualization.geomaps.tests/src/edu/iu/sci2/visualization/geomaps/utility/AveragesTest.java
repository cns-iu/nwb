package edu.iu.sci2.visualization.geomaps.utility;

import junit.framework.TestCase;

import com.google.common.collect.ImmutableList;

import edu.iu.sci2.visualization.geomaps.utility.Averages;

public class AveragesTest extends TestCase {
	public static final double EPSILON = 0.00001;
	
	public static void testAverageZeroElements() {
		try {
			Averages.meanOfDoubles(ImmutableList.<Double>of());
		} catch (IllegalArgumentException e) {
			assertEquals(Averages.FAIL_MESSAGE, e.getMessage());
			return;
		}
		
		fail("IllegalArgumentException for the average of no numbers.");
	}
	
	public static void testAverageOneElement() {
		assertEquals(3.0, Averages.meanOfDoubles(3.0), EPSILON);
	}
	
	public static void testAverageTwoElements() {
		assertEquals((3.0 + 4.0) / 2, Averages.meanOfDoubles(3.0, 4.0), EPSILON);
	}
	
	public static void testAverageThreeElements() {
		assertEquals((3.0 + 4.0 + 9.0) / 3, Averages.meanOfDoubles(3.0, 4.0, 9.0), EPSILON);
	}
	
}
