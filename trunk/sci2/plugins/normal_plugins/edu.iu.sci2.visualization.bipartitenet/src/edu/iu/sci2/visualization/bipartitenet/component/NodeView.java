package edu.iu.sci2.visualization.bipartitenet.component;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import math.geom2d.Point2D;
import math.geom2d.conic.Circle2D;
import edu.iu.sci2.visualization.bipartitenet.model.Node;
import edu.iu.sci2.visualization.bipartitenet.scale.Scale;

public class NodeView implements Paintable {
	public static final int NODE_TEXT_PADDING = 8;
	
	private final Node node;
	private final Point2D nodeCenter;
	private final Scale<Double,Double> coding;
	private final double maxHeight;
	private final Font nodeFont;
	private final double nodeToPageEdgeDistance;

	public NodeView(Node node, Point2D nodeCenter, Scale<Double,Double> coding, 
			double maxHeight, Font nodeFont, double nodeToPageEdgeDistance) {
		super();
		this.node = node;
		this.nodeCenter = nodeCenter;
		this.coding = coding;
		this.maxHeight = maxHeight;
		this.nodeFont = nodeFont;
		this.nodeToPageEdgeDistance = nodeToPageEdgeDistance;
	}

	public int getCenterToTextDistance() {
		// round up
		return Math.round(0.5f + (float) maxHeight + NODE_TEXT_PADDING);
	}

	private Node getNode() {
		return node;
	}

	public Point2D getNodeCenter() {
		return nodeCenter;
	}
	
	public double getRadius() {
		return coding.apply(Node.WEIGHT_GETTER.apply(node));
	}

	@Override
	public void paint(Graphics2D g) {
		// avoid lop-sided circles
		g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

		Circle2D circle = getNodeCircle();
		node.getDestination().paintLabel(this, g, nodeFont, nodeToPageEdgeDistance);
		g.setColor(node.getDestination().getFillColor());
		circle.fill(g);
		g.setColor(Color.black);
		circle.draw(g);
	}

	/*
	 * I'm sad that this exposes the fact that the representation is a circle.
	 * It's only public because the EdgeView needs to use the same code as
	 * NodeView so it's not duplicated. Would like to refactor this sometime.
	 */
	public Circle2D getNodeCircle() {
		Circle2D circle = new Circle2D(nodeCenter, getRadius());
		return circle;
	}

	public String getLabel() {
		return getNode().getLabel();
	}

}
