package edu.iu.sci2.visualization.bipartitenet.component.edge.shape;

import math.geom2d.Point2D;
import math.geom2d.conic.Circle2D;
import math.geom2d.curve.AbstractContinuousCurve2D;
import math.geom2d.line.LineSegment2D;
import math.geom2d.spline.CubicBezierCurve2D;
import edu.iu.sci2.visualization.bipartitenet.component.NodeView;

/*
 * This is awesome:
 * http://en.wikipedia.org/wiki/B%C3%A9zier_curve
 * 
 */
/**
 * An EdgeShape that connects two nodes with a Bezier curve.  The edges emerge from the nodes
 * approximately horizontally, then ascend or descend to the other node's level, then enter
 * the other node again approximately horizontally.
 *
 */
public class BezierEdgeShape implements EdgeShape {
	/**
	 * The control points for the Bezier curve are created on a horizontal line even with the
	 * node center.  This is the magnitude of the x-offset of the control points.  The control
	 * points are created towards the center of the page from the node centers.
	 */
	private static final int DEFAULT_CONTROL_POINT_OFFSET = 40;
	private final double controlPointOffset;
	
	public BezierEdgeShape() {
		this(DEFAULT_CONTROL_POINT_OFFSET);
	}

	public BezierEdgeShape(double controlPointOffset) {
		this.controlPointOffset = controlPointOffset;
	}

	@Override
	public AbstractContinuousCurve2D connectNodes(NodeView source, NodeView dest) {
		Circle2D sourceCircle = GeomUtils.getNodeCircle(source),
				destCircle = GeomUtils.getNodeCircle(dest);
		
		LineSegment2D sourceAndFirstControl = getSourceAndControl(sourceCircle, destCircle.getCenter(),
						controlPointOffset),
				secondControlAndDest = getSourceAndControl(destCircle, sourceCircle.getCenter(),
						controlPointOffset);
		
		return new CubicBezierCurve2D(sourceAndFirstControl.getFirstPoint(),
				sourceAndFirstControl.getLastPoint(),
				secondControlAndDest.getLastPoint(),
				secondControlAndDest.getFirstPoint());
	}

	static LineSegment2D getSourceAndControl(Circle2D nodeCircle,
			Point2D otherNodeCenter, double controlPointOffset) {
		Point2D firstPoint = nodeCircle.getCenter();
		Point2D controlPoint = GeomUtils.getNearestPointOnHorizontalDiameter(
				new Circle2D(nodeCircle.getCenter(), controlPointOffset),
				otherNodeCenter);
		return new LineSegment2D(firstPoint, controlPoint);
	}
}
