package edu.iu.sci2.visualization.scimaps.rendering.common;

import java.text.DecimalFormat;

import edu.iu.sci2.visualization.scimaps.tempvis.GraphicsState;

public class PageLegend {
	public static final DecimalFormat formatter = new DecimalFormat("###,###");

	private int numberOfUnclassified;
	private double minimumValue;
	private double maximumValue;
	
	public PageLegend(int numberOfUnclassified, double minimumValue, double maximumValue) {
		this.numberOfUnclassified = numberOfUnclassified;
		this.minimumValue = minimumValue;
		this.maximumValue = maximumValue;
	}
	
	public void render(GraphicsState state, float leftBound, float topBound) {
		state.save();
		state.current.translate(leftBound, topBound);
		
		String title = "Legend";
		String area = "Circle Area: Fractional Journal Count";
		String unclassified = "Unclassified = " + this.numberOfUnclassified;
		String minimum = "Minimum = " + formatter.format(this.minimumValue);
		String maximum = "Maximum = " + formatter.format(this.maximumValue);
		String color = "Color: Discipline" + System.getProperty("line.separator") + "See end of PDF for color legend.";
		
		state.setBoldFont("Arial", 14);
		state.drawStringAndTranslate(title, 0, 0);
		
		state.setFont("Arial", 10);
		state.drawStringAndTranslate(area, 0, 0);
		state.drawStringAndTranslate(title, 0, 0);
		state.drawStringAndTranslate(unclassified, 0, 0);
		state.drawStringAndTranslate(minimum, 0, 0);
		state.drawStringAndTranslate(maximum, 0, 0);
		state.current.translate(0, state.current.getFontMetrics().getHeight());
		
		for (String colorString : color.split(System.getProperty("line.separator"))){
			state.drawStringAndTranslate(colorString, 0, 0);
		}
		
		state.restore();
	}
}
