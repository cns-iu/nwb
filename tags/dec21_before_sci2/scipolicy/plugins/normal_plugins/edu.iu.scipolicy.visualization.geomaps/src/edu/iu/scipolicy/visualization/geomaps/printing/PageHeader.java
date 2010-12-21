package edu.iu.scipolicy.visualization.geomaps.printing;

import edu.iu.scipolicy.visualization.geomaps.utility.Constants;

public class PageHeader {
	public static final String INDENT = "    ";
	public static final String SEP = " | ";
	
	public static final String FONT_NAME = Constants.FONT_NAME;
	public static final double FONT_SIZE = 6;
	public static final double FONT_BRIGHTNESS = 0.35;
	
	public static final double LOWER_LEFT_X_IN_POINTS =
		Constants.PAGE_MARGIN_SIZE_IN_POINTS;
		
	private String dataLabel;
	private double lowerLeftYInPoints;	

	
	public PageHeader(String dataLabel, double pageHeightInPoints) {
		this.dataLabel = dataLabel;
		
		this.lowerLeftYInPoints =
			pageHeightInPoints - Constants.PAGE_MARGIN_SIZE_IN_POINTS - FONT_SIZE;
	}	
	
	@Override
	public String toString() {
		String s = "";
		
		if (stringSpecified(dataLabel)) {
			s += ("Generated from " + dataLabel);
		}
		
		return s;
	}

	private boolean stringSpecified(String string) {
		return ((string != null) && (!("".equals(string))));
	}
	
	public String toPostScript() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("gsave" + "\n");
		builder.append(INDENT + "/" + FONT_NAME + " findfont" + "\n");
		builder.append(INDENT + FONT_SIZE + " scalefont" + "\n");
		builder.append(INDENT + "setfont" + "\n");
		builder.append(INDENT + FONT_BRIGHTNESS + " setgray" + "\n");
		builder.append(INDENT + LOWER_LEFT_X_IN_POINTS + " " + lowerLeftYInPoints + " moveto" + "\n");
		builder.append(INDENT + "(" + escapeForPostScript(toString()) + ") show" + "\n");
		builder.append("grestore" + "\n");
		
		return builder.toString();
	}
	
	public static String escapeForPostScript(String string) {
		// Replace each backslash with two backslashes
		return string.replace("\\", "\\\\");
	}
}
