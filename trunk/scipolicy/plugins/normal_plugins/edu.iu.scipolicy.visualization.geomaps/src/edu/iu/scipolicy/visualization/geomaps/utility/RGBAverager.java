package edu.iu.scipolicy.visualization.geomaps.utility;

import java.awt.Color;

public class RGBAverager {
	public static Color mean(Color... colors) {
		// Beware overflow when colors.length is large
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
		
		return new Color((float) redTotal / colors.length,
						 (float) greenTotal / colors.length,
						 (float) blueTotal / colors.length);
	}
}
