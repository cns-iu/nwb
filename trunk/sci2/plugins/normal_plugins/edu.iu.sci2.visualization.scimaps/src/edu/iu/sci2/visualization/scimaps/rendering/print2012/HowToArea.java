package edu.iu.sci2.visualization.scimaps.rendering.print2012;

import java.awt.Color;

import edu.iu.sci2.visualization.scimaps.tempvis.GraphicsState;

public class HowToArea {
	String title;
	String body;

	public HowToArea() {
		this.title = "How To Read This Map";
		this.body = "At vero eos et accusamus et iusto odio dignissimos ducimus qui\nblanditiis praesentium voluptatum deleniti atque corrupti quos\ndolores et quas molestias excepturi sint occaecati cupiditate non\nprovident, similique sunt in culpa qui officia deserunt mollitia\nanimi, id est laborum et dolorum fuga. Et harum quidem rerum\nfacilis est et expedita distinctio.";
	}

	public void render(GraphicsState state, float leftBoundary,
			float topBoundary) {
		state.save();

		state.current.translate(leftBoundary, topBoundary);

		Color titleColor = Color.black;
		double titleFontSize = 14;
		state.current.setColor(titleColor);
		state.setFontSize(titleFontSize);
		state.current.drawString(title, 0, 0);
		state.current.translate(0, titleFontSize);

		Color bodyColor = Color.gray;
		double bodyFontSize = 12;
		state.current.setColor(bodyColor);
		state.setFontSize(bodyFontSize);
		String[] bodyLines = body.split("\n");
		for (String line : bodyLines) {
			state.current.drawString(line, 0, 0);
			state.current.translate(0, bodyFontSize);
		}

		state.restore();
	}
}