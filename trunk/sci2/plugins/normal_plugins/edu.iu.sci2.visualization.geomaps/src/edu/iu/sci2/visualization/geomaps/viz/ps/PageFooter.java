package edu.iu.sci2.visualization.geomaps.viz.ps;

import java.awt.Font;
import java.awt.geom.Point2D;

import edu.iu.sci2.visualization.geomaps.viz.PageLayout;

public class PageFooter implements PostScriptable {
	public static final String TEXT = "CNS (cns.iu.edu)";
	public static final double FONT_BRIGHTNESS = 0.45;
	
	private final Point2D.Double lowerCenter;
	private final PageLayout pageLayout;

	public PageFooter(Point2D.Double lowerCenter, PageLayout pageLayout) {
		this.lowerCenter = lowerCenter;
		this.pageLayout = pageLayout;
	}


	@Override
	public String toPostScript() {
		StringBuilder builder = new StringBuilder();

		builder.append("gsave" + "\n");

		Font font = pageLayout.contentFont().deriveFont(Font.ITALIC).deriveFont(10.0f);
		
		builder.append(PSUtility.findscalesetfont(font) + "\n");
		builder.append(PSUtility.setgray(FONT_BRIGHTNESS) + "\n");

		builder.append(String.format("%f %f (%s) centerShow\n", lowerCenter.x, lowerCenter.y, TEXT));
		
		builder.append("grestore" + "\n");
		
		return builder.toString();
	}
}
