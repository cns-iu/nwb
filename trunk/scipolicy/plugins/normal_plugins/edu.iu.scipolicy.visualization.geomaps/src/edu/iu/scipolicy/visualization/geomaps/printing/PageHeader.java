package edu.iu.scipolicy.visualization.geomaps.printing;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import edu.iu.scipolicy.visualization.geomaps.utility.Constants;

public class PageHeader {
	public static final String INDENT = "    ";
	public static final String SEP = " | ";
	
	public static final String FONT_NAME = Constants.FONT_NAME;
	public static final double FONT_SIZE = 6;
	public static final double FONT_BRIGHTNESS = 0.35;
	
	public static final double LOWER_LEFT_X_IN_POINTS =
		0.4 * Constants.POINTS_PER_INCH;
		
	
	private String authorName;
	private String dataLabel;
	private double lowerLeftYInPoints;	

	
	public PageHeader(String authorName, String dataLabel, double pageHeightInPoints) {
		this.authorName = authorName;
		this.dataLabel = dataLabel;
		
		// Place the text so that its top is 0.4 inches from the top of the page
		this.lowerLeftYInPoints =
			pageHeightInPoints - (0.4 * Constants.POINTS_PER_INCH) - FONT_SIZE;
	}	
	
	public String toString() {
		String s = "";
		
		if (stringSpecified(authorName)) {
			s += (authorName + SEP);
		}
		
		if (stringSpecified(dataLabel)) {
			s += ("Generated from " + dataLabel + SEP);
		}
		
		s += timestamp();
		
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

	public static String timestamp() {
		Calendar cal = Calendar.getInstance();
	    SimpleDateFormat sdf =
	    	new SimpleDateFormat("EEEEEEEEEEEEE, dd MMMMMMMMMMMM, yyyy HH:mm zzz");
	    return sdf.format(cal.getTime());
	}
}
