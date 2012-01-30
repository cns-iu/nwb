package edu.iu.sci2.visualization.scimaps.rendering.print2012;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.geom.Rectangle2D;

import edu.iu.sci2.visualization.scimaps.tempvis.GraphicsState;

public class Footer {
	String footerText;
	float pageWidth;
	float topBoundary;
	public Footer(float pageWidth, float topBoundary){
		this.footerText = "NIH's Reporter Web site (projectreporter.nih.gov), NETE & CNS (cns.iu.edu)";
		this.pageWidth = pageWidth;
		this.topBoundary = topBoundary;
	}
	
	public void render(GraphicsState state){
		state.save();

		Color footerTextColor = Color.gray;
		double footerTextFontSize = 12;
		state.current.setColor(footerTextColor);
		state.setFontSize(footerTextFontSize);

		// center it on the page
		FontMetrics fontMetrics = state.current.getFontMetrics();
		Rectangle2D footerTextBox = fontMetrics.getStringBounds(footerText, state.current);
		float footerStartX = (float) ((pageWidth / 2) - footerTextBox.getCenterX());
		
		float leftBoundary = footerStartX;

		state.current.translate(leftBoundary, topBoundary);
		
		state.current.drawString(footerText, 0, 0);
		
		state.restore();
	}
}
