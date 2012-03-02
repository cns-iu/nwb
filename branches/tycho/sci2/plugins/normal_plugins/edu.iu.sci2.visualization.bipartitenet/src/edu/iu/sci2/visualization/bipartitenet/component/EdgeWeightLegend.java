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
	private final Point2D topCenter;
	private final String title;
	private final Scale<Double, Color> coding;
	private final ImmutableList<Double> labeledValues;
	private final SimpleLabelPainter titlePainter;
	
	private static final int LEGEND_Y_OFFSET = 25; // from top of label to top of arrows
	private static final double ARROW_LENGTH = 40;
	private static final int BETWEEN_ARROW_Y_OFFSET = 10;
	private static final int LABEL_X_OFFSET = 10;
	
	private static final Font TITLE_FONT = PageDirector.BASIC_FONT.deriveFont(Font.BOLD);
	private static final Font LEGEND_FONT = PageDirector.BASIC_FONT.deriveFont(Font.PLAIN, 10);
	

	public EdgeWeightLegend(Point2D topCenter, String title,
			Scale<Double,Color> coding,
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
		paintArrows(g);
	}

	private void paintArrows(Graphics2D gForLabels) {
		Graphics2D g = (Graphics2D) gForLabels.create();
		Point2D arrowsTopCenter = topCenter.translate(0, LEGEND_Y_OFFSET);
		Point2D arrowStart = arrowsTopCenter.translate(-ARROW_LENGTH/2, 0);
		for (Double value : labeledValues.reverse()) {
			// loop invariant: arrowStart is the start of the current arrow
			Point2D arrowEnd = arrowStart.translate(ARROW_LENGTH, 0);
			LineSegment2D line = new LineSegment2D(arrowStart, arrowEnd);
			g.setColor(coding.apply(value));
			line.draw(g);
			EdgeView.drawArrowHead(line, g);
			
			Point2D labelPoint = arrowEnd.translate(LABEL_X_OFFSET, 0);
			new SimpleLabelPainter(labelPoint, XAlignment.LEFT, YAlignment.STRIKE_HEIGHT, value.toString(), LEGEND_FONT, null)
				.paint(gForLabels);
			
			// preserve invariant
			arrowStart = arrowStart.translate(0, BETWEEN_ARROW_Y_OFFSET);
		}
		
	}
}
