package edu.iu.sci2.visualization.bipartitenet.model;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import math.geom2d.Point2D;
import edu.iu.sci2.visualization.bipartitenet.component.NodeView;
import edu.iu.sci2.visualization.bipartitenet.component.SimpleLabelPainter;
import edu.iu.sci2.visualization.bipartitenet.component.SimpleLabelPainter.XAlignment;
import edu.iu.sci2.visualization.bipartitenet.component.SimpleLabelPainter.YAlignment;
import edu.iu.sci2.visualization.bipartitenet.component.Truncator;

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
				new SimpleLabelPainter(
						getAlignPoint(nv),
						alignDirection,
						YAlignment.STRIKE_HEIGHT,
						nv.getLabel(),
						defaultFont,
						null, 
						Truncator.atWidth(nodeCenterToPageEdge - nv.getCenterToTextDistance() - 10));
		
		painter.paint(g);
	}
	
	public Color getFillColor() {
		return fillColor;
	}
}
