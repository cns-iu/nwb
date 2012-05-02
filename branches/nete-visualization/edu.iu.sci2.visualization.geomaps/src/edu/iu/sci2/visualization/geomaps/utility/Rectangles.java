package edu.iu.sci2.visualization.geomaps.utility;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import com.google.common.collect.Range;
import com.google.common.collect.Ranges;

public class Rectangles {
	private Rectangles() {}
	
	public static Rectangle2D.Double forCenterWithDimensions(
			Point2D.Double displayCenter, Dimension<Double> dimension) {		
		Point2D.Double someCornerPoint =
				new Point2D.Double(
						displayCenter.x + 0.5 * dimension.getWidth(),
						displayCenter.y + 0.5 * dimension.getHeight());
		
		Rectangle2D.Double rectangle = new Rectangle2D.Double();
		rectangle.setFrameFromCenter(displayCenter,	someCornerPoint);
		
		return rectangle;
	}	
	
	public static Rectangle2D.Double forCornerPoints(
			Point2D.Double corner1, Point2D.Double corner2) {
		Rectangle2D.Double rectangle = new Rectangle2D.Double();
		rectangle.setFrameFromDiagonal(corner1, corner2);
		
		return rectangle;
	}

	public static Range<Double> xRange(Rectangle2D.Double rectangle) {
		return Ranges.closed(rectangle.getMinX(), rectangle.getMaxX());
	}

	public static Range<Double> yRange(Rectangle2D.Double rectangle) {
		return Ranges.closed(rectangle.getMinY(), rectangle.getMaxY());
	}	
}
