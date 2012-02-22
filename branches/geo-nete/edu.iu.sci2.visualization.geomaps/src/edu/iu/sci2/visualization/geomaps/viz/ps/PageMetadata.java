package edu.iu.sci2.visualization.geomaps.viz.ps;

import java.util.ArrayList;
import java.util.List;

import edu.iu.sci2.visualization.geomaps.viz.Constants;

public class PageMetadata {
	public static final String INDENT = "	";
	public static final String FONT_NAME = Constants.FONT_NAME;
	public static final double TITLE_FONT_SIZE = 12;
	public static final double TITLE_FONT_BRIGHTNESS = 0.0;
	public static final double OTHER_DATA_FONT_SIZE = 10;
	public static final double OTHER_DATA_FONT_BRIGHTNESS = 0.0;
	
	public static final double LOWER_LEFT_X_IN_POINTS =
		Constants.METADATA_PAGE_AREA_LOWER_LEFT_X_IN_POINTS;
	public static final double LOWER_LEFT_Y_IN_POINTS =
		Constants.METADATA_PAGE_AREA_LOWER_LEFT_Y_IN_POINTS;
	
	private final String title;
	private final String subtitle;
	private final List<String> metadata;
	
	
	public PageMetadata(String title, String subtitle) {
		this.title = title;
		this.subtitle = subtitle;
		
		this.metadata = new ArrayList<String>();
	}
	
	
	public void add(String metadatum) {
		metadata.add(metadatum);
	}
	
	public String toPostScript() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("% Page metadata" + "\n");
		builder.append("gsave" + "\n");
		
		builder.append(INDENT + "% Show title and subtitle" + "\n");
		builder.append(INDENT + LOWER_LEFT_X_IN_POINTS + " " + LOWER_LEFT_Y_IN_POINTS + " moveto" + "\n");
		
		builder.append(INDENT + "/" + FONT_NAME + " findfont " + TITLE_FONT_SIZE + " scalefont setfont" + "\n");
		builder.append(INDENT + TITLE_FONT_BRIGHTNESS + " setgray" + "\n");
		builder.append(INDENT + "gsave" + "\n");
		builder.append(INDENT + INDENT + "(" + title + ") show " + "( ) show " + "((" + subtitle + ")) show" +"\n");
		builder.append(INDENT + "grestore" + "\n");
		
		builder.append(INDENT + "% Show the rest of the metadata" + "\n");
		builder.append(INDENT + "0 " + (-(TITLE_FONT_SIZE + 5)) + " rmoveto");
		builder.append(INDENT + "/" + FONT_NAME + " findfont " + OTHER_DATA_FONT_SIZE + " scalefont setfont" + "\n");
		builder.append(INDENT + OTHER_DATA_FONT_BRIGHTNESS + " setgray" + "\n");
		for (String metadatum : metadata) {
			builder.append(INDENT + "gsave" + "\n");
			builder.append(INDENT + INDENT + "(" + metadatum + ")" + " show" + "\n");
			builder.append(INDENT + "grestore" + "\n");
			builder.append(INDENT + "0 " + (-(OTHER_DATA_FONT_SIZE + 5)) + " rmoveto" + "\n");
		}
		
		builder.append("grestore" + "\n");
		
		return builder.toString();
	}
}
