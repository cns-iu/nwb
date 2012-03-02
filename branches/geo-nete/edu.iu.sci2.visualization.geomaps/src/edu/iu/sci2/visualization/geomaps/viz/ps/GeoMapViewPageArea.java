package edu.iu.sci2.visualization.geomaps.viz.ps;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;

import com.vividsolutions.jts.geom.Coordinate;

import edu.iu.sci2.visualization.geomaps.viz.Constants;

public class GeoMapViewPageArea {
	public static final boolean CLIP_TO_BOUNDING_BOX = true;
	public static final boolean BACKGROUND_TRANSPARENT = true;
	public static final Color BACKGROUND_COLOR = Color.CYAN;
	public static final boolean DRAW_BOUNDING_BOX = false;
	public static final double BOUNDING_BOX_LINE_WIDTH = .2;

	public static final String INDENT = "  ";

	private final Rectangle2D.Double displayRectangle;

	private final double displayCenterXInPoints, displayCenterYInPoints;
	private Double dataRectangle;
	private final double scale;
	private final double displayHeightInPoints;

	public GeoMapViewPageArea(Rectangle2D.Double dataBoundingRectangle) {
		this.displayCenterXInPoints = Constants.MAP_CENTER_X_IN_POINTS;

		this.dataRectangle = dataBoundingRectangle;

		this.scale = calculateScale(dataBoundingRectangle.getMinX(), dataBoundingRectangle.getMaxX());

		this.displayHeightInPoints = (scale * (dataBoundingRectangle.getMaxY() - dataBoundingRectangle.getMinY()));
		this.displayCenterYInPoints = calculateDisplayCenterY(displayHeightInPoints);

		this.displayRectangle = rectangleWithCorners(
				asPoint2D(getDisplayCoordinate(new Coordinate(dataBoundingRectangle.getMinX(), dataBoundingRectangle.getMinY()))),
				asPoint2D(getDisplayCoordinate(new Coordinate(dataBoundingRectangle.getMaxX(), dataBoundingRectangle.getMaxY()))));
	}
	
	public static Rectangle2D.Double rectangleWithCorners(Point2D.Double corner, Point2D.Double oppositeCorner) {
		Rectangle2D.Double rectangle = new Rectangle2D.Double();
		rectangle.setFrameFromDiagonal(corner, oppositeCorner);
		return rectangle;
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

	// Transform ordinate z from the data space to the display space.
	private static double positionOnDisplay(double z, double displayCenterInPoints,
			double scale, double dataCenter) {
		return displayCenterInPoints + (scale * (z - dataCenter));
	}

	public Coordinate getDisplayCoordinate(Coordinate coordinate) {
		return new Coordinate(
				positionOnDisplay(
						coordinate.x, displayCenterXInPoints, scale, dataRectangle.getCenterX()),
				positionOnDisplay(
						coordinate.y, displayCenterYInPoints, scale, dataRectangle.getCenterY()));
	}

	private static double calculateScale(double dataMinX, double dataMaxX) {
		double dataWidth = dataMaxX - dataMinX;

		return (Constants.MAP_PAGE_AREA_WIDTH_IN_POINTS / dataWidth);
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
