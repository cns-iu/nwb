package edu.iu.sci2.visualization.geomaps.viz.ps;

import java.awt.Font;
import java.awt.geom.Point2D;

import edu.iu.sci2.visualization.geomaps.viz.PageLayout;

public class PageFooter implements PostScriptable {
	public static final String TEXT =
			"NIH's Reporter Web site (projectreporter.nih.gov), NETE & CNS (cns.iu.edu)";
	public static final Font FONT = GeoMapViewPS.CONTENT_FONT.deriveFont(8.0f).deriveFont(Font.ITALIC);
	public static final double FONT_BRIGHTNESS = 0.45;
	
	public static final Point2D.Double LOWER_CENTER =
			new Point2D.Double(
					PageLayout.pageWidth() / 2.0,
					PageLayout.pageMargin() - FONT.getSize());

	@Override
	public String toPostScript() {
		StringBuilder builder = new StringBuilder();

		builder.append("gsave" + "\n");

		builder.append(PSUtility.findscalesetfont(FONT) + "\n");
		builder.append(PSUtility.setgray(FONT_BRIGHTNESS) + "\n");

		builder.append(String.format("%f %f (%s) centerShow\n", LOWER_CENTER.x, LOWER_CENTER.y, TEXT));
		
		builder.append("grestore" + "\n");
		
		return builder.toString();
	}
}
