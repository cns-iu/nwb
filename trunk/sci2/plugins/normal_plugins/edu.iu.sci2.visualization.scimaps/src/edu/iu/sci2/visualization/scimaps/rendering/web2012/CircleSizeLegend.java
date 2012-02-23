package edu.iu.sci2.visualization.scimaps.rendering.web2012;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.Collections;

import oim.vivo.scimapcore.journal.Node;

import org.cishell.utilities.NumberUtilities;

import edu.iu.sci2.visualization.scimaps.rendering.scimaps.NodeRenderer;
import edu.iu.sci2.visualization.scimaps.tempvis.GraphicsState;

/**
 * This class represents a circle
 */
public class CircleSizeLegend {
	/*
	 * Brightnesses correspond to PostScript's setgray command. 0 is black, 1 is
	 * white.
	 */
	public static final float CIRCLE_BRIGHTNESS = 0.3f;
	public static final float EXTREMA_LABEL_BRIGHTNESS = 0.3f;
	public static final float EXTREMA_LABEL_FONT_SIZE = 12f;
	public static final float TYPE_LABEL_BRIGHTNESS = 0.0f;
	public static final float TYPE_LABEL_FONT_SIZE = 12f;
	public static final float SCALING_LABEL_BRIGHTNESS = 0.25f;
	public static final float KEY_LABEL_BRIGHTNESS = 0.3f;
	public static final float KEY_LABEL_FONT_SIZE = 14f;

	private float minArea;
	private float maxArea;
	private float midArea;
	private String minLabel;
	private String midLabel;
	private String maxLabel;

	private float scalingFactor;
	private String legendTitle;
	private String legendSubtitle;
	
	public CircleSizeLegend(Collection<Float> values, float scalingFactor,
			String legendTitle, String legendSubtitle) {
		this.scalingFactor = scalingFactor;
		this.legendTitle = legendTitle;
		this.legendSubtitle = legendSubtitle;

		minArea = Collections.min(values);
		maxArea = Collections.max(values);
		midArea = Math.round((minArea + maxArea) / 2.0);
		minLabel = roundFormatted(minArea);
		midLabel = roundFormatted(midArea);
		maxLabel = roundFormatted(maxArea);
	}

	private static String roundFormatted(double number) {
		if (number == (int) number) {
			return String.valueOf((int) number);
		} else {
			return String.valueOf(NumberUtilities.roundToNDecimalPlaces(number,
					2));
		}
	}

	public float getScalingFactor() {
		return scalingFactor;
	}

	public String getLegendTitle() {
		return legendTitle;
	}

	public String getLegendSubtitle() {
		return legendSubtitle;
	}

	public float getMinArea() {
		return minArea;
	}

	public float getMaxArea() {
		return maxArea;
	}

	public float getMidArea() {
		return midArea;
	}

	public String getMinLabel() {
		return minLabel;
	}

	public String getMidLabel() {
		return midLabel;
	}

	public String getMaxLabel() {
		return maxLabel;
	}

	public void render(GraphicsState state, float leftBoundary, float rightBoundary) {
		state.save();

		float minRadius = Node
				.calculateRadius(getMinArea(), getScalingFactor());
		float midRadius = Node
				.calculateRadius(getMidArea(), getScalingFactor());
		float maxRadius = Node
				.calculateRadius(getMaxArea(), getScalingFactor());

		double circleX = maxRadius;

		float minCircleY = minRadius + 5;
		float midCircleY = midRadius + 5;
		float maxCircleY = maxRadius + 5;

		float labelX = 2 * maxRadius + 5;

		float minLabelY = minCircleY
				+ (CircleSizeLegend.EXTREMA_LABEL_FONT_SIZE / 2.0f);
		float midLabelY = Math.max(midCircleY
				+ (CircleSizeLegend.EXTREMA_LABEL_FONT_SIZE / 2.0f), minLabelY
				+ CircleSizeLegend.EXTREMA_LABEL_FONT_SIZE);
		float maxLabelY = Math.max(-maxCircleY
				+ (CircleSizeLegend.EXTREMA_LABEL_FONT_SIZE / 2.0f), midLabelY
				+ CircleSizeLegend.EXTREMA_LABEL_FONT_SIZE);

		state.current.translate(leftBoundary, rightBoundary);

		// Draw title				
		state.setFontSize(CircleSizeLegend.KEY_LABEL_FONT_SIZE);
		state.setGray(CircleSizeLegend.KEY_LABEL_BRIGHTNESS);
		state.current.drawString(getLegendTitle(), 0, 0);
		state.current.translate(0, CircleSizeLegend.KEY_LABEL_FONT_SIZE);

		// Translate over by the center of the title so that 0 0 is centered.
		FontMetrics fontMetrics = state.current.getFontMetrics();
		Rectangle2D titleBounds = fontMetrics.getStringBounds(getLegendTitle(), state.current);
		Rectangle2D subtitleBounds = fontMetrics.getStringBounds(getLegendSubtitle(), state.current);
		double shiftX = titleBounds.getCenterX() - subtitleBounds.getCenterX();
		state.current.translate(shiftX, 0);
		
		//Draw subtitle
		double subtitleFontSize = 10;
		Color subtitleColor = Color.darkGray;
		state.setFontSize(subtitleFontSize);
		state.current.setColor(subtitleColor);
		state.current.drawString(getLegendSubtitle(), 0, 0);
		
		// Draw the graphic
		// TODO could this be made into a shape or something?
		state.setFontSize(CircleSizeLegend.EXTREMA_LABEL_FONT_SIZE);
		NodeRenderer.render(state, circleX, minCircleY, minRadius,
				CircleSizeLegend.CIRCLE_BRIGHTNESS);
		state.current.drawString(getMinLabel(), labelX, minLabelY);
		NodeRenderer.render(state, circleX, midCircleY, midRadius,
				CircleSizeLegend.CIRCLE_BRIGHTNESS);
		state.current.drawString(getMidLabel(), labelX, midLabelY);
		NodeRenderer.render(state, circleX, maxCircleY, maxRadius,
				CircleSizeLegend.CIRCLE_BRIGHTNESS);
		state.current.drawString(getMaxLabel(), labelX, maxLabelY);

		state.restore();
	}

}
