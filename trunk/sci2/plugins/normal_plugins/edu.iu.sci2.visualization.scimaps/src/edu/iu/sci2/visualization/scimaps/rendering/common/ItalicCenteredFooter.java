package edu.iu.sci2.visualization.scimaps.rendering.common;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.geom.Rectangle2D;

import edu.iu.sci2.visualization.scimaps.tempvis.GraphicsState;
import edu.iu.sci2.visualization.scimaps.tempvis.PageElement;

public class ItalicCenteredFooter implements PageElement {
	private final String footerText;
	private double topY;
	private double centerX;

	public ItalicCenteredFooter(double centerX, double topY) {
		this.centerX = centerX;
		this.topY = topY;
		this.footerText = "CNS (cns.iu.edu)";
	}

	public void render(GraphicsState state) {
		state.save();

		state.current.setColor(Color.gray);
		state.setItalicFont("Arial", 10);

		FontMetrics fontMetrics = state.current.getFontMetrics();
		Rectangle2D footerTextBox = fontMetrics.getStringBounds(this.footerText,
				state.current);
		double footerStartX = this.centerX - footerTextBox.getCenterX();

		double leftBoundary = footerStartX;

		state.current.translate(leftBoundary, this.topY);

		state.current.drawString(this.footerText, 0, 0);

		state.restore();
	}
}