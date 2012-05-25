package edu.iu.sci2.visualization.bipartitenet.component.edge.shape;

import static org.junit.Assert.*;

import math.geom2d.Point2D;
import math.geom2d.conic.Circle2D;
import math.geom2d.line.LineSegment2D;

import org.junit.Test;

import edu.iu.sci2.visualization.bipartitenet.component.edge.shape.GeomUtils;


public class GeomUtilsTest {

	@Test
	public void testGetHorizontalDiameter() {
		Circle2D testCircle = Circle2D.create(new Point2D(0, 5), 1);
		LineSegment2D diameter = GeomUtils.getHorizontalDiameter(testCircle),
				reference = LineSegment2D.create(new Point2D(-1, 5), new Point2D(1, 5));
		
		assertEquals(reference, diameter);
	}
	
	@Test
	public void testGetClosestPointNegative() throws Exception {
		LineSegment2D unitHorizontal = new LineSegment2D(0, 0, 1, 0);
		
		Point2D closest = GeomUtils.getClosestPoint(unitHorizontal, new Point2D(-5, 20));
		assertTrue(unitHorizontal.contains(closest));
		assertEquals(new Point2D(0, 0), closest);
	}
	
	@Test
	public void testGetClosestPointPositive() throws Exception {
		LineSegment2D unitHorizontal = new LineSegment2D(0, 0, 1, 0);
		
		Point2D closest = GeomUtils.getClosestPoint(unitHorizontal, new Point2D(5, -3));
		assertTrue(unitHorizontal.contains(closest));
		assertEquals(new Point2D(1, 0), closest);
	}
	
	@Test
	public void testGetClosestPointInternal() throws Exception {
		LineSegment2D unitHorizontal = new LineSegment2D(0, 0, 1, 0);
		
		Point2D closest = GeomUtils.getClosestPoint(unitHorizontal, new Point2D(0.5, 1));
		assertTrue(unitHorizontal.contains(closest));
		assertEquals(new Point2D(0.5, 0), closest);
	}
	
	@Test
	public void testGetPointOnNearEdge() throws Exception {
		Circle2D testCircle = Circle2D.create(new Point2D(0, 5), 1);
		
		Point2D closest = GeomUtils.getNearestPointOnHorizontalDiameter(testCircle, new Point2D(-10, 0));
		
		assertTrue(testCircle.contains(closest));
		assertEquals(new Point2D(-1, 5), closest);
	}

	@Test
	public void testClipEndsFromLineSegment() throws Exception {
		Point2D start = new Point2D(0, 0);
		Point2D end = new Point2D(10, 0);
		double startClip = 2, endClip = 3;
		
		LineSegment2D segment = GeomUtils.clipEndsFromLineSegment(new LineSegment2D(start, end), startClip, endClip);
		assertNotNull(segment);
		assertTrue(segment.getFirstPoint().almostEquals(new Point2D(2, 0), 0.00000001));
		assertTrue(segment.getLastPoint().almostEquals(new Point2D(7, 0), 0.00000001));
	}

	@Test
	public void testClipEndsFromLineSegmentClipTooBig() throws Exception {
		Point2D start = new Point2D(0, 0);
		Point2D end = new Point2D(10, 0);
		double startClip = 12, endClip = 3;
		
		try {
			GeomUtils.clipEndsFromLineSegment(new LineSegment2D(start, end), startClip, endClip);
			fail("Exception must be thrown if clipping would create an empty line segment");
		} catch (Exception e) {
			assertEquals(IllegalArgumentException.class, e.getClass());
		}
	}
}
