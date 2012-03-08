package edu.iu.sci2.visualization.scimaps.rendering.common;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.geom.Rectangle2D;

import edu.iu.sci2.visualization.scimaps.tempvis.GraphicsState;


public class Footer {
	private static final String footerText = "NIH's Reporter Web site (projectreporter.nih.gov), NETE & CNS (cns.iu.edu)";

	private Footer() {

	}

	/**
	 * Render the footer on the given {@code state} at the {@code leftBoundary} and
	 * {@code} topBoundary}
	 * 
	 * @param state
	 * @param leftBoundary
	 * @param topBoundary
	 */
	public static void render(GraphicsState state, float leftBoundary, float topBoundary) {
		state.save();

		state.current.translate(leftBoundary, topBoundary);
		state.setItalicFont("Arial", 10);
		state.current.drawString(footerText, 0, 0);

		state.restore();
	}
	
	/**
	 * This will render the footer text as centered on a point at {@code x},{@code y}.
	 * @param state
	 * @param x
	 * @param y
	 */
	public static void  renderAbout(GraphicsState state, float x, float y) {
		state.save();

		state.current.setColor(Color.gray);
		state.setItalicFont("Arial", 10);

		// center it on the page
		FontMetrics fontMetrics = state.current.getFontMetrics();
		Rectangle2D footerTextBox = fontMetrics.getStringBounds(footerText, state.current);
		float footerStartX = (float) (x - footerTextBox.getCenterX());
		
		float leftBoundary = footerStartX;

		state.current.translate(leftBoundary, y);
		
		state.current.drawString(footerText, 0, 0);
		
		state.restore();
	}
}