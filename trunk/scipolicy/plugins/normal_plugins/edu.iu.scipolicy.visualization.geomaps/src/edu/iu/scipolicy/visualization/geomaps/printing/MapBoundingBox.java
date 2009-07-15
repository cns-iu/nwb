package edu.iu.scipolicy.visualization.geomaps.printing;

import java.awt.Color;

import com.vividsolutions.jts.geom.Coordinate;

import edu.iu.scipolicy.visualization.geomaps.ShapefileToPostScriptWriter;

public class MapBoundingBox {
	// PostScript is not Encapsulated if this is set to false!
	public static final boolean CLIP_TO_BOUNDING_BOX = true;
	public static final boolean BACKGROUND_TRANSPARENT = true;
	public static final Color BACKGROUND_COLOR = Color.CYAN;	
	public static final boolean DRAW_BOUNDING_BOX = false;
	
	public static final double BOUNDING_BOX_LINE_WIDTH = .2;
	public static final String INDENT = "  ";
	public static final double DISPLAY_LOWER_LEFT_X_IN_INCHES = 0.0;
	public static final double DISPLAY_LOWER_LEFT_Y_IN_INCHES = 4;
	public static final double DISPLAY_WIDTH_IN_INCHES = 8.5;
	public static final double DISPLAY_HEIGHT_IN_INCHES = 11 - DISPLAY_LOWER_LEFT_Y_IN_INCHES;
	public static final double POINTS_PER_INCH = 72.0;
	public static final boolean FIX_ASPECT_RATIO = true;

	private double displayLowerLeftX, displayLowerLeftY, displayUpperRightX, displayUpperRightY;
	private double displayCenterXInPoints, displayCenterYInPoints;
	private double scaleX, scaleY;
	private double dataCenterX, dataCenterY;
	

	public MapBoundingBox(double dataMinX, double dataMinY, double dataMaxX, double dataMaxY) {
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
		this.displayLowerLeftX = displayLowerLeftCorner.x;
		this.displayLowerLeftY = displayLowerLeftCorner.y;
		
		Coordinate displayUpperRightCorner = getDisplayCoordinate(new Coordinate(dataMaxX, dataMaxY));
		this.displayUpperRightX = displayUpperRightCorner.x;
		this.displayUpperRightY = displayUpperRightCorner.y;
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
		return displayLowerLeftX + " " + displayLowerLeftY + " " + displayUpperRightX + " " + displayUpperRightY;
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

	public String toPostScript() {
		String s = "";

		s += "newpath" + "\n";
		s += INDENT + displayLowerLeftX + " " + displayLowerLeftY + " moveto" + "\n";
		s += INDENT + displayLowerLeftX + " " + displayUpperRightY + " lineto" + "\n";
		s += INDENT + displayUpperRightX + " " + displayUpperRightY + " lineto" + "\n";
		s += INDENT + displayUpperRightX + " " + displayLowerLeftY + " lineto" + "\n";
		s += "closepath" + "\n";
		if ( !BACKGROUND_TRANSPARENT ) {
			s += "gsave" + "\n";
			s += INDENT + ShapefileToPostScriptWriter.makeSetRGBColorCommand(BACKGROUND_COLOR);
			s += INDENT + "fill" + "\n";
			s += "grestore" + "\n";
		}
		if ( DRAW_BOUNDING_BOX ) {
			s += "gsave" + "\n";
			s += INDENT + BOUNDING_BOX_LINE_WIDTH + " setlinewidth" + "\n";
			s += INDENT + "stroke" + "\n";
			s += "grestore" + "\n";
		}
		if ( CLIP_TO_BOUNDING_BOX ) {
			s += "clip" + "\n";
		}

		return s;
	}

	public double getLowerLeftX() {
		return displayLowerLeftX;
	}

	public double getLowerLeftY() {
		return displayLowerLeftY;
	}

	public double getUpperRightX() {
		return displayUpperRightX;
	}

	public double getUpperRightY() {
		return displayUpperRightY;
	}
}
