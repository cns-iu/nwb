package edu.iu.sci2.visualization.bipartitenet.component.edge.shape;

import static org.junit.Assert.*;

import math.geom2d.Point2D;
import math.geom2d.conic.Circle2D;
import math.geom2d.line.LineSegment2D;

import org.junit.Test;

import edu.iu.sci2.visualization.bipartitenet.component.edge.shape.BezierEdgeShape;

public class BezierEdgeStrategyTest {

	@Test
	public void testGetSourceAndControl() {
		Circle2D unitCircle = new Circle2D(new Point2D(0, 0), 1);
		double controlPointOffset = 5;
		
		LineSegment2D sourceAndControl = BezierEdgeShape.getSourceAndControl(
				unitCircle, new Point2D(100, 1), controlPointOffset);
		
		assertEquals(new Point2D(0, 0), sourceAndControl.getFirstPoint());
		assertEquals(new Point2D(5, 0), sourceAndControl.getLastPoint());
	}

}
