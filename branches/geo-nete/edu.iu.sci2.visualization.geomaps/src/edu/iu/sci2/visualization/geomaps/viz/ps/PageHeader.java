package edu.iu.sci2.visualization.geomaps.viz.ps;

import java.awt.geom.Point2D;

import com.google.common.collect.ImmutableList;

import edu.iu.sci2.visualization.geomaps.viz.Constants;

public class PageHeader {
	public static final String INDENT = "	";
	public static final String FONT_NAME = Constants.FONT_NAME;
	public static final double TITLE_FONT_SIZE = 12;
	public static final double TITLE_FONT_BRIGHTNESS = 0.0;
	public static final double OTHER_DATA_FONT_SIZE = 10;
	public static final double OTHER_DATA_FONT_BRIGHTNESS = 0.0;
	
	public static final Point2D.Double LOWER_LEFT = Constants.HEADER_LOWER_LEFT;
	
	private final String title;
	private final String subtitle;
	private final ImmutableList<String> extraInfo;
	
	public PageHeader(String title, String subtitle, String... extraInfo) {
		this.title = title;
		this.subtitle = subtitle;		
		this.extraInfo = ImmutableList.copyOf(extraInfo);
	}
	
	
	public String toPostScript() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("% Page info" + "\n");
		builder.append("gsave" + "\n");
		
		builder.append(INDENT + "% Show title and subtitle" + "\n");
		builder.append(INDENT + LOWER_LEFT.x + " " + LOWER_LEFT.y + " moveto" + "\n");
		
		builder.append(PSUtility.findscalesetfont(FONT_NAME, TITLE_FONT_SIZE) + "\n");
		builder.append(PSUtility.setgray(TITLE_FONT_BRIGHTNESS) + "\n");
		builder.append(INDENT + "gsave" + "\n");
		builder.append(INDENT + INDENT + "(" + title + ") show " + "( ) show " + "((" + subtitle + ")) show" +"\n");
		builder.append(INDENT + "grestore" + "\n");
		
		builder.append(INDENT + "% Show the rest of the info" + "\n");
		builder.append(INDENT + "0 " + (-(TITLE_FONT_SIZE + 5)) + " rmoveto");
		builder.append(PSUtility.findscalesetfont(FONT_NAME, OTHER_DATA_FONT_SIZE) + "\n");
		builder.append(PSUtility.setgray(OTHER_DATA_FONT_BRIGHTNESS) + "\n");
		for (String infoBit : extraInfo) {
			builder.append(INDENT + "gsave" + "\n");
			builder.append(INDENT + INDENT + "(" + infoBit + ")" + " show" + "\n");
			builder.append(INDENT + "grestore" + "\n");
			builder.append(INDENT + "0 " + (-(OTHER_DATA_FONT_SIZE + 5)) + " rmoveto" + "\n");
		}
		
		builder.append("grestore" + "\n");
		
		return builder.toString();
	}
}
