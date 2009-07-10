package edu.iu.scipolicy.visualization.geomaps.printing;

import java.awt.Color;

import com.vividsolutions.jts.geom.Coordinate;

import edu.iu.scipolicy.visualization.geomaps.ShapefileToPostScript;

public class PostScriptBoundingBox { // TODO Rename?  to Map?  ShapefileMap?  FeatureMap?  ProjectedMap?
	public static final double BOUNDING_BOX_LINE_WIDTH = .2;
	public static final String INDENT = "  ";
	public static final double DISPLAY_LOWER_LEFT_X_IN_INCHES = 0.0;
	public static final double DISPLAY_LOWER_LEFT_Y_IN_INCHES = 4;
	public static final double DISPLAY_WIDTH_IN_INCHES = 8.5;
	public static final double DISPLAY_HEIGHT_IN_INCHES = 11 - DISPLAY_LOWER_LEFT_Y_IN_INCHES;
	public static final double POINTS_PER_INCH = 72.0;
	public static final boolean FIX_ASPECT_RATIO = true;

	private double displayMinX, displayMinY, displayMaxX, displayMaxY;
	private double displayCenterXInPoints, displayCenterYInPoints;
	private double scaleX, scaleY;
	private double dataCenterX, dataCenterY;

	public PostScriptBoundingBox(double dataMinX, double dataMinY, double dataMaxX, double dataMaxY) {
		double displayWidthInPoints = POINTS_PER_INCH * DISPLAY_WIDTH_IN_INCHES;
		double displayHeightInPoints = POINTS_PER_INCH * DISPLAY_HEIGHT_IN_INCHES;

		double displayLowerLeftXInPoints = POINTS_PER_INCH * DISPLAY_LOWER_LEFT_X_IN_INCHES;
		double displayLowerLeftYInPoints = POINTS_PER_INCH * DISPLAY_LOWER_LEFT_Y_IN_INCHES;

		this.displayCenterXInPoints = displayLowerLeftXInPoints + displayWidthInPoints / 2;
		this.displayCenterYInPoints = displayLowerLeftYInPoints	+ displayHeightInPoints / 2;

		this.dataCenterX = (dataMaxX + dataMinX) / 2;
		this.dataCenterY = (dataMaxY + dataMinY) / 2;

		calculateScale(dataMinX, dataMinY, dataMaxX, dataMaxY, displayWidthInPoints, displayHeightInPoints);

		Coordinate displayLowerLeftCorner = getDisplayCoordinate(new Coordinate(dataMinX, dataMinY));
		this.displayMinX = displayLowerLeftCorner.x;
		this.displayMinY = displayLowerLeftCorner.y;
		
		Coordinate displayUpperRightCorner = getDisplayCoordinate(new Coordinate(dataMaxX, dataMaxY));
		this.displayMaxX = displayUpperRightCorner.x;
		this.displayMaxY = displayUpperRightCorner.y;
	}

	/* 
	 * Transform ordinate z from the data space to the display space
	 * Equivalent to the PostScript:
	 * 		displayCenter(X)InPoints displayCenter(Y)InPoints translate
	 * 		scale(X) scale(Y) scale
	 *  	dataCenter(X) dataCenter(Y) translate
	 */
	private double positionOnDisplay(double z, double displayCenterInPoints, double scale, double dataCenter) {
		return displayCenterInPoints + (scale * (z - dataCenter));
	}
	public Coordinate getDisplayCoordinate(Coordinate coordinate) {
		return new Coordinate(
				positionOnDisplay(coordinate.x, displayCenterXInPoints, scaleX, dataCenterX),
				positionOnDisplay(coordinate.y, displayCenterYInPoints, scaleY, dataCenterY));
	}

	/* Corner specification for the BoundingBox PostScript comment.
	 * <lower left x> <lower left y> <upper left x> <upper right y>
	 */
	public String getCoordinatesString() {
		return displayMinX + " " + displayMinY + " " + displayMaxX + " " + displayMaxY;
	}

	private void calculateScale(double dataMinX, double dataMinY,
			double dataMaxX, double dataMaxY, double displayWidthInPoints, double displayHeightInPoints) {
		double dataWidth = dataMaxX - dataMinX;
		double dataHeight = dataMaxY - dataMinY;

		this.scaleX = displayWidthInPoints / dataWidth;
		this.scaleY = displayHeightInPoints / dataHeight;

		if ( FIX_ASPECT_RATIO ) {
			double scale = Math.min(scaleX, scaleY);

			scaleX = scale;
			scaleY = scale;
		}
	}

	public String toPostScript(boolean stroke, boolean backgroundTransparent, Color backgroundColor, boolean useAsClip) {
		String s = "";

		s += "% Bounding box" + "\n";
		s += "newpath" + "\n";
		s += INDENT + displayMinX + " " + displayMinY + " moveto" + "\n";
		s += INDENT + displayMinX + " " + displayMaxY + " lineto" + "\n";
		s += INDENT + displayMaxX + " " + displayMaxY + " lineto" + "\n";
		s += INDENT + displayMaxX + " " + displayMinY + " lineto" + "\n";
		s += "closepath" + "\n";
		if ( !backgroundTransparent ) {
			s += "gsave" + "\n";
			s += INDENT + ShapefileToPostScript.makeSetRGBColorCommand(backgroundColor);
			s += INDENT + "fill" + "\n";
			s += "grestore" + "\n";
		}
		if ( stroke ) {
			s += "gsave" + "\n";
			s += INDENT + BOUNDING_BOX_LINE_WIDTH + " setlinewidth" + "\n";
			s += INDENT + "stroke" + "\n";
			s += "grestore" + "\n";
		}
		if ( useAsClip ) {
			s += "clip" + "\n";
		}

		return s;
	}
}
