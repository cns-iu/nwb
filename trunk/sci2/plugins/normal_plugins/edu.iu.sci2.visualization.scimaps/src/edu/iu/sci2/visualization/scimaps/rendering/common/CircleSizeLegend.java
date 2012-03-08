package edu.iu.sci2.visualization.scimaps.rendering.common;

import java.awt.Color;
import java.text.DecimalFormat;

import oim.vivo.scimapcore.journal.Node;
import edu.iu.sci2.visualization.scimaps.tempvis.GraphicsState;

/**
 * This class represents a circle legend.
 */
public class CircleSizeLegend {
	private static final DecimalFormat formatter = new DecimalFormat(
			"###,###.##");
	private final float scalingFactor;

	private float minArea;
	private float maxArea;
	private float midArea;
	private String minLabel;
	private String midLabel;
	private String maxLabel;

	/**
	 * Construct a default CircleSizeLegend
	 */
	public CircleSizeLegend() {
		this(1.0f);
	}

	/**
	 * If you scaled your {@link Node}s, you can use this constructor which will take
	 * that into account numerically, though not graphically.
	 * 
	 * @param scalingFactor
	 *            - The nodes were scaled by in the Map of Science
	 */
	public CircleSizeLegend(float scalingFactor) {
		this.minArea = 5;
		this.maxArea = 50;
		this.midArea = Math.round((this.minArea + this.maxArea) / 2.0);
		this.minLabel = formatter.format(this.minArea);
		this.midLabel = formatter.format(this.midArea);
		this.maxLabel = formatter.format(this.maxArea);
		this.scalingFactor = scalingFactor;
	}

	/**
	 * Render the legend on the given {@code state} at the {@code leftBoundary} and
	 * {@code} topBoundary}
	 * 
	 * @param state
	 * @param leftBoundary
	 * @param topBoundary
	 */
	public void render(GraphicsState state, float leftBoundary,
			float topBoundary) {
		state.save();
		state.setFont("Arial", 10);
		state.current.setColor(Color.BLACK);
		state.current.translate(leftBoundary, topBoundary);

		float minRadius = Node
				.calculateRadius(this.minArea, this.scalingFactor);
		float midRadius = Node
				.calculateRadius(this.midArea, this.scalingFactor);
		float maxRadius = Node
				.calculateRadius(this.maxArea, this.scalingFactor);

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
		float midLabelY = midCircleY - midRadius
				+ (state.current.getFontMetrics().getHeight() / 2);
		float maxLabelY = 0 + state.current.getFontMetrics().getHeight();

		state.drawCircle((int) circleX, (int) minCircleY, (int) minRadius);
		state.current.drawString(this.minLabel, labelX, minLabelY);
		state.drawCircle((int) circleX, (int) midCircleY, (int) midRadius);
		state.current.drawString(this.midLabel, labelX, midLabelY);
		state.drawCircle((int) circleX, (int) maxCircleY, (int) maxRadius, 2);
		state.current.drawString(this.maxLabel, labelX, maxLabelY);

		state.restore();
	}

}
