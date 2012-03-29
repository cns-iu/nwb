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
	private static final DecimalFormat formatter = new DecimalFormat(
			"###,###.##");
	private final float scalingFactor;

	private float minArea;
	private float maxArea;
	private float midArea;
	private String minLabel;
	private String midLabel;
	private String maxLabel;
	private double leftBoundary;
	private double topBoundary;
	private int fontSize;

	/**
	 * If you scaled your {@link Node}s, you can use this constructor which will
	 * take that into account numerically, though not graphically.
	 * 
	 * @param scalingFactor
	 *            - The nodes were scaled by in the Map of Science
	 */
	public CircleSizeLegend(float scalingFactor, double leftBoundary,
			double topBoundary, int fontSize, float minArea, float maxArea) {
		this.scalingFactor = scalingFactor;
		this.leftBoundary = leftBoundary;
		this.topBoundary = topBoundary;
		this.fontSize = fontSize;

		this.minArea = minArea;
		this.maxArea = maxArea;
		this.midArea = Math.round((this.minArea + this.maxArea) / 2.0);

		// HACK I could calculate the radius using nodes.calculateRadius but the
		// radius given isn't really the radius so I couldn't get the area back.
		this.minLabel = formatter.format(this.minArea * this.scalingFactor);
		this.midLabel = formatter.format(this.midArea * this.scalingFactor);
		this.maxLabel = formatter.format(this.maxArea * this.scalingFactor);
	}

	public void render(GraphicsState state) {
		state.save();
		state.setFont("Arial", this.fontSize);
		state.current.setColor(Color.BLACK);
		state.current.translate(this.leftBoundary, this.topBoundary);

		float minRadius = Node.calculateRadius(this.minArea, 1.0f);
		float midRadius = Node.calculateRadius(this.midArea, 1.0f);
		float maxRadius = Node.calculateRadius(this.maxArea, 1.0f);

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
