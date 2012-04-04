package edu.iu.sci2.visualization.bipartitenet.component;

import java.awt.Color;
import java.awt.Graphics2D;

import math.geom2d.AffineTransform2D;
import math.geom2d.Point2D;
import math.geom2d.line.AbstractLine2D;
import math.geom2d.line.LineSegment2D;
import math.geom2d.polygon.SimplePolygon2D;
import edu.iu.sci2.visualization.bipartitenet.model.Edge;
import edu.iu.sci2.visualization.bipartitenet.scale.Scale;

public class DarknessCodedEdgeView implements Paintable {
	
	private static final double NODE_EDGE_SPACE = 4;
	private static final double ARROW_HEAD_SIDE_LENGTH = 4;
	private final NodeView dest;
	private final NodeView src;
	private final Scale<Double,Color> edgeCoding;
	private final Edge e;

	public DarknessCodedEdgeView(Edge e, NodeView src, NodeView dest, Scale<Double,Color> edgeCoding) {
		this.e = e;
		this.src = src;
		this.dest = dest;
		this.edgeCoding = edgeCoding;
	}

	@Override
	public void paint(Graphics2D g) {
		Color edgeColor = edgeCoding.apply(e.getWeight());
		g.setColor(edgeColor);
		LineSegment2D grossLine = new LineSegment2D(src.getNodeCenter(), dest.getNodeCenter());
		double tStart = (src.getRadius() + NODE_EDGE_SPACE) / grossLine.getLength(),
				tEnd = (grossLine.getLength() - dest.getRadius() - NODE_EDGE_SPACE)
							/ grossLine.getLength();
		AbstractLine2D fineLine = grossLine.getSubCurve(tStart, tEnd);
		
		fineLine.draw(g);

		drawArrowHead(fineLine, g);
		
	}

	public static void drawArrowHead(AbstractLine2D line, Graphics2D g) {
		Point2D end = line.getLastPoint();
		Point2D colinearPoint = line.getPoint(1 - (ARROW_HEAD_SIDE_LENGTH / line.getLength()));
		Point2D cwPoint = colinearPoint.transform(AffineTransform2D.createRotation(end, Math.PI / 6));
		Point2D ccwPoint = colinearPoint.transform(AffineTransform2D.createRotation(end, -Math.PI / 6));
		SimplePolygon2D arrowHead = new SimplePolygon2D(end, cwPoint, ccwPoint);
		arrowHead.draw(g);
		arrowHead.fill(g);
	}

}
