package edu.iu.epic.modelbuilder.gui.transition;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;
import java.util.List;

import edu.iu.epic.modelbuilder.gui.utility.GlobalConstants;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;

 
public class Arrow extends PPath {

	private static final int ARROW_SIDE_SIZE = 10;
	
	public Arrow(PNode targetCompartment, 
				 Point2D.Double normalizedBounds1, 
				 Point2D.Double normalizedBounds2) {
		super();

		float[] defaultArrowXCoordinates = {ARROW_SIDE_SIZE, 
											ARROW_SIDE_SIZE 
												+ ARROW_SIDE_SIZE 
												* 0.5f, 
											2.0f * ARROW_SIDE_SIZE };
		
		float[] defaultArrowYCoordinates = {ARROW_SIDE_SIZE, 
											ARROW_SIDE_SIZE 
												- (float) Math.pow(3.0f, 0.5) 
												* 0.5f
												* (float) ARROW_SIDE_SIZE, 
												ARROW_SIDE_SIZE };
		
		this.setPathToPolyline(defaultArrowXCoordinates, 
							   defaultArrowYCoordinates);
		
		this.setPaint(Color.DARK_GRAY);
		this.setPickable(false);
		
		this.addAttribute(GlobalConstants.NODE_TYPE_ATTRIBUTE_NAME, 
						  GlobalConstants.TRANSITION_ARROW_TYPE_ATTRIBUTE_VALUE);
		/*
		 * Side-effects the orientation of the arrow.
		 * */
		rotateArrow(normalizedBounds1, normalizedBounds2, this);

		Point2D.Double arrowDisplayPoint = calculateArrowDisplayPosition(
				targetCompartment, normalizedBounds1, normalizedBounds2);
		
		if (arrowDisplayPoint != null) {
			this.centerFullBoundsOnPoint(arrowDisplayPoint.getX(), 
														   arrowDisplayPoint.getY());
			}
		
		
	}
	
	public static void rotateArrow(Point2D.Double transitionEndpoint1,
			Point2D.Double transitionEndpoint2, PNode arrow) {
		float slope = (float) ((float) (transitionEndpoint2.getY() - transitionEndpoint1
				.getY()) / (transitionEndpoint2.getX() - transitionEndpoint1.getX()));

		double rotateByThis = Math.atan(slope) + Math.toRadians(90)  - arrow.getRotation();

		if (transitionEndpoint2.getX() < transitionEndpoint1.getX()) {
			rotateByThis += Math.toRadians(180);
		}

		arrow.rotateInPlace(rotateByThis);
	}
	
	public static Point2D.Double calculateArrowDisplayPosition(
			PNode targetCompartment, Point2D.Double normalizedBounds1,
			Point2D.Double normalizedBounds2) {
		/*
		 * It always has to be node2
		 * */
		Rectangle2D nodeBounds = targetCompartment.getFullBoundsReference();
		
		List<Point2D.Double> edgePoints = getTransitionBoundPoints(
											normalizedBounds1, normalizedBounds2);
		
		List<Point2D.Double> nodeBoundsPoints = getCompartmentBoundPoints(nodeBounds);
		
		Point2D.Double arrowDisplayPoint = getArrowDisplayPoint(edgePoints, nodeBoundsPoints);
		return arrowDisplayPoint;
	}
	
	/**
	 * Based on http://local.wasp.uwa.edu.au/~pbourke/geometry/lineline2d/
	 * @param edgePoints
	 * @param nodeBoundsPoints
	 * @return
	 */
	private static Double getArrowDisplayPoint(List<Double> edgePoints,
											List<Double> nodeBoundsPoints) {
		
		Point2D.Double intersectingPoint = null;
		
		for (int ii = 0; ii < 4; ii++) {
		intersectingPoint = getIntersectingPoint(edgePoints.get(0), 
												 edgePoints.get(1),
												 nodeBoundsPoints.get(ii),
												 nodeBoundsPoints.get((ii + 1) % 4));
		if (intersectingPoint != null) {
			return intersectingPoint;
		}
		
		}
		return null;
	}
	
