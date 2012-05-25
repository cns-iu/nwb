package edu.iu.sci2.visualization.bipartitenet.component.edge.shape;

import math.geom2d.Point2D;
import math.geom2d.conic.Circle2D;
import math.geom2d.curve.AbstractContinuousCurve2D;
import math.geom2d.line.LineSegment2D;
import edu.iu.sci2.visualization.bipartitenet.component.NodeView;

/**
 * Connects two nodes by drawing lines between particular points on their
 * perimeters. The perimeter points are the intersections of the horizontal
 * diameter of each circle with the perimeter of the circle.
 * 
 */
public class TangentPointEdgeShape implements EdgeShape {
	@Override
	public AbstractContinuousCurve2D connectNodes(NodeView source, NodeView dest) {
		Circle2D sourceCircle = GeomUtils.getNodeCircle(source),
				destCircle = GeomUtils.getNodeCircle(dest);
		
		Point2D sourceTangentPoint = GeomUtils.getNearestPointOnHorizontalDiameter(sourceCircle, destCircle.getCenter()),
				destTangentPoint = GeomUtils.getNearestPointOnHorizontalDiameter(destCircle, sourceCircle.getCenter());
		
		return new LineSegment2D(sourceTangentPoint, destTangentPoint);
	}
}
