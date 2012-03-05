package edu.iu.sci2.visualization.geomaps.viz.ps;

import java.awt.Color;
import java.awt.geom.Point2D;

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
		
		return String.format("%f %f %f setrgbcolor ", red, green, blue);
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
	
	public static String path(Point2D.Double first, Point2D.Double... rest) {
		StringBuilder builder = new StringBuilder();
		builder.append("newpath ");
		builder.append(String.format("%s moveto ", xy(first)));
		
		for (Point2D.Double point : rest) {
			builder.append(String.format("%s lineto ", xy(point)));
		}
		
		builder.append("closepath ");
		
		return builder.toString();		
	}
	
	public static String xy(Point2D.Double point) {
		return String.format("%f %f ", point.x, point.y);
	}
}
