package edu.iu.scipolicy.visualization.geomaps.printing;

import java.awt.Color;

import com.vividsolutions.jts.geom.Coordinate;

import edu.iu.scipolicy.visualization.geomaps.ShapefileToPostScriptWriter;
import edu.iu.scipolicy.visualization.geomaps.utility.Calculator;
import edu.iu.scipolicy.visualization.geomaps.utility.Constants;

public class MapDisplayer {
	public static final boolean CLIP_TO_BOUNDING_BOX = true;
	public static final boolean BACKGROUND_TRANSPARENT = true;
	public static final Color BACKGROUND_COLOR = Color.CYAN;
	public static final boolean DRAW_BOUNDING_BOX = false;

	public static final double BOUNDING_BOX_LINE_WIDTH = .2;

	public static final String INDENT = "  ";

	private double displayLowerLeftX, displayLowerLeftY;
	private double displayUpperRightX, displayUpperRightY;
	private double displayCenterXInPoints, displayCenterYInPoints;
	private double dataCenterX, dataCenterY;
	private double scale;
	private double displayHeightInPoints;

	public MapDisplayer(double dataMinX, double dataMinY, double dataMaxX,
			double dataMaxY) {
		this.displayCenterXInPoints = Constants.MAP_CENTER_X_IN_POINTS;

		this.dataCenterX = Calculator.mean(dataMinX, dataMaxX);
		this.dataCenterY = Calculator.mean(dataMinY, dataMaxY);

		this.scale = calculateScale(dataMinX, dataMaxX);

		this.displayHeightInPoints = (scale * (dataMaxY - dataMinY));
		this.displayCenterYInPoints = calculateDisplayCenterY(displayHeightInPoints);

		Coordinate displayLowerLeftCorner = getDisplayCoordinate(new Coordinate(
				dataMinX, dataMinY));
		this.displayLowerLeftX = displayLowerLeftCorner.x;
		this.displayLowerLeftY = displayLowerLeftCorner.y;

		Coordinate displayUpperRightCorner = getDisplayCoordinate(new Coordinate(
				dataMaxX, dataMaxY));
		this.displayUpperRightX = displayUpperRightCorner.x;
		this.displayUpperRightY = displayUpperRightCorner.y;
	}

	public double getMapHeightInPoints() {
		return displayHeightInPoints;
	}

	private double calculateDisplayCenterY(double displayHeightInPoints) {
		return (Constants.PAGE_FOOTER_HEIGHT_IN_POINTS
				+ Constants.LEGEND_PAGE_AREA_HEIGHT_IN_POINTS + (displayHeightInPoints / 2.0));
	}

	/*
	 * Transform ordinate z from the data space to the display space Equivalent
	 * to the PostScript: displayCenter(X)InPoints displayCenter(Y)InPoints
	 * translate scale scale scale dataCenter(X) dataCenter(Y) translate
	 */
	private double positionOnDisplay(double z, double displayCenterInPoints,
			double scale, double dataCenter) {
		return displayCenterInPoints + (scale * (z - dataCenter));
	}

	public Coordinate getDisplayCoordinate(Coordinate coordinate) {
		return new Coordinate(positionOnDisplay(coordinate.x,
				displayCenterXInPoints, scale, dataCenterX), positionOnDisplay(
				coordinate.y, displayCenterYInPoints, scale, dataCenterY));
	}

	private double calculateScale(double dataMinX, double dataMaxX) {
		double dataWidth = dataMaxX - dataMinX;

		return (Constants.MAP_PAGE_AREA_WIDTH_IN_POINTS / dataWidth);
	}

	public String toPostScript() {
		String s = "";

		s += "newpath" + "\n";
		s += INDENT + displayLowerLeftX + " " + displayLowerLeftY + " moveto"
				+ "\n";
		s += INDENT + displayLowerLeftX + " " + displayUpperRightY + " lineto"
				+ "\n";
		s += INDENT + displayUpperRightX + " " + displayUpperRightY + " lineto"
				+ "\n";
		s += INDENT + displayUpperRightX + " " + displayLowerLeftY + " lineto"
				+ "\n";
		s += "closepath" + "\n";
		if (!BACKGROUND_TRANSPARENT) {
			s += "gsave" + "\n";
			s += INDENT
					+ ShapefileToPostScriptWriter
							.makeSetRGBColorCommand(BACKGROUND_COLOR);
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
