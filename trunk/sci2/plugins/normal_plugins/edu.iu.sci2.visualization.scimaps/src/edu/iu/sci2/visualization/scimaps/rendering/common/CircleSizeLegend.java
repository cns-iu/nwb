package edu.iu.sci2.visualization.scimaps.rendering.common;

import java.awt.Color;
import java.text.DecimalFormat;

import oim.vivo.scimapcore.journal.Node;
import edu.iu.sci2.visualization.scimaps.tempvis.GraphicsState;
import edu.iu.sci2.visualization.scimaps.tempvis.PageElement;

/**
 * This class represents a circle legend.
 */
public class CircleSizeLegend implements PageElement {
	private static final DecimalFormat FORMATTER = new DecimalFormat("###,###.##");

	private float minArea;
	private float maxArea;
	private float midArea;
	private double leftBoundary;
	private double topBoundary;
	private int fontSize;
	private int titleFontSize;
	private float pageScalingFactor;
	private float scalingFactor;

	/**
	 * If you scaled your {@link Node}s, you can use this constructor which will
	 * take that into account numerically, though not graphically.
	 * 
	 * @param scalingFactor
	 *            The nodes were scaled by in the Map of Science
	 * @param pageScalingFactor
	 *            The scaling factor for the page
	 */
	public CircleSizeLegend(float scalingFactor, float pageScalingFactor,
			double leftBoundary, double topBoundary, int fontSize,
			int titleFontSize, float minArea, float maxArea) {
		this.leftBoundary = leftBoundary;
		this.topBoundary = topBoundary;
		this.fontSize = fontSize;
		this.titleFontSize = titleFontSize;
		
		this.pageScalingFactor = pageScalingFactor;
		this.scalingFactor = scalingFactor;

		this.minArea = minArea;
		this.maxArea = maxArea;
		this.midArea = Math.round((this.minArea + this.maxArea) / 2.0);
	}

	@Override
	public void render(GraphicsState state) {
		// Draw the word 'Area'
		String title = "Area";
		state.save();
		state.current.translate(this.leftBoundary, this.topBoundary);
		double titleWidth = state.current.getFontMetrics()
				.getStringBounds(title, state.current).getBounds().getWidth();
		state.setBoldFont("Arial", this.titleFontSize);
		state.current.drawString(title, (int) (-titleWidth), 0);
		state.restore();

		// Draw the legend
		state.save();
		state.setFont("Arial", this.fontSize);
		state.current.setColor(Color.BLACK);
		state.current.translate(this.leftBoundary, this.topBoundary);

		float minRadius = Node.calculateRadius(this.minArea, 1.0f);
		float midRadius = Node.calculateRadius(this.midArea, 1.0f);
		float maxRadius = Node.calculateRadius(this.maxArea, 1.0f);

		String minLabel = FORMATTER.format(Node.calculateWeight(minRadius / this.pageScalingFactor, this.scalingFactor));
		String midLabel = FORMATTER.format(Node.calculateWeight(midRadius / this.pageScalingFactor, this.scalingFactor));
		String maxLabel = FORMATTER.format(Node.calculateWeight(maxRadius / this.pageScalingFactor, this.scalingFactor));

		double circleX = maxRadius;

		// The circles should be stacked at the bottom
		float maxCircleY = maxRadius * 2 - maxRadius;
		float midCircleY = maxRadius * 2 - midRadius;
		float minCircleY = maxRadius * 2 - minRadius;

		float labelX = 2 * maxRadius + 5;

		// The min label should be above the bottom line, the mid label split by
		// the top of the mid circle, and the top label should be just under the
		// top line
		float minLabelY = maxRadius * 2;
		float midLabelY = (float) (
				midCircleY
				- midRadius
				+ (state.current.getFontMetrics().getHeight() / 2.0));
		float maxLabelY = 0 + state.current.getFontMetrics().getHeight();

		state.drawCircle((int) circleX, (int) minCircleY, (int) minRadius);
		state.current.drawString(minLabel, labelX, minLabelY);
		state.drawCircle((int) circleX, (int) midCircleY, (int) midRadius);
		state.current.drawString(midLabel, labelX, midLabelY);
		state.drawCircle((int) circleX, (int) maxCircleY, (int) maxRadius);
		state.current.drawString(maxLabel, labelX, maxLabelY);

		state.restore();

	}

}
