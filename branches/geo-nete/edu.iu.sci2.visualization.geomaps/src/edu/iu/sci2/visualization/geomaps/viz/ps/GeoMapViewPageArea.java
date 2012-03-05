package edu.iu.sci2.visualization.geomaps.viz.ps;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import com.vividsolutions.jts.geom.Coordinate;

import edu.iu.sci2.visualization.geomaps.data.interpolation.Interpolator1D;
import edu.iu.sci2.visualization.geomaps.utility.Dimension;
import edu.iu.sci2.visualization.geomaps.utility.Rectangles;
import edu.iu.sci2.visualization.geomaps.viz.Constants;

public class GeoMapViewPageArea {
	public static final boolean CLIP_TO_BOUNDING_BOX = true;
	public static final boolean BACKGROUND_TRANSPARENT = true;
	public static final Color BACKGROUND_COLOR = Color.CYAN;
	public static final boolean DRAW_BOUNDING_BOX = false;
	public static final double BOUNDING_BOX_LINE_WIDTH = .2;

	public static final String INDENT = "  ";

	private final Rectangle2D.Double dataRectangle;
	private final Rectangle2D.Double displayRectangle;

	public GeoMapViewPageArea(Rectangle2D.Double dataRectangle) {
		this.dataRectangle = dataRectangle;
		
		double scale = (Constants.MAP_PAGE_AREA_WIDTH_IN_POINTS / dataRectangle.getWidth());		
		Dimension<Double> displayDimension =
				Dimension.ofSize(
						(scale * dataRectangle.getWidth()),
						(scale * dataRectangle.getHeight()));
		
		Point2D.Double displayCenter =
				new Point2D.Double(
						Constants.MAP_CENTER_X_IN_POINTS,
						(Constants.PAGE_FOOTER_HEIGHT_IN_POINTS +
						Constants.LEGEND_PAGE_AREA_DIMENSION.getHeight() +
								(displayDimension.getHeight() / 2.0)));
		
		
		this.displayRectangle = Rectangles.forCenterWithDimensions(displayCenter, displayDimension);
	}
	
	public Point2D.Double getDisplayPoint(Coordinate coordinate) {
		return new Point2D.Double(
				Interpolator1D.between(Rectangles.xRange(dataRectangle), Rectangles.xRange(displayRectangle)).apply(coordinate.x),
				Interpolator1D.between(Rectangles.yRange(dataRectangle), Rectangles.yRange(displayRectangle)).apply(coordinate.y));
	}
	
	public String toPostScript() {
		String s = "";

		s += PSUtility.path(
				new Point2D.Double(displayRectangle.getMinX(), displayRectangle.getMinY()),
				new Point2D.Double(displayRectangle.getMinX(), displayRectangle.getMaxY()),
				new Point2D.Double(displayRectangle.getMaxX(), displayRectangle.getMaxY()),
				new Point2D.Double(displayRectangle.getMaxX(), displayRectangle.getMinY()));
		
		if (!BACKGROUND_TRANSPARENT) {
			s += "gsave" + "\n";
			s += INDENT	+ PSUtility.makeSetRGBColorCommand(BACKGROUND_COLOR);
			s += INDENT + "fill" + "\n";
			s += "grestore" + "\n";
		}
		
		if (DRAW_BOUNDING_BOX) {
			s += "gsave" + "\n";
			s += INDENT + BOUNDING_BOX_LINE_WIDTH + " setlinewidth" + "\n";
			s += INDENT + "stroke" + "\n";
			s += "grestore" + "\n";
		}
		
		if (CLIP_TO_BOUNDING_BOX) {
			s += "clip" + "\n";
		}

		return s;
	}

	public Rectangle2D.Double getDisplayRectangle() {
		return displayRectangle;
	}
}
