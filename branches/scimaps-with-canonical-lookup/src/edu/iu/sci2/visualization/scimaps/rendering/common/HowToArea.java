package edu.iu.sci2.visualization.scimaps.rendering.common;

import java.awt.Color;
import java.awt.Graphics2D;

import edu.iu.sci2.visualization.scimaps.tempvis.GraphicsState;
import edu.iu.sci2.visualization.scimaps.tempvis.PageElement;

public class HowToArea implements PageElement {
	private static final String title = "How To Read This Map";
	private static final String body1_beginning = "The ";
	private static final String body1_italics = "UCSD map of science";
	private static final String body1_end = " depicts a network of 554 subdiscipline nodes that ";
	private static final String body2 = "are aggregated to 13 main disciplines of science. Each discipline has a distinct ";
	private static final String body3 = "color and is labeled. Overlaid are circles, each representing all records per ";
	private static final String body4 = "unique subdiscipline. Circle area is proportional to the number of fractionally ";
	private static final String body5 = "assigned records. Minimum and maximum data values are given in the legend.";
	private double leftBoundary;
	private double topBoundary;

	public HowToArea(double leftBoundary, double topBoundary) {
		this.leftBoundary = leftBoundary;
		this.topBoundary = topBoundary;
	}

	@Override
	public void render(GraphicsState state) {
		state.save();

		state.current.translate(this.leftBoundary, this.topBoundary);
		state.current.setColor(Color.BLACK);

		state.setBoldFont("Arial", 14);
		state.drawStringAndTranslate(title, 0, 0);

		state.setFont("Arial", 10);

		state.current.drawString(HowToArea.body1_beginning, 0, 0);
		state.setItalicFont("Arial", 10);
		state.current.drawString(HowToArea.body1_italics, width(body1_beginning, state.current), 0);
		state.setFont("Arial", 10);
		state.current.drawString(HowToArea.body1_end, width(body1_beginning, state.current) + width(body1_italics, state.current), 0);
		state.current.translate(0, state.current.getFontMetrics().getHeight());

		state.drawStringAndTranslate(HowToArea.body2, 0, 0);
		state.drawStringAndTranslate(HowToArea.body3, 0, 0);
		state.drawStringAndTranslate(HowToArea.body4, 0, 0);
		state.drawStringAndTranslate(HowToArea.body5, 0, 0);

		state.restore();

	}
	
	private static int width(String string, Graphics2D graphics) {
		return (int) graphics.getFontMetrics().getStringBounds(string, graphics).getWidth();		
	}
}