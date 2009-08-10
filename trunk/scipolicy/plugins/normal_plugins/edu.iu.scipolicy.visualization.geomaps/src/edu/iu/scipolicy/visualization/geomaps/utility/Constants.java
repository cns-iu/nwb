package edu.iu.scipolicy.visualization.geomaps.utility;



public class Constants {
	// The "point" is the fundamental unit of PostScript.  1 point = 1/72 inch.
	public static final double POINTS_PER_INCH = 72.0;
	
	
	public static final String FONT_NAME = "Garamond";
	
	
	public static final double PAGE_WIDTH_IN_POINTS = 11.0 * POINTS_PER_INCH;
	public static final double PAGE_HEIGHT_IN_POINTS = 8.5 * POINTS_PER_INCH;
	
	
	public static final double PAGE_HEADER_HEIGHT_IN_POINTS = 0.5 * POINTS_PER_INCH;
	
	public static final double MAP_CENTER_X_IN_POINTS = PAGE_WIDTH_IN_POINTS / 2.0;
	public static final double MAP_CENTER_Y_IN_POINTS = PAGE_HEIGHT_IN_POINTS - PAGE_HEADER_HEIGHT_IN_POINTS - (Constants.MAP_PAGE_AREA_HEIGHT_IN_POINTS / 2.0);
	
	public static final double MAP_PAGE_AREA_HEIGHT_IN_POINTS = 6.0 * POINTS_PER_INCH;
	public static final double MAP_PAGE_AREA_WIDTH_IN_POINTS = PAGE_WIDTH_IN_POINTS;
	
	public static final double PAGE_FOOTER_HEIGHT_IN_POINTS = 0.5 * POINTS_PER_INCH;
		
	public static final double LEGEND_PAGE_AREA_WIDTH_IN_POINTS = PAGE_WIDTH_IN_POINTS / 2.0;
	public static final double LEGEND_PAGE_AREA_HEIGHT_IN_POINTS = 1.5 * POINTS_PER_INCH;
	
	public static final double LEGEND_PAGE_AREA_LOWER_LEFT_X_IN_POINTS = PAGE_WIDTH_IN_POINTS / 2.0;
	public static final double LEGEND_PAGE_AREA_LOWER_LEFT_Y_IN_POINTS = PAGE_FOOTER_HEIGHT_IN_POINTS + (0.70 * LEGEND_PAGE_AREA_HEIGHT_IN_POINTS);


	
}
