package edu.iu.sci2.visualization.scimaps.rendering.web2012;

import java.awt.Color;

import edu.iu.sci2.visualization.scimaps.tempvis.GraphicsState;

public class HowToArea {
	String title;
	String body;
	public static final String newline = System.getProperty("line.separator");

	public HowToArea() {
		this.title = "How To Read This Map";
		this.body = "The Map of Science is a visual representation of 554 subdisciplines within 13"
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
	}

	public void render(GraphicsState state, float leftBoundary,
			float topBoundary) {
		state.save();

		state.current.translate(leftBoundary, topBoundary);

		Color titleColor = Color.black;
		double titleFontSize = 14;
		state.current.setColor(titleColor);
		state.setFontSize(titleFontSize);
		state.current.drawString(this.title, 0, 0);
		state.current.translate(0, titleFontSize);

		Color bodyColor = Color.black;
		double bodyFontSize = 12;
		state.current.setColor(bodyColor);
		state.setFontSize(bodyFontSize);
		String[] bodyLines = this.body.split(newline);
		for (String line : bodyLines) {
			state.current.drawString(line, 0, 0);
			state.current.translate(0, bodyFontSize);
		}

		state.restore();
	}
}