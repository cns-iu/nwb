package edu.iu.scipolicy.visualization.geomaps.printing;

import org.antlr.stringtemplate.StringTemplate;

import edu.iu.scipolicy.visualization.geomaps.GeoMapsAlgorithm;
import edu.iu.scipolicy.visualization.geomaps.utility.Constants;

public class PageFooter {	
	public static final String FOOTER_STRING_PREFIX =
		"Created with the ";
	
	public static final String CNS_URL = "http://cns.slis.indiana.edu";
	public static final String FOOTER_STRING_SUFFIX =
		"Cyberinfrastructure for Network Science Center "
		+ "(" + CNS_URL + ")";	
	
	public static final String FONT_NAME = Constants.FONT_NAME;
	public static final double FONT_SIZE = 6;
	public static final double FONT_BRIGHTNESS = 0.35;
	
	public static final double LOWER_LEFT_X_IN_POINTS =
		0.35 * Constants.POINTS_PER_INCH;
	public static final double LOWER_LEFT_Y_IN_POINTS =
		0.35 * Constants.POINTS_PER_INCH;

	private static final String SEP = " | ";
	
	private boolean hasPrintedDefinitions = false;
	
	public String toPostScript() {
		String s = "";
		
		if (!hasPrintedDefinitions) {
			StringTemplate definitionsTemplate =
				GeoMapsAlgorithm.group.getInstanceOf("showToolNameDefinitions");
			
			s += definitionsTemplate.toString();
			
			this.hasPrintedDefinitions = true;
		}
		
		s += "gsave" + "\n";
		s += "/" + FONT_NAME + " findfont" + "\n";
		s += FONT_SIZE + " scalefont" + "\n";
		s += "setfont" + "\n";
		s += FONT_BRIGHTNESS + " setgray" + "\n";
		s += LOWER_LEFT_X_IN_POINTS + " " + LOWER_LEFT_Y_IN_POINTS + " moveto" + "\n";
		s += "(" + FOOTER_STRING_PREFIX + ") show" + "\n";
		
		s += "currentpoint" + " ";
		s += "{/" + FONT_NAME + " findfont}" + " ";
		s += FONT_BRIGHTNESS + " ";
		s += FONT_SIZE + " ";
		s += "showToolName moveto" + "\n";
		
		s += "(" + SEP + FOOTER_STRING_SUFFIX + ") show" + "\n";
		s += "grestore" + "\n";
		
		return s;
	}
}
