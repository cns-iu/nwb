package edu.iu.sci2.visualization.geomaps.utility;

import java.awt.geom.Point2D;

import junit.framework.TestCase;

import com.google.common.base.Equivalence;

import edu.iu.sci2.visualization.geomaps.utility.Points;

public class PointsTest extends TestCase {
	private static Point2D.Double pointAtZero;
	private static Point2D.Double pointAtOne;
	private static Point2D.Double pointAtTwo;
	private static Equivalence<Point2D.Double> equivalenceUpToDistanceOnePointFive;
	
	@Override
	protected void setUp() throws Exception {
		PointsTest.pointAtZero = new Point2D.Double(0.0, 0.0);
		PointsTest.pointAtOne = new Point2D.Double(1.0, 0.0);
		PointsTest.pointAtTwo = new Point2D.Double(2.0, 0.0);
		PointsTest.equivalenceUpToDistanceOnePointFive =
				Points.distanceEquivalenceWithTolerance(1.5);
		
	}
	
	public static void testZeroAndOneEquivalent() {
		assertTrue(equivalenceUpToDistanceOnePointFive.equivalent(pointAtZero, pointAtOne));
	}
	
	public static void testOneAndTwoEquivalent() {
		assertTrue(equivalenceUpToDistanceOnePointFive.equivalent(pointAtOne, pointAtTwo));
	}
	
	public static void testZeroAndThreeNotEquivalent() {
		assertFalse(equivalenceUpToDistanceOnePointFive.equivalent(pointAtZero, pointAtTwo));
	}
}
