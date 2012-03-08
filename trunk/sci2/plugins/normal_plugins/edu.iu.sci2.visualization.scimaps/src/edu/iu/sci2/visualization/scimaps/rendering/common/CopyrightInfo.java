package edu.iu.sci2.visualization.scimaps.rendering.common;

import java.awt.Color;
import java.awt.geom.Rectangle2D;

import edu.iu.sci2.visualization.scimaps.tempvis.GraphicsState;

public class CopyrightInfo {
	private static final String copyrightText = "© 2008  The Regents of the University of California and SciTech Strategies.\nMap updated  by SciTech Strategies, OST, and CNS in 2011.";

	private CopyrightInfo() {

	}

	/**
	 * This will render the copyright text on the given {@code state} using the
	 * {@code topBoundary} and {@code leftBoundary} as bounds.
	 * 
	 * @param state
	 * @param topBoundary
	 * @param pageWidth
	 */
	public static void render(GraphicsState state, float topBoundary,
			float leftBoundary) {
		state.save();

		state.current.translate(leftBoundary, topBoundary);
		state.setItalicFont("Arial", 10);
		state.current.drawString(copyrightText, 0, 0);

		state.restore();

	}

	/**
	 * This will render the copyright text as centered on a point at {@code x},{@code y}.
	 * @param state
	 * @param x
	 * @param y
	 */
	public static void renderAbout(GraphicsState state, float x, float y) {
		state.save();
		state.current.translate(0, y);

		Color copyrightColor = Color.black;
		double copyrightFontSize = 10;
		state.current.setColor(copyrightColor);
		state.setFontSize(copyrightFontSize);
		String[] copyrightLines = copyrightText.split("\n");
		for (String line : copyrightLines) {

			state.save();
			Rectangle2D lineTextBox = state.current.getFontMetrics()
					.getStringBounds(line, state.current);
			float leftBoundTextCentered = (float) (x - lineTextBox.getCenterX());
			state.current.translate(leftBoundTextCentered, 0);
			state.current.drawString(line, 0, 0);
			state.restore();

			state.current.translate(0, copyrightFontSize);
		}
		state.restore();
	}
}