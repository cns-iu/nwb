package edu.iu.sci2.visualization.bipartitenet.component;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.GlyphVector;
import java.awt.geom.Rectangle2D;

import math.geom2d.Point2D;
import math.geom2d.conic.Circle2D;
import math.geom2d.line.LineSegment2D;

import com.google.common.collect.ImmutableList;

import edu.iu.sci2.visualization.bipartitenet.PageDirector;
import edu.iu.sci2.visualization.bipartitenet.component.SimpleLabelPainter.XAlignment;
import edu.iu.sci2.visualization.bipartitenet.component.SimpleLabelPainter.YAlignment;
import edu.iu.sci2.visualization.bipartitenet.scale.Scale;

public class CircleRadiusLegend implements Paintable {
	private final Scale<Double,Double> coding;
	private final ImmutableList<Double> labeledValues;
	private final Point2D topCenter;
	private final String title;
	private final double maxRadius;
	
	private static final Font TITLE_FONT = PageDirector.BASIC_FONT.deriveFont(Font.BOLD);
	private static final Font LEGEND_FONT = PageDirector.BASIC_FONT.deriveFont(Font.PLAIN, 10);
	
	private static final int LABEL_X_OFFSET = 5; // from outer edge of circles to the labels
	private static final int LEGEND_Y_OFFSET = 25; // from top of title to top of circles
	
	
	public CircleRadiusLegend(Point2D topCenter, String title,
			Scale<Double,Double> coding,
			ImmutableList<Double> labeledValues, double maxRadius) {
		this.topCenter = topCenter;
		this.title = title;
		this.coding = coding;
		this.labeledValues = labeledValues;
		this.maxRadius = maxRadius;
	}


	@Override
	public void paint(Graphics2D g) {
		paintTitle(g);
		paintCircles(g);
		paintDataLabels(g);
	}

	private void paintTitle(Graphics2D g) {
		// TODO Use drawString?
		GlyphVector titleGV = TITLE_FONT.createGlyphVector(g.getFontRenderContext(), title);
		Rectangle2D bounds = titleGV.getVisualBounds();
		float x = (float) (topCenter.getX() - bounds.getCenterX());
		float y = (float) (topCenter.getY() - bounds.getY());
		g.drawGlyphVector(titleGV, x, y);
	}

	private void paintCircles(Graphics2D g) {
		Point2D legendTopCenter = topCenter.translate(0, LEGEND_Y_OFFSET);
		for (Double value : labeledValues) {
			double radius = coding.apply(value);
			// circle center
			double circleX = legendTopCenter.getX() - maxRadius,
					circleY = legendTopCenter.getY() + 2 * maxRadius - radius;
			new Circle2D(circleX, circleY, radius).draw(g);
		}
	}
	
	private void paintDataLabels(Graphics2D g) {
		Point2D labelsTop = topCenter.translate(LABEL_X_OFFSET, LEGEND_Y_OFFSET);
		LineSegment2D labelLine = new LineSegment2D(labelsTop, labelsTop.translate(0, 2 * maxRadius)); // the line "points" downward
		
		ImmutableList<Double> reversedValues = labeledValues.reverse();
		
		int numLabels = reversedValues.size();
		double denominator = Math.max(1, numLabels - 1);
	
		for (int i = 0; i < numLabels; i++) {
			Point2D labelPoint = labelLine.getPoint((i) / denominator);
			new SimpleLabelPainter(labelPoint, XAlignment.LEFT, YAlignment.STRIKE_HEIGHT,
					reversedValues.get(i).toString(), LEGEND_FONT, null).paint(g);
		}		
	}
}
