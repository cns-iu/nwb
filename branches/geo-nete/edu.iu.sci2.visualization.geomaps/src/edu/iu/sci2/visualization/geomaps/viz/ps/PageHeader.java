//package edu.iu.sci2.visualization.geomaps.viz.ps;
//
//import com.google.common.base.Strings;
//
//import edu.iu.sci2.visualization.geomaps.viz.Constants;
//
//public class PageHeader {
//	public static final String INDENT = "    ";
//	public static final String SEP = " | ";
//	
//	public static final String FONT_NAME = Constants.FONT_NAME;
//	public static final double FONT_SIZE = 6;
//	public static final double FONT_BRIGHTNESS = 0.35;
//	
//	public static final double LOWER_LEFT_X_IN_POINTS =
//		Constants.PAGE_MARGIN_IN_POINTS;
//		
//	private final String dataLabel;
//	private final double lowerLeftYInPoints;
//
//	
//	public PageHeader(String dataLabel, double pageHeightInPoints) {
//		this.dataLabel = dataLabel;
//		
//		this.lowerLeftYInPoints =
//			pageHeightInPoints - Constants.PAGE_MARGIN_IN_POINTS - FONT_SIZE;
//	}
//	
//	@Override
//	public String toString() {
//		return Strings.isNullOrEmpty(dataLabel) ? "" : ("Generated from " + dataLabel);
//	}
//
//	public String toPostScript() {
//		StringBuilder builder = new StringBuilder();
//		
//		builder.append("gsave" + "\n");
//		builder.append(PSUtility.findscalesetfont(FONT_NAME, FONT_SIZE) + "\n");
//		builder.append(PSUtility.setgray(FONT_BRIGHTNESS) + "\n");
//		builder.append(INDENT + LOWER_LEFT_X_IN_POINTS + " " + lowerLeftYInPoints + " moveto" + "\n");
//		builder.append(INDENT + "(" + PSUtility.escapeForPostScript(toString()) + ") show" + "\n");
//		builder.append("grestore" + "\n");
//		
//		return builder.toString();
//	}
//}
