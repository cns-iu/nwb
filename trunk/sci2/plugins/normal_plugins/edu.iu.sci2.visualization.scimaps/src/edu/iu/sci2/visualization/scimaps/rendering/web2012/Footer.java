package edu.iu.sci2.visualization.scimaps.rendering.web2012;

import java.awt.Color;

import edu.iu.sci2.visualization.scimaps.tempvis.GraphicsState;


public class Footer {
	String footerText;

	public Footer() {
		this.footerText = "NIH's Reporter Web site (projectreporter.nih.gov), NETE & CNS (cns.iu.edu)";
	}

	public void render(GraphicsState state, float leftBoundary, float topBoundary) {
		state.save();

		state.current.translate(leftBoundary, topBoundary);

		Color footerTextColor = Color.gray;
		double footerTextFontSize = 12;
		state.current.setColor(footerTextColor);
		state.setFontSize(footerTextFontSize);
		state.current.drawString(footerText, 0, 0);

		state.restore();
	}
}