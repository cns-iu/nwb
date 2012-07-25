package edu.iu.sci2.visualization.scimaps.rendering;

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

	private final String dataColumnName;
	private final int numberOfUnclassified;
	private final double minimumValue;
	private final double maximumValue;
	private final float scalingFactor;
	private final double leftBoundary;
	private final double topBoundary;
	private final int titleFontSize;
	private final int normalFontSize;


	/**
	 * Construct a PageLegend
	 * @param numberOfUnclassified the number of {@link Journal}s that were unmapped on the {@link MapOfScience}.
	 */
	public PageLegend(String dataColumnName, int numberOfUnclassified, double minimumValue,
			double maximumValue, float scalingFactor, double leftBoundary, double topBoundary,
			int titleFontSize, int normalFontSize) {
		this.dataColumnName = dataColumnName;
		this.numberOfUnclassified = numberOfUnclassified;
		this.minimumValue = minimumValue;
		this.maximumValue = maximumValue;
		this.scalingFactor = scalingFactor;
		this.leftBoundary = leftBoundary;
		this.topBoundary = topBoundary;
		this.titleFontSize = titleFontSize;
		this.normalFontSize = normalFontSize;
	}


	@Override
	public void render(GraphicsState state) {
		state.save();
		state.current.translate(this.leftBoundary, this.topBoundary);

		String title = "Legend";
		String area = String.format("Circle area: %s", dataColumnName);
		String unclassified = "Unclassified = " + this.numberOfUnclassified;
		String minimum = "Minimum = " + formatter.format(this.minimumValue);
		String maximum = "Maximum = " + formatter.format(this.maximumValue);
		String scalingFactor = "Scaling factor = " + String.valueOf(this.scalingFactor);
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
		state.drawStringAndTranslate(scalingFactor, 0, 0);

		for (String colorString : color.split(System.getProperty("line.separator"))) {
			state.drawStringAndTranslate(colorString, 0, 0);
		}

		state.restore();
	}
}
