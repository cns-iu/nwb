package edu.iu.sci2.visualization.geomaps.viz.ps;

import java.awt.Font;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

import edu.iu.sci2.visualization.geomaps.viz.PageLayout;

public class PageFooter implements PostScriptable {
	public static final String TEXT =
			"NIH's Reporter Web site (projectreporter.nih.gov), NETE & CNS (cns.iu.edu)";
	public static final Font FONT = PageLayout.CONTENT_FONT.deriveFont(8.0f).deriveFont(Font.ITALIC);
	public static final double FONT_BRIGHTNESS = 0.45;
	
	private final Point2D.Double lowerCenter;

	public PageFooter(Double lowerCenter) {
		this.lowerCenter = lowerCenter;
	}


	@Override
	public String toPostScript() {
		StringBuilder builder = new StringBuilder();

		builder.append("gsave" + "\n");

		builder.append(PSUtility.findscalesetfont(FONT) + "\n");
		builder.append(PSUtility.setgray(FONT_BRIGHTNESS) + "\n");

		builder.append(String.format("%f %f (%s) centerShow\n", lowerCenter.x, lowerCenter.y, TEXT));
		
		builder.append("grestore" + "\n");
		
		return builder.toString();
	}
}
