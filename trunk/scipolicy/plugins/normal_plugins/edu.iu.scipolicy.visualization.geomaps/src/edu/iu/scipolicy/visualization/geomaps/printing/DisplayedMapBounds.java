package edu.iu.scipolicy.visualization.geomaps.printing;

import java.awt.Color;

import com.vividsolutions.jts.geom.Coordinate;

import edu.iu.scipolicy.visualization.geomaps.ShapefileToPostScriptWriter;
import edu.iu.scipolicy.visualization.geomaps.utility.Calculator;
import edu.iu.scipolicy.visualization.geomaps.utility.Constants;

public class DisplayedMapBounds {
	public static final boolean CLIP_TO_BOUNDING_BOX = true;
	public static final boolean BACKGROUND_TRANSPARENT = true;
	public static final Color BACKGROUND_COLOR = Color.CYAN;
	public static final boolean DRAW_BOUNDING_BOX = false;	
	
	public static final double BOUNDING_BOX_LINE_WIDTH = .2;
	
	/* The ranges of coordinates of features in the shapefile imply an
	 * aspect ratio.  "true" means we keep that ratio in the display.
	 * "false" means we use the dimensions below, even if they will result
	 * in a map that is "stretched" or "compressed" in one or both dimensions.
	 */
	public static final boolean FIX_ASPECT_RATIO = true;
	public static final String INDENT = "  ";

	private double displayLowerLeftX, displayLowerLeftY;
	private double displayUpperRightX, displayUpperRightY;
	private double displayCenterXInPoints, displayCenterYInPoints;
	private double dataCenterX, dataCenterY;
	private double scaleX, scaleY;
	
	/* The generated map will generally only take these requested dimensions
	 * if FIX_ASPECT_RATIO is false.
	 * When FIX_ASPECT_RATIO is true, the map is centered as indicated and
	 * drawn precisely large enough to fit the "tighter"
	 * dimension.  Unless by luck the data and display aspect ratios agree,
	 * this means that there will be empty space on either side in the
	 * "looser" dimension.
	 */
	public static final double REQUESTED_WIDTH_IN_POINTS =
		0.9 * Constants.MAP_PAGE_AREA_WIDTH_IN_POINTS;
	public static final double REQUESTED_HEIGHT_IN_POINTS =
		0.9 * Constants.MAP_PAGE_AREA_HEIGHT_IN_POINTS;
	

	public DisplayedMapBounds(double dataMinX, double dataMinY, double dataMaxX, double dataMaxY) {
		double displayWidthInPoints = REQUESTED_WIDTH_IN_POINTS;
		double displayHeightInPoints = REQUESTED_HEIGHT_IN_POINTS;

		this.displayCenterXInPoints = Constants.MAP_CENTER_X_IN_POINTS;
		this.displayCenterYInPoints = Constants.MAP_CENTER_Y_IN_POINTS;

		this.dataCenterX = Calculator.mean(dataMaxX, dataMinX);
		this.dataCenterY = Calculator.mean(dataMaxY, dataMinY);

		// Set scaleX and scaleY
		setScales(dataMinX, dataMinY, dataMaxX, dataMaxY, displayWidthInPoints, displayHeightInPoints);

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
	private double positionOnDisplay(double z,
									 double displayCenterInPoints,
									 double scale,
									 double dataCenter) {
		return displayCenterInPoints + (scale * (z - dataCenter));
	}
	
	public Coordinate getDisplayCoordinate(Coordinate coordinate) {
		return new Coordinate(
				positionOnDisplay(coordinate.x, displayCenterXInPoints, scaleX, dataCenterX),
				positionOnDisplay(coordinate.y, displayCenterYInPoints, scaleY, dataCenterY));
	}

	private void setScales(double dataMinX, double dataMinY,
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
		if (!BACKGROUND_TRANSPARENT) {
			s += "gsave" + "\n";
			s += INDENT + ShapefileToPostScriptWriter.makeSetRGBColorCommand(BACKGROUND_COLOR);
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
