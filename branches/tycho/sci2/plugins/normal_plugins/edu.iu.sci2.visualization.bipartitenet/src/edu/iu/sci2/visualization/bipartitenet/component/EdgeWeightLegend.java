package edu.iu.sci2.visualization.bipartitenet.component;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import math.geom2d.Point2D;
import math.geom2d.line.LineSegment2D;

import com.google.common.collect.ImmutableList;

import edu.iu.sci2.visualization.bipartitenet.PageDirector;
import edu.iu.sci2.visualization.bipartitenet.component.SimpleLabelPainter.XAlignment;
import edu.iu.sci2.visualization.bipartitenet.component.SimpleLabelPainter.YAlignment;
import edu.iu.sci2.visualization.bipartitenet.scale.Scale;

public class EdgeWeightLegend implements Paintable {
	private final Point2D topLeft;
	private final Scale<Double, Double> coding;
	private final ImmutableList<Double> labeledValues;
	private final ComplexLabelPainter titlePainter;
	
	private static final double ARROW_LENGTH = 40;
	private static final int LABEL_X_OFFSET = 10;
	private final Font labelFont;
	

	public EdgeWeightLegend(Point2D topLeft, ImmutableList<String> headers,
			Scale<Double,Double> coding,
			ImmutableList<Double> labeledValues,
			Font titleFont, Font labelFont) {
		this.topLeft = topLeft;
		this.coding = coding;
		this.labeledValues = labeledValues;
		this.labelFont = labelFont;
		
		this.titlePainter = new ComplexLabelPainter.Builder(topLeft, labelFont, Color.black)
			.withLineSpacing(PageDirector.LINE_SPACING)
			.addLine(headers.get(0), titleFont)
			.addLine(headers.get(1))
			.addLine(headers.get(2), labelFont, Color.gray)
			.build();
	}

	@Override
	public void paint(Graphics2D g) {
		this.titlePainter.paint(g);
		float yOffset = this.titlePainter.estimateHeight() + 8;
		paintArrows(g, yOffset);
	}

	private void paintArrows(Graphics2D gForLabels, float yOffset) {
		Graphics2D g = (Graphics2D) gForLabels.create();
		Point2D arrowsTopLeft = topLeft.translate(0, yOffset);
		Point2D arrowStart = arrowsTopLeft;
		for (Double value : labeledValues.reverse()) {
			// loop invariant: arrowStart is the start of the current arrow
			Point2D arrowEnd = arrowStart.translate(ARROW_LENGTH, 0);
			LineSegment2D line = new LineSegment2D(arrowStart, arrowEnd);
			float lineThickness = coding.apply(value).floatValue();
			ThicknessCodedEdgeView.drawArrow(line, lineThickness, g);
			
			Point2D labelPoint = arrowEnd.translate(LABEL_X_OFFSET, 0);
			new SimpleLabelPainter(labelPoint, XAlignment.LEFT, YAlignment.STRIKE_HEIGHT, value.toString(), labelFont, null, Truncator.none())
				.paint(gForLabels);
			
			// preserve invariant
			arrowStart = arrowStart.translate(0, 1.2 * labelFont.getSize());
		}
		g.dispose();
		
	}
}
