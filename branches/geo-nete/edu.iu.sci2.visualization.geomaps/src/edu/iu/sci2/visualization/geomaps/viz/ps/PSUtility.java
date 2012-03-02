package edu.iu.sci2.visualization.geomaps.viz.ps;

import java.awt.Color;

public class PSUtility {
	private PSUtility() {}

	/**
	 * Generates a {@code setrgbcolor} command for {@code color}.
	 */
	public static String makeSetRGBColorCommand(Color color) {
		float[] colorComponents = new float[3];
		color.getColorComponents(colorComponents);
		float red = colorComponents[0];
		float green = colorComponents[1];
		float blue = colorComponents[2];
		
		return String.format("%f %f %f setrgbcolor\n", red, green, blue);
	}

	/**
	 * Replaces each backslash with two backslashes.
	 */
	public static String escapeForPostScript(String string) {
		return string.replace("\\", "\\\\");
	}

	public static String findscalesetfont(String fontName, double fontSize) {
		return String.format("/%s findfont %f scalefont setfont ", fontName, fontSize);
	}

	public static String setgray(double brightness) {
		return String.format("%f setgray ", brightness);
	}	
}
