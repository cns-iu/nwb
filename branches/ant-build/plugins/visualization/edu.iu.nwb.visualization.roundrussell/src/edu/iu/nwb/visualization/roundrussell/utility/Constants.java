package edu.iu.nwb.visualization.roundrussell.utility;

import java.awt.Color;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


public class Constants {
	// The "point" is the fundamental unit of PostScript.  1 point = 1/72 inch.
	public static final double POINTS_PER_INCH = 72.0;	
	
	public static final String FONT_NAME = "Garamond";
		
	public static final double PAGE_WIDTH_IN_POINTS = 8.5 * POINTS_PER_INCH;
	
	
	public static final double PAGE_HEADER_HEIGHT_IN_POINTS = 0.75 * POINTS_PER_INCH;
	
	public static final double PAGE_FOOTER_HEIGHT_IN_POINTS = 0.75 * POINTS_PER_INCH;
		
	public static final double LEGEND_PAGE_AREA_WIDTH_IN_POINTS = PAGE_WIDTH_IN_POINTS / 2.0;
	public static final double LEGEND_PAGE_AREA_HEIGHT_IN_POINTS = 1.5 * POINTS_PER_INCH;
	
	public static final double LEGEND_PAGE_AREA_LOWER_LEFT_X_IN_POINTS = PAGE_WIDTH_IN_POINTS / 2.0;
	public static final double LEGEND_PAGE_AREA_LOWER_LEFT_Y_IN_POINTS = PAGE_FOOTER_HEIGHT_IN_POINTS;

	public static final Map<String, Range<Color>> COLOR_RANGES;
	static {
		Map<String, Range<Color>> t = new LinkedHashMap<String, Range<Color>>();
		t.put("Yellow to blue", new Range<Color>(new Color(255, 255, 158), new Color(37, 52, 148)));
		t.put("Yellow to red", new Range<Color>(new Color(254, 204, 92), new Color(177, 4, 39)));
		t.put("Green to red", new Range<Color>(new Color(98, 164, 44), new Color(123, 21, 21)));
		t.put("Blue to red", new Range<Color>(new Color(49, 243, 255), new Color(127, 4, 27)));		
		t.put("Gray to black", new Range<Color>(new Color(214, 214, 214), new Color(0, 0 ,0)));	
		COLOR_RANGES = Collections.unmodifiableMap(t);
	}
}
