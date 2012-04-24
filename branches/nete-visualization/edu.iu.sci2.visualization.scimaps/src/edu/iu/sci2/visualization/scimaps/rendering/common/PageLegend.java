package edu.iu.sci2.visualization.scimaps.rendering.common;

import java.text.DecimalFormat;

import oim.vivo.scimapcore.journal.Journal;

import edu.iu.sci2.visualization.scimaps.MapOfScience;
import edu.iu.sci2.visualization.scimaps.tempvis.GraphicsState;
import edu.iu.sci2.visualization.scimaps.tempvis.PageElement;

/**
 * A PageLegend is the text on the bottom of the Map of Science that represents
 * the legend.
 * 
 */
public class PageLegend implements PageElement{
	private static final DecimalFormat formatter = new DecimalFormat("###,###");

	private int numberOfUnclassified;
	private double minimumValue;
	private double maximumValue;
	private double leftBoundary;
	private double topBoundary;

	private int titleFontSize;

	private int normalFontSize;

	/**
	 * Construct a PageLegend
	 * @param numberOfUnclassified the number of {@link Journal}s that were unmapped on the {@link MapOfScience}.
	 * @param minimumValue
	 * @param maximumValue
	 */
	public PageLegend(int numberOfUnclassified, double minimumValue,
			double maximumValue, double leftBoundary, double topBoundary, int titleFontSize, int normalFontSize) {
		this.numberOfUnclassified = numberOfUnclassified;
		this.minimumValue = minimumValue;
		this.maximumValue = maximumValue;
		this.leftBoundary = leftBoundary;
		this.topBoundary = topBoundary;
		this.titleFontSize = titleFontSize;
		this.normalFontSize = normalFontSize;
	}


	public void render(GraphicsState state) {
		state.save();
		state.current.translate(this.leftBoundary, this.topBoundary);

		String title = "Legend";
		String area = "Circle area: Fractional Journal Count";
		String unclassified = "Unclassified = " + this.numberOfUnclassified;
		String minimum = "Minimum = " + formatter.format(this.minimumValue);
		String maximum = "Maximum = " + formatter.format(this.maximumValue);
		String color = "Color: Discipline"
				+ System.getProperty("line.separator")
				+ "See end of PDF for color legend.";

		state.setBoldFont("Arial", this.titleFontSize);
		state.drawStringAndTranslate(title, 0, 0);

		state.setFont("Arial", this.normalFontSize);
		state.drawStringAndTranslate(area, 0, 0);
		state.drawStringAndTranslate(unclassified, 0, 0);
		state.drawStringAndTranslate(minimum, 0, 0);
		state.drawStringAndTranslate(maximum, 0, 0);

		for (String colorString : color.split(System
				.getProperty("line.separator"))) {
			state.drawStringAndTranslate(colorString, 0, 0);
		}

		state.restore();
	}
}
