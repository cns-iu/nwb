package edu.iu.sci2.visualization.geomaps.viz.ps;

import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Point2D;
import java.util.List;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;

public class PSUtility {
	public static final ImmutableMap<Integer, String> PS_FONT_NAME_SUFFIX_FOR_FONT_STYLE =
			ImmutableMap.of(
					Font.BOLD, "-Bold",
					Font.ITALIC, "-Italic");
	
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
	 * Appends style info to the font name in a way PostScript understands. 
	 * 
	 * <p>If {@code font}'s name is "Arial" then "MT" is appended.
	 */
	public static String psFontName(Font font) {
		String name = font.getName();
		
		int style = font.getStyle();
		if (PS_FONT_NAME_SUFFIX_FOR_FONT_STYLE.containsKey(style)) {
			name += PS_FONT_NAME_SUFFIX_FOR_FONT_STYLE.get(style);
		}
		
		if (Objects.equal(font.getName(), "Arial")) {
			name += "MT";
		}
		
		return name;
	}
	
	public static String showAndNewLine(String text, double fontSize) {
		return String.format("(%s) dup show stringwidth pop neg %f rmoveto \n", text, -fontSize);
	}
	
	public static String showLines(List<String> lines, Point2D.Double point, double fontSize) {
		StringBuilder builder = new StringBuilder();
		
		for (int ii = 0; ii < lines.size(); ii++) {
			builder.append(String.format("%f %f moveto ", point.x, point.y - (ii * fontSize)));
			builder.append(String.format("(%s) show ", lines.get(ii)));
		}
		
		return builder.toString();
	}

	// TODO Move somewhere more public
	/**
	 * Replaces each backslash with two backslashes, then each open parenthesis with an escaped
	 * open parenthesis, then each close parenthesis with an escaped parenthesis.
	 */
	public static String escapeForPostScript(String string) {
		return string.replace("\\", "\\\\").replace("(", "\\(").replace(")", "\\)");
	}

	public static String findscalesetfont(Font font) {
		return String.format("/%s findfont %d scalefont setfont ", psFontName(font), font.getSize());
	}

	public static String setgray(double brightness) {
		return String.format("%f setgray ", brightness);
	}
	
	/**
	 * @throws IllegalArgumentException
	 *             If {@code points} is empty.
	 */
	public static String path(List<? extends Point2D.Double> points) {
		Preconditions.checkArgument(!(points.isEmpty()));
		
		return path(
				Iterables.getFirst(points, null),
				Iterables.toArray(Iterables.skip(points, 1), Point2D.Double.class));
	}
	
	/**
	 * @throws IllegalArgumentException
	 *             If {@code points} is empty.
	 */
	public static String closedPath(List<? extends Point2D.Double> points) {
		Preconditions.checkArgument(!(points.isEmpty()));
		
		return path(points) + " closepath ";
	}
	
	public static String path(Point2D.Double first, Point2D.Double... rest) {
		StringBuilder builder = new StringBuilder();
		builder.append(" newpath ");
		builder.append(String.format("%s moveto ", xy(first)));
		
		for (Point2D.Double point : rest) {
			builder.append(String.format("%s lineto ", xy(point)));
		}
		
		return builder.toString();		
	}
	
	public static String closedPath(Point2D.Double first, Point2D.Double... rest) {
		return path(first, rest) + " closepath ";
	}
	
	public static String xy(Point2D.Double point) {
		return String.format("%f %f ", point.x, point.y);
	}
}
