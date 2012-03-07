package edu.iu.sci2.visualization.geomaps.viz;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.EnumSet;

import com.google.common.collect.ImmutableMap;

import edu.iu.sci2.visualization.geomaps.utility.Dimension;
import edu.iu.sci2.visualization.geomaps.utility.Range;


public class Constants {
	// The "point" is the fundamental unit of PostScript.  1 point = 1/72 inch.
	public static final double POINTS_PER_INCH = 72.0;
	
	public static final String FONT_NAME = "Garamond";
		
	public static final double PAGE_WIDTH_IN_POINTS = 11.0 * POINTS_PER_INCH;
	public static final double PAGE_HEIGHT_IN_POINTS = 8.5 * POINTS_PER_INCH;
	
	public static final double PAGE_HEADER_HEIGHT_IN_POINTS = 1.5 * POINTS_PER_INCH; // TODO Fix for metadata
	
	public static final double MAP_CENTER_X_IN_POINTS = PAGE_WIDTH_IN_POINTS / 2.0;
	
	public static final double PAGE_MARGIN_IN_POINTS = 0.5 * POINTS_PER_INCH;
	
//	/* No map page area height is necessary since, as we are fixing the aspect
//	 * ratio, it will be implied by the map page area width.
//	 * See calculatePageHeightInPoints(double).
//	 */
//	public static final double MAP_PAGE_AREA_WIDTH_IN_POINTS =
//		(PAGE_WIDTH_IN_POINTS - (2 * PAGE_MARGIN_IN_POINTS));
	
	public static final double PAGE_FOOTER_HEIGHT_IN_POINTS =
		PAGE_MARGIN_IN_POINTS + (0.25 * POINTS_PER_INCH);

	public static final Dimension<Double> LEGEND_PAGE_AREA_DIMENSION = Dimension.ofSize(
			0.7 * PAGE_WIDTH_IN_POINTS,
			1.2 * POINTS_PER_INCH);
	
	public static final Point2D.Double LEGEND_PAGE_AREA_LOWER_LEFT = new Point2D.Double(
			PAGE_MARGIN_IN_POINTS,
			PAGE_FOOTER_HEIGHT_IN_POINTS);
	
	
	public static final double LEGEND_COMPOSITE_WIDTH_IN_POINTS = LEGEND_PAGE_AREA_DIMENSION.getWidth();
	public static final Point2D.Double LEGEND_COMPOSITE_LOWER_LEFT = new Point2D.Double(
			LEGEND_PAGE_AREA_LOWER_LEFT.getX(),
			LEGEND_PAGE_AREA_LOWER_LEFT.getY() + (0.75 * LEGEND_PAGE_AREA_DIMENSION.getHeight()));
	
	
	public static final Point2D.Double HEADER_LOWER_LEFT = new Point2D.Double(
			PAGE_MARGIN_IN_POINTS, PAGE_HEIGHT_IN_POINTS - PAGE_MARGIN_IN_POINTS);
	
	public static final ImmutableMap<String, Range<Color>> COLOR_RANGES = ImmutableMap.of(
			"Yellow to Blue", Range.between(new Color(255, 255, 158), new Color(37, 52, 148)),
			"Yellow to Red", Range.between(new Color(254, 204, 92), new Color(177, 4, 39)),
			"Green to Red", Range.between(new Color(98, 164, 44), new Color(123, 21, 21)),
			"Blue to Red", Range.between(new Color(49, 243, 255), new Color(127, 4, 27)),
			"Gray to Black", Range.between(new Color(214, 214, 214), new Color(0, 0 ,0)));

	public static final double MAP_PAGE_AREA_MAX_HEIGHT_IN_POINTS = PAGE_HEIGHT_IN_POINTS -
			(PAGE_HEADER_HEIGHT_IN_POINTS
			+ LEGEND_PAGE_AREA_DIMENSION.getHeight()
			+ PAGE_FOOTER_HEIGHT_IN_POINTS);

	public static final double MAP_PAGE_AREA_MAX_WIDTH_IN_POINTS = PAGE_WIDTH_IN_POINTS - 2 * PAGE_MARGIN_IN_POINTS;

	public static final Dimension<Double> COLOR_GRADIENT_DIMENSION =
			Dimension.ofSize(
					0.8 * (LEGEND_COMPOSITE_WIDTH_IN_POINTS / EnumSet.allOf(CircleDimension.class).size()),
					10.0);
}
