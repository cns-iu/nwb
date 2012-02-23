package edu.iu.sci2.visualization.geomaps.viz.ps;

import org.antlr.stringtemplate.StringTemplate;

import edu.iu.sci2.visualization.geomaps.GeoMapsAlgorithm;
import edu.iu.sci2.visualization.geomaps.viz.Constants;

public class PageFooter {
	public static final String INDENT = "    ";
	public static final String ATTRIBUTION_PREFIX =	"Created with ";
	
	public static final String CNS_URL = "http://cns.iu.edu";
	public static final String CNS =
		"Cyberinfrastructure for Network Science Center";
	
	public static final String FONT_NAME = Constants.FONT_NAME;
	public static final double FONT_SIZE = 6;
	public static final double FONT_BRIGHTNESS = 0.35;
	
	public static final double LOWER_LEFT_X_IN_POINTS =
		Constants.PAGE_MARGIN_SIZE_IN_POINTS;
	public static final double LOWER_LEFT_Y_IN_POINTS =
		Constants.PAGE_MARGIN_SIZE_IN_POINTS;

	private static final String SEP = " | ";
	
	private boolean hasPrintedDefinitions = false;
	
	public String toPostScript() {
		StringBuilder builder = new StringBuilder();
		
		if (!hasPrintedDefinitions) {
			StringTemplate definitionsTemplate =
				GeoMapsAlgorithm.TEMPLATE_GROUP.getInstanceOf("showToolNameDefinitions");
			
			builder.append(definitionsTemplate.toString());
			
			this.hasPrintedDefinitions = true;
		}
		
		builder.append("gsave" + "\n");
		builder.append(INDENT + "/" + FONT_NAME + " findfont" + "\n");
		builder.append(INDENT + FONT_SIZE + " scalefont" + "\n");
		builder.append(INDENT + "setfont" + "\n");
		builder.append(INDENT + FONT_BRIGHTNESS + " setgray" + "\n");
		builder.append(INDENT + LOWER_LEFT_X_IN_POINTS + " " + LOWER_LEFT_Y_IN_POINTS + " moveto" + "\n");
		
		builder.append(INDENT + "(" + ATTRIBUTION_PREFIX + ") show" + "\n");
		
		builder.append(INDENT + "currentpoint" + " ");
		builder.append("{/" + FONT_NAME + " findfont}" + " ");
		builder.append(FONT_BRIGHTNESS + " ");
		builder.append(FONT_SIZE + " ");
		builder.append("showToolName moveto" + "\n");
		
		builder.append(INDENT + "(" + SEP + CNS + " " + ")" + " show" + "\n");
		builder.append(INDENT + "(" + "\\(" + ")" + " show" + "\n");
		builder.append(INDENT + "(" + CNS_URL + ")" + " underlinedShow" + "\n");
		builder.append(INDENT + "(" + "\\)" + ")" + " show" + "\n");
		builder.append("grestore" + "\n");
		
		return builder.toString();
	}
}
