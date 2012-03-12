package edu.iu.sci2.visualization.geomaps.viz;

import java.awt.Color;

import com.google.common.collect.ImmutableMap;

import edu.iu.sci2.visualization.geomaps.utility.Range;


public class Constants {
	public static final ImmutableMap<String, Range<Color>> COLOR_RANGES = ImmutableMap.of(
			"Yellow to Blue", Range.between(new Color(255, 255, 158), new Color(37, 52, 148)),
			"Yellow to Red", Range.between(new Color(254, 204, 92), new Color(177, 4, 39)),
			"Green to Red", Range.between(new Color(98, 164, 44), new Color(123, 21, 21)),
			"Blue to Red", Range.between(new Color(49, 243, 255), new Color(127, 4, 27)),
			"Gray to Black", Range.between(new Color(214, 214, 214), new Color(0, 0, 0)));
	
	public static final String FONT_NAME = "Garamond";
}
