package edu.iu.sci2.visualization.scimaps.rendering.common;

import java.awt.Color;

import edu.iu.sci2.visualization.scimaps.tempvis.GraphicsState;

public class HowToArea {
	public static final String newline = System.getProperty("line.separator");
	public static final String title = "How To Read This Map";
	private static final String body = "The Map of Science is a visual representation of 554 subdisciplines within 13"
			+ newline
			+ "disciplines of science and their relationships to one another, shown as points "
			+ newline
			+ "and lines connecting those points respectively.  Over top this visualization is "
			+ newline
			+ "drawn the result of mapping a dataset's journals to the underlying "
			+ newline
			+ "subdiscipline(s) those journals contain. Mapped subdisciplines are shown with "
			+ newline
			+ "size relative to the number matching journals and color from the discipline.  For "
			+ newline
			+ "more information on maps of science, see http://mapofscience.com";

	private HowToArea() {

	}

	/**
	 * Render the howto text on the given {@code state} at the {@code leftBoundary} and
	 * {@code} topBoundary}
	 * 
	 * @param state
	 * @param leftBoundary
	 * @param topBoundary
	 */
	public static void render(GraphicsState state, float leftBoundary,
			float topBoundary) {
		state.save();

		state.current.translate(leftBoundary, topBoundary);
		state.current.setColor(Color.BLACK);

		state.setBoldFont("Arial", 14);
		state.drawStringAndTranslate(title, 0, 0);

		state.setFont("Arial", 10);
		String[] bodyLines = body.split(newline);
		for (String line : bodyLines) {
			state.drawStringAndTranslate(line, 0, 0);
		}

		state.restore();
	}
}