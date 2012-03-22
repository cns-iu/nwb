package edu.iu.sci2.visualization.bipartitenet.component;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import math.geom2d.Point2D;
import math.geom2d.conic.Circle2D;
import math.geom2d.line.LineSegment2D;
import math.geom2d.line.StraightLine2D;

import org.apache.commons.lang.ArrayUtils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Ordering;

import edu.iu.sci2.visualization.bipartitenet.PageDirector;
import edu.iu.sci2.visualization.bipartitenet.component.SimpleLabelPainter.XAlignment;
import edu.iu.sci2.visualization.bipartitenet.component.SimpleLabelPainter.YAlignment;
import edu.iu.sci2.visualization.bipartitenet.scale.Scale;
import edu.iu.sci2.visualization.geomaps.numberformat.NumberFormatFactory;
import edu.iu.sci2.visualization.geomaps.numberformat.UnsignedZeroDecimalFormat;

public class CircleRadiusLegend implements Paintable {
	private final Scale<Double,Double> coding;
	private final ImmutableList<Double> labeledValues;
	private final Point2D topLeft;
	private final ComplexLabelPainter titlePainter;
	
	private static final int LABEL_X_OFFSET = 5; // from outer edge of circles to the labels
	private final Font labelFont;
	
	public CircleRadiusLegend(Point2D topLeft, ImmutableList<String> headerLines,
			Scale<Double,Double> coding,
			ImmutableList<Double> labeledValues,
			Font titleFont, Font labelFont) {
		this.topLeft = topLeft;
		this.coding = coding;
		this.labeledValues = labeledValues;
		this.labelFont = labelFont;
		this.titlePainter = new ComplexLabelPainter.Builder(topLeft, labelFont, Color.black)
			.withLineSpacing(PageDirector.LINE_SPACING)
			.addLine(headerLines.get(0), titleFont)
			.addLine(headerLines.get(1))
			.addLine(headerLines.get(2), labelFont, Color.gray)
			.build();
	}


	@Override
	public void paint(Graphics2D g) {
		this.titlePainter.paint(g);
		float yOffset = this.titlePainter.estimateHeight() + 8;

		// avoid lop-sided circles
		g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

		LineSegment2D maxDiameter = paintCircles(g, yOffset);
		paintDataLabels(g, maxDiameter, yOffset);
	}

	private LineSegment2D paintCircles(Graphics2D g, float yOffset) {
		double maxActualRadius = coding.apply(Ordering.natural().max(labeledValues));
		Point2D circleTopCenter = topLeft.translate(maxActualRadius, yOffset);
		Point2D circleBottomCenter = circleTopCenter.translate(0, 2 * maxActualRadius);
		for (Double value : labeledValues) {
			double radius = coding.apply(value);
			Point2D circleCenter = circleBottomCenter.translate(0, - radius);
			new Circle2D(circleCenter, radius).draw(g);
		}
		
		return new LineSegment2D(circleTopCenter, circleBottomCenter);
	}
	
	private void paintDataLabels(Graphics2D g, LineSegment2D maxDiameter, float yOffset) {
		Point2D labelsTop = topLeft.translate(LABEL_X_OFFSET + maxDiameter.getLength(), yOffset);
		StraightLine2D labelLine = maxDiameter.getParallel(labelsTop);
		
		ImmutableList<Double> reversedValues = labeledValues.reverse();
		
		int numLabels = reversedValues.size();
		double denominator = Math.max(1, numLabels - 1);
		
		UnsignedZeroDecimalFormat formatter = NumberFormatFactory.getNumberFormat(
				NumberFormatFactory.GENERAL_FORMAT, 
				ArrayUtils.toPrimitive(labeledValues.toArray(new Double[]{})));
	
		SimpleLabelPainter labelPainter = SimpleLabelPainter
				.alignedBy(XAlignment.LEFT, YAlignment.STRIKE_HEIGHT)
				.withFont(labelFont)
				.build();
		
		for (int i = 0; i < numLabels; i++) {
			Point2D labelPoint = labelLine.getPoint((i) / denominator);
			labelPainter.paintLabel(labelPoint, formatter.format(reversedValues.get(i)), g);
		}		
	}
}
