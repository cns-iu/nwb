package edu.iu.sci2.visualization.bipartitenet.component;

import java.awt.Font;
import java.awt.Graphics2D;

import math.geom2d.Point2D;
import math.geom2d.conic.Circle2D;
import math.geom2d.line.LineSegment2D;
import math.geom2d.line.StraightLine2D;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Ordering;

import edu.iu.sci2.visualization.bipartitenet.PageDirector;
import edu.iu.sci2.visualization.bipartitenet.component.SimpleLabelPainter.XAlignment;
import edu.iu.sci2.visualization.bipartitenet.component.SimpleLabelPainter.YAlignment;
import edu.iu.sci2.visualization.bipartitenet.scale.Scale;

public class CircleRadiusLegend implements Paintable {
	private final Scale<Double,Double> coding;
	private final ImmutableList<Double> labeledValues;
	private final Point2D topCenter;
	private final String title;
	private final SimpleLabelPainter titlePainter;
	
	private static final Font TITLE_FONT = PageDirector.BASIC_FONT.deriveFont(Font.BOLD);
	private static final Font LEGEND_FONT = PageDirector.BASIC_FONT.deriveFont(Font.PLAIN, 10);
	
	private static final int LABEL_X_OFFSET = 5; // from outer edge of circles to the labels
	private static final int LEGEND_Y_OFFSET = 25; // from top of title to top of circles
	
	
	public CircleRadiusLegend(Point2D topCenter, String title,
			Scale<Double,Double> coding,
			ImmutableList<Double> labeledValues) {
		this.topCenter = topCenter;
		this.title = title;
		this.coding = coding;
		this.labeledValues = labeledValues;
		this.titlePainter = new SimpleLabelPainter(this.topCenter, 
				XAlignment.CENTER, YAlignment.ASCENT, 
				this.title, TITLE_FONT, null);
	}


	@Override
	public void paint(Graphics2D g) {
		this.titlePainter.paint(g);
		
		LineSegment2D maxDiameter = paintCircles(g);
		paintDataLabels(g, maxDiameter);
	}

	private LineSegment2D paintCircles(Graphics2D g) {
		double maxActualRadius = coding.apply(Ordering.natural().max(labeledValues));
		Point2D circleTopCenter = topCenter.translate(- maxActualRadius, LEGEND_Y_OFFSET);
		Point2D circleBottomCenter = circleTopCenter.translate(0, 2 * maxActualRadius);
		for (Double value : labeledValues) {
			double radius = coding.apply(value);
			Point2D circleCenter = circleBottomCenter.translate(0, - radius);
			new Circle2D(circleCenter, radius).draw(g);
		}
		
		return new LineSegment2D(circleTopCenter, circleBottomCenter);
	}
	
	private void paintDataLabels(Graphics2D g, LineSegment2D maxDiameter) {
		Point2D labelsTop = topCenter.translate(LABEL_X_OFFSET, LEGEND_Y_OFFSET);
		StraightLine2D labelLine = maxDiameter.getParallel(labelsTop);
		
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
