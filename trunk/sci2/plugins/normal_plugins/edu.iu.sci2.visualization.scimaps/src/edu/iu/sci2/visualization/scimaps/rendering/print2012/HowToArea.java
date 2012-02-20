package edu.iu.sci2.visualization.scimaps.rendering.print2012;

import java.awt.Color;

import edu.iu.sci2.visualization.scimaps.tempvis.GraphicsState;

public class HowToArea {
	String title;
	String body;
	public static final String newline = System.getProperty("line.separator");

	public HowToArea() {
		this.title = "How To Read This Map";
		this.body = "The Map of Science is a visual representation of 554 sub-disciplines within 13 disciplines"
				+ newline
				+ "of science and their relationships to one another, shown as points and lines connecting"
				+ newline
				+ "those points respectively.  Over top this visualization is drawn the result of matching a"
				+ newline
				+ "dataset to the underlying sub-disciplines it contains. Each matching sub-discipline is"
				+ newline
				+ "shown as a circle whose color corresponds to the superior discipline and whose relative"
				+ newline
				+ "size is derived from the overall representation of this sub-discipline within the dataset.";
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

		Color bodyColor = Color.gray;
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