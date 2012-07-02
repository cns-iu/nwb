package edu.iu.sci2.visualization.scimaps.rendering.common;

import java.awt.geom.Rectangle2D;

import edu.iu.sci2.visualization.scimaps.tempvis.GraphicsState;
import edu.iu.sci2.visualization.scimaps.tempvis.PageElement;

public class CenteredCopyrightInfo implements PageElement{
	private static final String copyrightText = "© 2008  The Regents of the University of California and SciTech Strategies.\nMap updated  by SciTech Strategies, OST, and CNS in 2011.";
	private double centerX;
	private double centerY;
	private int fontSize;

	public CenteredCopyrightInfo(double centerX, double centerY, int fontSize) {
		this.centerX = centerX;
		this.centerY = centerY;
		this.fontSize = fontSize;
	}

	@Override
	public void render(GraphicsState state) {
		state.save();
		state.current.translate(0, this.centerY);

		state.setFontSize(this.fontSize);
		String[] copyrightLines = copyrightText.split("\n");
		for (String line : copyrightLines) {

			state.save();
			Rectangle2D lineTextBox = state.current.getFontMetrics()
					.getStringBounds(line, state.current);
			float leftBoundTextCentered = (float) (this.centerX - lineTextBox.getCenterX());
			state.current.translate(leftBoundTextCentered, 0);
			state.current.drawString(line, 0, 0);
			state.restore();
			state.current.translate(0, this.fontSize);
		}
		state.restore();
		
	}
}