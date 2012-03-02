package edu.iu.sci2.visualization.geomaps.viz.ps;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import com.vividsolutions.jts.geom.Coordinate;

import edu.iu.sci2.visualization.geomaps.data.interpolation.Interpolator1D;
import edu.iu.sci2.visualization.geomaps.utility.Dimension;
import edu.iu.sci2.visualization.geomaps.utility.Range;
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
	private final double displayHeightInPoints;

	public GeoMapViewPageArea(Rectangle2D.Double dataRectangle) {
		this.dataRectangle = dataRectangle;
		
		double scale = (Constants.MAP_PAGE_AREA_WIDTH_IN_POINTS / dataRectangle.getWidth());		
		this.displayHeightInPoints = (scale * dataRectangle.getHeight());
		Point2D.Double displayCenter = new Point2D.Double(Constants.MAP_CENTER_X_IN_POINTS, calculateDisplayCenterY(displayHeightInPoints));
		
		this.displayRectangle = rectangleFromCenter(displayCenter, Dimension.ofSize(scale * dataRectangle.getWidth(), displayHeightInPoints));
	}

	public static Rectangle2D.Double rectangleFromCenter(Point2D.Double displayCenter, Dimension<Double> dimension) {
		Rectangle2D.Double rectangle = new Rectangle2D.Double();
		rectangle.setFrameFromCenter(
				displayCenter,
				new Point2D.Double(
						displayCenter.x + 0.5 * dimension.getWidth(),
						displayCenter.y + 0.5 * dimension.getHeight()));
		
		return rectangle;
	}
	
	public static Rectangle2D.Double rectangleWithCorners(Point2D.Double corner, Point2D.Double oppositeCorner) {
		Rectangle2D.Double rectangle = new Rectangle2D.Double();
		rectangle.setFrameFromDiagonal(corner, oppositeCorner);
		return rectangle;
	}
	
	public static Coordinate pointAsCoordinate(Point2D.Double point) {
		return new Coordinate(point.x, point.y);
	}
	
	public static Point2D.Double minPointOf(Rectangle2D.Double rectangle) {
		return new Point2D.Double(rectangle.getMinX(), rectangle.getMinY());
	}
	
	public static Point2D.Double maxPointOf(Rectangle2D.Double rectangle) {
		return new Point2D.Double(rectangle.getMaxX(), rectangle.getMaxY());
	}
	
	public static Range<Double> xRange(Rectangle2D.Double rectangle) {
		return Range.between(rectangle.getMinX(), rectangle.getMaxX());
	}
	
	public static Range<Double> yRange(Rectangle2D.Double rectangle) {
		return Range.between(rectangle.getMinY(), rectangle.getMaxY());
	}
	
	public static Point2D.Double asPoint2D(Coordinate coordinate) { // TODO temporary bridge, replace all non-geo Coordinates with Point2D.Doubles soon ... or not, the map looks pretty good without these margins 
		return new Point2D.Double(coordinate.x, coordinate.y);
	}

	public double getMapHeightInPoints() {
		return displayHeightInPoints;
	}

	private static double calculateDisplayCenterY(double displayHeightInPoints) {
		return (Constants.PAGE_FOOTER_HEIGHT_IN_POINTS
				+ Constants.LEGEND_PAGE_AREA_DIMENSION.getHeight() + (displayHeightInPoints / 2.0)); //Constants.LEGEND_PAGE_AREA_DIMENSION.getHeight() + (displayHeightInPoints / 2.0));
	}

	public Point2D.Double getDisplayPoint(Coordinate coordinate) {
		return new Point2D.Double(
				Interpolator1D.between(xRange(dataRectangle), xRange(displayRectangle)).apply(coordinate.x),
				Interpolator1D.between(yRange(dataRectangle), yRange(displayRectangle)).apply(coordinate.y));
	}

	public String toPostScript() {
		String s = "";

		s += "newpath" + "\n";
		
		// TODO Replace with PathIterator over displayRectangle?
		s += INDENT + displayRectangle.getMinX() + " " + displayRectangle.getMinY() + " moveto" + "\n";
		s += INDENT + displayRectangle.getMinX() + " " + displayRectangle.getMaxY() + " lineto" + "\n";
		s += INDENT + displayRectangle.getMaxX() + " " + displayRectangle.getMaxY() + " lineto"	+ "\n";
		s += INDENT + displayRectangle.getMaxX() + " " + displayRectangle.getMinY() + " lineto" + "\n";
		s += "closepath" + "\n";
		
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
