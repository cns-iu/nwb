package edu.iu.scipolicy.visualization.geomaps.utility;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

public class RGBAverager {
	public static Color mean(Color color1, Color color2) {
		return mean(Arrays.asList(new Color[]{ color1, color2 }));
	}
	public static Color mean(List<Color> colors) {
		// Beware overflow when colors.size() is large
		double redTotal = 0.0;
		double greenTotal = 0.0;
		double blueTotal = 0.0;
		
		for (Color color : colors) {
			float[] colorComponents = new float[3];
			color.getColorComponents(colorComponents);
			
			redTotal += colorComponents[0];
			greenTotal += colorComponents[1];
			blueTotal += colorComponents[2];
		}
		
		return new Color((float) redTotal / colors.size(),
						 (float) greenTotal / colors.size(),
						 (float) blueTotal / colors.size());
	}
}
