package edu.iu.sci2.visualization.geomaps.utility;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Rectangles {
	private Rectangles() {}

	
	public static Rectangle2D.Double forCenterWithDimensions(
			Point2D.Double displayCenter, Dimension<Double> dimension) {
		Rectangle2D.Double rectangle = new Rectangle2D.Double();
		
		Point2D.Double aCornerPoint =
				new Point2D.Double(
						displayCenter.x + 0.5 * dimension.getWidth(),
						displayCenter.y + 0.5 * dimension.getHeight());
		
		rectangle.setFrameFromCenter(
				displayCenter,
				aCornerPoint);
		
		return rectangle;
	}

	public static Range<Double> xRange(Rectangle2D.Double rectangle) {
		return Range.between(rectangle.getMinX(), rectangle.getMaxX());
	}

	public static Range<Double> yRange(Rectangle2D.Double rectangle) {
		return Range.between(rectangle.getMinY(), rectangle.getMaxY());
	}	
}
