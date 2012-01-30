package edu.iu.sci2.visualization.scimaps.rendering.print2008;

import oim.vivo.scimapcore.journal.Node;
import edu.iu.sci2.visualization.scimaps.rendering.scimaps.NodeRenderer;
import edu.iu.sci2.visualization.scimaps.tempvis.GraphicsState;
import static edu.iu.sci2.visualization.scimaps.tempvis.GraphicsState.inch;
/**
 * This class renders the circle size legend.
 *
 */
public class CircleSizeLegendRenderer {
	
	public static void render(GraphicsState state, CircleSizeLegend legend) {
		float minRadius = Node.calculateRadius(legend.getMinArea(), legend.getScalingFactor());
		float midRadius = Node.calculateRadius(legend.getMidArea(), legend.getScalingFactor());
		float maxRadius = Node.calculateRadius(legend.getMaxArea(), legend.getScalingFactor());

		double circleX = maxRadius;
		
		float minCircleY = minRadius + 5;
		float midCircleY = midRadius + 5;
		float maxCircleY = maxRadius + 5;
		
		float labelX = 2*maxRadius + 5;
		
		float minLabelY = minCircleY + (CircleSizeLegend.EXTREMA_LABEL_FONT_SIZE / 2.0f);
		float midLabelY = Math.max(midCircleY + (CircleSizeLegend.EXTREMA_LABEL_FONT_SIZE / 2.0f),
									minLabelY + CircleSizeLegend.EXTREMA_LABEL_FONT_SIZE);
		float maxLabelY = Math.max(-maxCircleY + (CircleSizeLegend.EXTREMA_LABEL_FONT_SIZE / 2.0f),
									midLabelY + CircleSizeLegend.EXTREMA_LABEL_FONT_SIZE);

		state.save();
		state.current.translate(inch(1.0f), inch(-0.5f));
		state.setFontSize(CircleSizeLegend.EXTREMA_LABEL_FONT_SIZE);
		NodeRenderer.render(state, circleX, minCircleY, minRadius, CircleSizeLegend.CIRCLE_BRIGHTNESS);
		state.current.drawString(legend.getMinLabel(), labelX, minLabelY);
		NodeRenderer.render(state, circleX, midCircleY, midRadius, CircleSizeLegend.CIRCLE_BRIGHTNESS);
		state.current.drawString(legend.getMidLabel(), labelX, midLabelY);
		NodeRenderer.render(state, circleX, maxCircleY, maxRadius, CircleSizeLegend.CIRCLE_BRIGHTNESS);
		state.current.drawString(legend.getMaxLabel(), labelX, maxLabelY);
		state.save();
		state.setFontSize(CircleSizeLegend.KEY_LABEL_FONT_SIZE);
		state.setGray(CircleSizeLegend.KEY_LABEL_BRIGHTNESS);
		state.current.drawString("Circle Area: " + legend.getCircleSizeMeaning(), 0, -CircleSizeLegend.KEY_LABEL_FONT_SIZE);
		state.restore();
		
		state.restore();
	
	}
	
}
