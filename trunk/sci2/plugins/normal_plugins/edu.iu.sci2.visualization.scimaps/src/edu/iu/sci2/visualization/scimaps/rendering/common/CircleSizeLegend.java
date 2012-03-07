package edu.iu.sci2.visualization.scimaps.rendering.common;

import java.awt.Color;
import java.text.DecimalFormat;

import oim.vivo.scimapcore.journal.Node;
import edu.iu.sci2.visualization.scimaps.tempvis.GraphicsState;

/**
 * This class represents a circle
 */
public class CircleSizeLegend {
	private static final DecimalFormat formatter = new DecimalFormat("###,###.##");
	private final float scalingFactor;
	
	private float minArea;
	private float maxArea;
	private float midArea;
	private String minLabel;
	private String midLabel;
	private String maxLabel;
	
	public CircleSizeLegend() {
		this(1.0f);
	}
	
	public CircleSizeLegend(float scalingFactor) {
		this.minArea = 5;
		this.maxArea = 50;
		this.midArea = Math.round((this.minArea + this.maxArea) / 2.0);
		this.minLabel = formatter.format(this.minArea);
		this.midLabel = formatter.format(this.midArea);
		this.maxLabel = formatter.format(this.maxArea);
		this.scalingFactor = scalingFactor;
	}

	public float getMinArea() {
		return this.minArea;
	}

	public float getMaxArea() {
		return this.maxArea;
	}

	public float getMidArea() {
		return this.midArea;
	}

	public String getMinLabel() {
		return this.minLabel;
	}

	public String getMidLabel() {
		return this.midLabel;
	}

	public String getMaxLabel() {
		return this.maxLabel;
	}

	public void render(GraphicsState state, float leftBoundary, float topBoundary) {
		state.save();
		state.setFont("Arial", 10);
		state.current.setColor(Color.BLACK);
		state.current.translate(leftBoundary, topBoundary);
		
		float minRadius = Node
				.calculateRadius(getMinArea(), this.scalingFactor);
		float midRadius = Node
				.calculateRadius(getMidArea(), this.scalingFactor);
		float maxRadius = Node
				.calculateRadius(getMaxArea(), this.scalingFactor);

		double circleX = maxRadius;

		float maxCircleY = maxRadius * 2 - maxRadius;
		float midCircleY = maxRadius * 2 - midRadius;
		float minCircleY = maxRadius * 2 - minRadius;

		float labelX = 2 * maxRadius + 5;

		float minLabelY = maxRadius * 2;
		float midLabelY = midCircleY - midRadius + (state.current.getFontMetrics().getHeight() / 2);
		float maxLabelY = 0 + state.current.getFontMetrics().getHeight();
		
		
		// Draw the graphic
		state.drawCircle((int) circleX, (int) minCircleY, (int) minRadius);
		state.current.drawString(getMinLabel(), labelX, minLabelY);
		state.drawCircle((int) circleX, (int) midCircleY, (int) midRadius);
		state.current.drawString(getMidLabel(), labelX, midLabelY);
		state.drawCircle((int) circleX, (int) maxCircleY, (int) maxRadius, 2);
		state.current.drawString(getMaxLabel(), labelX, maxLabelY);

		state.restore();
	}

}
