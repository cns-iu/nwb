package edu.iu.sci2.visualization.geomaps.viz.ps;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import com.vividsolutions.jts.geom.Coordinate;

import edu.iu.sci2.visualization.geomaps.data.interpolation.Interpolator1D;
import edu.iu.sci2.visualization.geomaps.utility.Continuum;
import edu.iu.sci2.visualization.geomaps.utility.Rectangles;

public class GeoMapViewPageArea implements PostScriptable {
	private static final boolean CLIP_TO_BOUNDING_BOX = false;
	private static final boolean BACKGROUND_TRANSPARENT = true;
	private static final Color BACKGROUND_COLOR = Color.CYAN;
	private static final boolean DRAW_BOUNDING_BOX = false;
	private static final double BOUNDING_BOX_LINE_WIDTH = .2;
	private static final String INDENT = "  ";

	private final Rectangle2D.Double displayRectangle;
	private final Interpolator1D xInterpolator;
	private final Interpolator1D yInterpolator;

	public GeoMapViewPageArea(Rectangle2D.Double dataRectangle, Rectangle2D.Double displayRectangle) {
		this.displayRectangle = displayRectangle;
		
		xInterpolator = Interpolator1D.between(
				Rectangles.xRange(dataRectangle),
				Continuum.fromRange(Rectangles.xRange(displayRectangle)));
		
		yInterpolator = Interpolator1D.between(
				Rectangles.yRange(dataRectangle),
				Continuum.fromRange(Rectangles.yRange(displayRectangle)));
	}
	
	public Point2D.Double displayPointFor(Coordinate projectedCoordinate) {
		return new Point2D.Double(
				xInterpolator.apply(projectedCoordinate.x),
				yInterpolator.apply(projectedCoordinate.y));
		
	}

	@Override
	public String toPostScript() {
		String s = "";

		s += PSUtility.closedPath(
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
}
