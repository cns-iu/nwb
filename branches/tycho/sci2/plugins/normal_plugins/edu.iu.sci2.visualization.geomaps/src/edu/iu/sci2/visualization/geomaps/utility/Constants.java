package edu.iu.sci2.visualization.geomaps.utility;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import edu.iu.sci2.visualization.geomaps.legend.Legend;


public class Constants {
	// The "point" is the fundamental unit of PostScript.  1 point = 1/72 inch.
	public static final double POINTS_PER_INCH = 72.0;	
	
	public static final String FONT_NAME = "Garamond";
		
	public static final double PAGE_WIDTH_IN_POINTS = 11.0 * POINTS_PER_INCH;	
	
	public static final double PAGE_HEADER_HEIGHT_IN_POINTS =
		0.75 * POINTS_PER_INCH;
	
	public static final double MAP_CENTER_X_IN_POINTS =
		PAGE_WIDTH_IN_POINTS / 2.0;
	
	public static final double PAGE_MARGIN_SIZE_IN_POINTS = 0.5 * POINTS_PER_INCH;
	
	/* No map page area height is necessary since, as we are fixing the aspect
	 * ratio, it will be implied by the map page area width.
	 * See calculatePageHeightInPoints(double).
	 */
	public static final double MAP_PAGE_AREA_WIDTH_IN_POINTS =
		(PAGE_WIDTH_IN_POINTS - (2 * PAGE_MARGIN_SIZE_IN_POINTS));
	
	public static final double PAGE_FOOTER_HEIGHT_IN_POINTS =
		PAGE_MARGIN_SIZE_IN_POINTS + (0.25 * POINTS_PER_INCH);

	public static final double LEGEND_PAGE_AREA_WIDTH_IN_POINTS =
		0.7 * MAP_PAGE_AREA_WIDTH_IN_POINTS;
	public static final double LEGEND_PAGE_AREA_HEIGHT_IN_POINTS =
		1.2 * POINTS_PER_INCH;
	
	public static final double LEGEND_PAGE_AREA_LOWER_LEFT_X_IN_POINTS =
		PAGE_WIDTH_IN_POINTS
		- LEGEND_PAGE_AREA_WIDTH_IN_POINTS
		+ PAGE_MARGIN_SIZE_IN_POINTS;
	public static final double LEGEND_PAGE_AREA_LOWER_LEFT_Y_IN_POINTS =
		PAGE_FOOTER_HEIGHT_IN_POINTS;
	
	public static final double METADATA_PAGE_AREA_LOWER_LEFT_X_IN_POINTS =
		PAGE_MARGIN_SIZE_IN_POINTS;
	public static final double METADATA_PAGE_AREA_LOWER_LEFT_Y_IN_POINTS =
		Legend.DEFAULT_LOWER_LEFT_Y_IN_POINTS;

	public static final String COUNTRIES_SHAPEFILE_KEY = "Countries";
	public static final String US_STATES_SHAPEFILE_KEY = "US States";

	public static final Map<String, String> SHAPEFILES;
	static {
		// Values should correspond to .shp files in the shapefiles package
		Map<String, String> t = new LinkedHashMap<String, String>();
		t.put(
			COUNTRIES_SHAPEFILE_KEY,
			"/edu/iu/sci2/visualization/geomaps/shapefiles/countries.shp");
		t.put(
			US_STATES_SHAPEFILE_KEY,
			"/edu/iu/sci2/visualization/geomaps/shapefiles/st99_d00.shp");		
		SHAPEFILES = Collections.unmodifiableMap(t);
	}
	
	public static final Map<String, String> FEATURE_NAME_KEY;
	static {
		/* Values should correspond to feature-identifying attribute of the 
		 * respective shapefile
		 */
		Map<String, String> t = new HashMap<String, String>();
		t.put(COUNTRIES_SHAPEFILE_KEY, "NAME");
		t.put(US_STATES_SHAPEFILE_KEY, "NAME");		
		FEATURE_NAME_KEY = Collections.unmodifiableMap(t);
	}
	
	public static final String ECKERT_IV_DISPLAY_NAME =	"Eckert IV";
	public static final String WINKEL_TRIPEL_DISPLAY_NAME =	"Winkel Tripel";
	public static final String MERCATOR_DISPLAY_NAME = "Mercator";
	public static final String ALBERS_EQUAL_AREA_DISPLAY_NAME =
		"Albers Equal-Area Conic";
	public static final String LAMBERT_CONFORMAL_CONIC_DISPLAY_NAME =
		"Lambert Conformal Conic";

	public static final Map<String, String> PROJECTIONS;
	static {
		// Values should correspond to keys in projection/wellKnownTexts.properties
		Map<String, String> t = new LinkedHashMap<String, String>();
		t.put(ECKERT_IV_DISPLAY_NAME, "eckertIV");
		t.put(WINKEL_TRIPEL_DISPLAY_NAME, "winkelTripel");
		t.put(MERCATOR_DISPLAY_NAME, "mercator");
		t.put(ALBERS_EQUAL_AREA_DISPLAY_NAME, "albersEqualArea");
		t.put(LAMBERT_CONFORMAL_CONIC_DISPLAY_NAME, "lambertConformalConic");
		PROJECTIONS = Collections.unmodifiableMap(t);
	}	
	
	public static final List<String> WKT_PROJECTIONS;
	static {
		/* The subset of our supported projections which will be parsed from
		 * a well-known text.  Not all of them can be since the WKT specifies
		 * a PROJECTION identifier, which for the projections that we include
		 * that are not currently supported by GeoTools, will result in an error
		 * trying to find the MapProjection when parsing the WKT.
		 */
		List<String> l = new ArrayList<String>();
		l.add(MERCATOR_DISPLAY_NAME);
		l.add(ALBERS_EQUAL_AREA_DISPLAY_NAME);
		l.add(LAMBERT_CONFORMAL_CONIC_DISPLAY_NAME);
		WKT_PROJECTIONS = l;
	}
	
	public static final Map<String, Range<Color>> COLOR_RANGES;
	static {
		Map<String, Range<Color>> t = new LinkedHashMap<String, Range<Color>>();
		t.put("Yellow to Blue", new Range<Color>(new Color(255, 255, 158), new Color(37, 52, 148)));
		t.put("Yellow to Red", new Range<Color>(new Color(254, 204, 92), new Color(177, 4, 39)));
		t.put("Green to Red", new Range<Color>(new Color(98, 164, 44), new Color(123, 21, 21)));
		t.put("Blue to Red", new Range<Color>(new Color(49, 243, 255), new Color(127, 4, 27)));		
		t.put("Gray to Black", new Range<Color>(new Color(214, 214, 214), new Color(0, 0 ,0)));	
		COLOR_RANGES = Collections.unmodifiableMap(t);
	}
	
	
	public static double calculatePageHeightInPoints(double mapHeightInPoints) {
		return (Constants.PAGE_HEADER_HEIGHT_IN_POINTS
				+ mapHeightInPoints
				+ Constants.LEGEND_PAGE_AREA_HEIGHT_IN_POINTS
				+ Constants.PAGE_FOOTER_HEIGHT_IN_POINTS);
	}
}
