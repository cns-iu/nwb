package edu.iu.sci2.visualization.geomaps.viz.ps;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import com.vividsolutions.jts.geom.Coordinate;

import edu.iu.sci2.visualization.geomaps.data.interpolation.Interpolator1D;
import edu.iu.sci2.visualization.geomaps.utility.Continuum;
import edu.iu.sci2.visualization.geomaps.utility.Dimension;
import edu.iu.sci2.visualization.geomaps.utility.Rectangles;
import edu.iu.sci2.visualization.geomaps.viz.PageLayout;

public class GeoMapViewPageArea implements PostScriptable {
	public static final boolean CLIP_TO_BOUNDING_BOX = false; // TODO was true
	public static final boolean BACKGROUND_TRANSPARENT = true;
	public static final Color BACKGROUND_COLOR = Color.CYAN;
	public static final boolean DRAW_BOUNDING_BOX = false;
	public static final double BOUNDING_BOX_LINE_WIDTH = .2;

	public static final String INDENT = "  ";

	private final Rectangle2D.Double dataRectangle;
	private final Rectangle2D.Double displayRectangle;

	public GeoMapViewPageArea(Rectangle2D.Double dataRectangle, PageLayout pageLayout) {
		this.dataRectangle = dataRectangle;
		
		double xScale = pageLayout.mapPageAreaMaxDimensions().getWidth() / dataRectangle.getWidth();
		double yScale = pageLayout.mapPageAreaMaxDimensions().getHeight() / dataRectangle.getHeight();		
		double scale = Math.min(xScale, yScale);		
		
		Dimension<Double> displayDimension =
				Dimension.ofSize(
						(scale * dataRectangle.getWidth()),
						(scale * dataRectangle.getHeight()));
		
		double availableMapHeight =	pageLayout.mapPageAreaMaxDimensions().getHeight();
		
		double mapCenterY =
				pageLayout.pageFooterHeight()
				+ pageLayout.legendariumReservedDimensions().getHeight()
				+ availableMapHeight / 2;
		
		Point2D.Double displayCenter =
				new Point2D.Double(pageLayout.mapCenterX(), mapCenterY);
		
		
		this.displayRectangle = Rectangles.forCenterWithDimensions(displayCenter, displayDimension);
	}
	
	public Point2D.Double displayPointFor(Coordinate coordinate) {
		Interpolator1D xInterpolator = Interpolator1D.between(
				Rectangles.xRange(dataRectangle),
				Continuum.fromRange(Rectangles.xRange(displayRectangle)));		
		
		Interpolator1D yInterpolator = Interpolator1D.between(
				Rectangles.yRange(dataRectangle),
				Continuum.fromRange(Rectangles.yRange(displayRectangle)));
		
		return new Point2D.Double(
				xInterpolator.apply(coordinate.x), yInterpolator.apply(coordinate.y));
	}
	
	public Rectangle2D.Double getDisplayRectangle() {
		return displayRectangle;
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
