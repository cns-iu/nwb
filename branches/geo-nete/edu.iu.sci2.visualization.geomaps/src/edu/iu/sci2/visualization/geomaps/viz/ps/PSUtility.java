package edu.iu.sci2.visualization.geomaps.viz.ps;

import java.awt.Color;

public class PSUtility {
	private PSUtility() {}

	public static String makeSetRGBColorCommand(Color color) {
		float[] colorComponents = new float[3];
		color.getColorComponents(colorComponents);
		float red = colorComponents[0];
		float green = colorComponents[1];
		float blue = colorComponents[2];
		
		return String.format("%f %f %f setrgbcolor\n", red, green, blue);
	}	
}