	/**
	 * Refer to
	 * http://stackoverflow.com/questions/563198/how-do-you-detect-where
	 * -two-line-segments-intersect for the explanation. Also there is the a
	 * modification to the algorithm mentioned. >Explain g<
	 * 
	 * @param edgePoint1
	 * @param edgePoint2
	 * @param nodeSidePoint1
	 * @param nodeSidePoint2
	 * @return
	 */
	private static Double getIntersectingPoint(Double edgePoint1, Double edgePoint2,
			Double nodeSidePoint1, Double nodeSidePoint2) {

		double intersectingPointX, intersectingPointY;
		Point2D.Double intersectingPoint;

		Point2D.Double E = new Point2D.Double(edgePoint2.getX()
				- edgePoint1.getX(), edgePoint2.getY() - edgePoint1.getY());

		Point2D.Double F = new Point2D.Double(nodeSidePoint2.getX()
				- nodeSidePoint1.getX(), nodeSidePoint2.getY()
				- nodeSidePoint1.getY());

		Point2D.Double P = new Point2D.Double(-1 * E.getY(), E.getX());

		Point2D.Double Q = new Point2D.Double(-1 * F.getY(), F.getX());

		double h_denominator = (F.getX() * P.getX()) + (F.getY() * P.getY());

		double g_denominator = (E.getX() * Q.getX()) + (E.getY() * Q.getY());

		if (h_denominator == 0.0 || g_denominator == 0.0) {
			return null;
		}

		Point2D.Double A_C = new Point2D.Double(edgePoint1.getX()
				- nodeSidePoint1.getX(), edgePoint1.getY()
				- nodeSidePoint1.getY());

		double h_numerator = (A_C.getX() * P.getX()) + (A_C.getY() * P.getY());
		double g_numerator = -1.0
				* ((A_C.getX() * Q.getX()) + (A_C.getY() * Q.getY()));

		double h = h_numerator / h_denominator;
		double g = g_numerator / g_denominator;

		if ((h <= 1.0 && h >= 0.0) && (g <= 1.0 && g >= 0.0)) {
			intersectingPointX = nodeSidePoint1.getX() + F.getX() * h;
			intersectingPointY = nodeSidePoint1.getY() + F.getY() * h;
			intersectingPoint = new Point2D.Double(intersectingPointX,
					intersectingPointY);

			return intersectingPoint;
		} else {
			return null;
		}
	}
	
	/**
	 * @param nodeBounds
	 * @return
	 */
	private static List<Point2D.Double> getCompartmentBoundPoints(
			Rectangle2D nodeBounds) {
		List<Point2D.Double> nodeBoundsPoints = new ArrayList<Point2D.Double>();
		
		Point2D.Double nodeBoundPoint = new Point2D.Double(nodeBounds.getX(), nodeBounds.getY()); 
		nodeBoundsPoints.add(nodeBoundPoint);
		
		nodeBoundPoint = new Point2D.Double(nodeBounds.getX() + nodeBounds.getWidth(), 
											nodeBounds.getY()); 
		nodeBoundsPoints.add(nodeBoundPoint);
		
		nodeBoundPoint = new Point2D.Double(nodeBounds.getX() + nodeBounds.getWidth(), 
											nodeBounds.getY() + nodeBounds.getHeight()); 
		nodeBoundsPoints.add(nodeBoundPoint);

		nodeBoundPoint = new Point2D.Double(nodeBounds.getX(), 
											nodeBounds.getY() + nodeBounds.getHeight()); 
		nodeBoundsPoints.add(nodeBoundPoint);
		return nodeBoundsPoints;
	}
	
	
	/**
	 * @param bound1
	 * @param bound2
	 * @return
	 */
	private static List<Point2D.Double> getTransitionBoundPoints(
			Point2D.Double bound1, Point2D.Double bound2) {
		List<Point2D.Double> edgePoints = new ArrayList<Point2D.Double>();
		Point2D.Double edgePoint = new Point2D.Double(bound1.getX(), bound1.getY()); 
		edgePoints.add(edgePoint);
		
		edgePoint = new Point2D.Double(bound2.getX(), bound2.getY()); 
		edgePoints.add(edgePoint);
		return edgePoints;
	}

}
