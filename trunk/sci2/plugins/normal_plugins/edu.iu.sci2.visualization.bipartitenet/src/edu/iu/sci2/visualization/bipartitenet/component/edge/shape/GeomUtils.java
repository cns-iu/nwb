package edu.iu.sci2.visualization.bipartitenet.component.edge.shape;

import math.geom2d.Point2D;
import math.geom2d.conic.Circle2D;
import math.geom2d.line.LineSegment2D;
import math.geom2d.line.StraightLine2D;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import edu.iu.sci2.visualization.bipartitenet.component.NodeView;

class GeomUtils {
	/**
	 * A horizontal line on the x-axis.
	 */
	static final StraightLine2D HORIZONTAL =
			StraightLine2D.create(Point2D.create(0, 0), Point2D.create(1, 0));

	/**
	 * Finds a horizontal line segment through the center of the given circle, with its ends on the
	 * perimeter.
	 */
	static LineSegment2D getHorizontalDiameter(Circle2D circle) {
		StraightLine2D lineThroughCenter = HORIZONTAL.getParallel(circle.getCenter());
		ImmutableList<Point2D> edgePoints = 
				ImmutableList.copyOf(circle.getIntersections(lineThroughCenter));
		if (edgePoints.size() != 2) {
			throw new AssertionError("Line through the center of a circle should intersect the circle twice!");
		}
		
		return new LineSegment2D(edgePoints.get(0), edgePoints.get(1));
	}

	/**
	 * Returns the Circle2D representing the given NodeView.
	 */
	static Circle2D getNodeCircle(NodeView nodeView) {
		return nodeView.getNodeCircle();
	}

	/**
	 * Given a line segment and another point, returns the point on the line segment that is
	 * closest to the external point.
	 */
	static Point2D getClosestPoint(LineSegment2D segment,
			Point2D target) {
		double t = segment.project(target);
		return segment.getPoint(t);
	}

	/**
	 * Given a circle and another point, finds the horizontal diameter of the circle,
	 * and returns the point from the diameter that is closest to the other point. 
	 */
	static Point2D getNearestPointOnHorizontalDiameter(Circle2D circle, Point2D nearPoint) {
		LineSegment2D horizontalDiameter = getHorizontalDiameter(circle);
		return getClosestPoint(horizontalDiameter, nearPoint);
	}

	/**
	 * Takes a line segment, and takes some length off of each end of it.
	 * Returns a new LineSegment2D that is collinear with the given one, but
	 * shorter.
	 * 
	 * @param baseLine
	 *            The line segment to shorten
	 * @param startClip
	 *            the length to take off of the start of the line segment. Must
	 *            not be negative.
	 * @param endClip
	 *            the length to take off of the end of the line segment. Must
	 *            not be negative.
	 * @throws IllegalArgumentException
	 *            if either startClip or endClip is negative, or if the length
	 *            of the new line segment would be 0 or negative.
	 */
	static LineSegment2D clipEndsFromLineSegment(LineSegment2D baseLine,
			double startClip, double endClip) {
		Preconditions.checkArgument(startClip >= 0, "startClip must be nonnegative");
		Preconditions.checkArgument(endClip >= 0, "endClip must be nonnegative");
		double newLineLength = baseLine.getLength() - startClip - endClip; 
		Preconditions.checkArgument(newLineLength > 0,
				"New line segment must have positive length (instead, length = %s)",
				newLineLength);
		
		Circle2D startCircle = new Circle2D(baseLine.getFirstPoint(), startClip);
		Circle2D endCircle = new Circle2D(baseLine.getLastPoint(), endClip);
		
		Point2D newStart = Iterables.getOnlyElement(startCircle.getIntersections(baseLine));
		Point2D newEnd = Iterables.getOnlyElement(endCircle.getIntersections(baseLine));
		return new LineSegment2D(newStart, newEnd);
	}
}
