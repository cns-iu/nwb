package edu.iu.nwb.visualization.roundrussell.utility;

import java.awt.Color;


public class Averager {
	
	public static double mean(Double... values) {
		if (values.length > 0) {
			// Beware overflow when values.length is large
			double sum = 0.0;
			
			for (double value : values) {				
				sum += value;
			}
			
			return (sum / values.length);
		} else {
			return 0.0;
		}
	}

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
