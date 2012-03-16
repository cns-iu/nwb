package edu.iu.sci2.visualization.bipartitenet.model;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import math.geom2d.Point2D;
import edu.iu.sci2.visualization.bipartitenet.component.NodeView;
import edu.iu.sci2.visualization.bipartitenet.component.SimpleLabelPainter;
import edu.iu.sci2.visualization.bipartitenet.component.SimpleLabelPainter.XAlignment;
import edu.iu.sci2.visualization.bipartitenet.component.SimpleLabelPainter.YAlignment;

public enum NodeDestination {
	LEFT(Color.decode("#3399FF"), XAlignment.RIGHT) {
		@Override
		protected Point2D getAlignPoint(NodeView nv) {
			return nv.getNodeCenter().translate(- nv.getCenterToTextDistance(), 0);
		}
	},
	RIGHT(Color.decode("#FF9900"), XAlignment.LEFT) {
		@Override
		protected Point2D getAlignPoint(NodeView nv) {
			return nv.getNodeCenter().translate(+ nv.getCenterToTextDistance(), 0);
		}
	};

	
	private final Color fillColor;
	private final XAlignment alignDirection;
	
	private NodeDestination(Color fillColor, XAlignment alignDirection) {
		this.fillColor = fillColor;
		this.alignDirection = alignDirection;
	}
	
	protected abstract Point2D getAlignPoint(NodeView nv);

	public void paintLabel(NodeView nv, Graphics2D g, Font defaultFont, double nodeCenterToPageEdge) {
		SimpleLabelPainter painter =
				SimpleLabelPainter.alignedBy(alignDirection, YAlignment.STRIKE_HEIGHT)
				.withFont(defaultFont)
				.truncatedTo(nodeCenterToPageEdge - nv.getCenterToTextDistance() - 10)
				.build();
		
		painter.paintLabel(getAlignPoint(nv), nv.getLabel(), g);
	}
	
	public Color getFillColor() {
		return fillColor;
	}
}
