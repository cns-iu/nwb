package edu.iu.sci2.visualization.geomaps.viz.ps;

import java.awt.Font;
import java.awt.geom.Point2D;

import org.antlr.stringtemplate.StringTemplate;

import edu.iu.sci2.visualization.geomaps.GeoMapsAlgorithm;
import edu.iu.sci2.visualization.geomaps.viz.PageLayout;

public class PageFooter implements PostScriptable {
	public static final String INDENT = "    ";
	public static final String ATTRIBUTION_PREFIX =	"Created with ";
	
	public static final String CNS_URL = "http://cns.iu.edu";
	public static final String CNS =
		"Cyberinfrastructure for Network Science Center";
	
	public static final Font FONT = GeoMapViewPS.CONTENT_FONT.deriveFont(6.0f);
	public static final double FONT_BRIGHTNESS = 0.35;
	
	public static final Point2D.Double LOWER_LEFT =
			new Point2D.Double(PageLayout.pageMargin(), PageLayout.pageMargin());

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

		builder.append(PSUtility.findscalesetfont(FONT) + "\n");
		builder.append(PSUtility.setgray(FONT_BRIGHTNESS) + "\n");
		builder.append(INDENT + LOWER_LEFT.x + " " + LOWER_LEFT.y + " moveto" + "\n");
		
		builder.append(INDENT + "(" + ATTRIBUTION_PREFIX + ") show" + "\n");
		
		builder.append(INDENT + "currentpoint" + " ");
		builder.append("{/" + FONT.getName() + " findfont}" + " ");
		builder.append(FONT_BRIGHTNESS + " ");
		builder.append(FONT.getSize() + " ");
		builder.append("showToolName moveto" + "\n");
		
		builder.append(INDENT + "(" + SEP + CNS + " " + ")" + " show" + "\n");
		builder.append(INDENT + "(" + "\\(" + ")" + " show" + "\n");
		builder.append(INDENT + "(" + CNS_URL + ")" + " underlinedShow" + "\n");
		builder.append(INDENT + "(" + "\\)" + ")" + " show" + "\n");
		builder.append("grestore" + "\n");
		
		return builder.toString();
	}
}
