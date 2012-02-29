package edu.iu.sci2.visualization.bipartitenet.component;

import java.awt.Color;
import java.awt.Graphics2D;

import math.geom2d.Point2D;
import math.geom2d.conic.Circle2D;
import edu.iu.sci2.visualization.bipartitenet.PageDirector;
import edu.iu.sci2.visualization.bipartitenet.model.Node;
import edu.iu.sci2.visualization.bipartitenet.scale.Scale;

public class NodeView implements Paintable {
	public static final int NODE_TEXT_PADDING = 8;
	
	private final Node node;
	private final Point2D nodeCenter;
	private final Scale<Double,Double> coding;
	private final double maxHeight;

	public NodeView(Node node, Point2D nodeCenter, Scale<Double,Double> coding, double maxHeight) {
		super();
		this.node = node;
		this.nodeCenter = nodeCenter;
		this.coding = coding;
		this.maxHeight = maxHeight;
	}

	public int getCenterToTextDistance() {
		// round up
		// no, this is wrong
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
		Circle2D circle = new Circle2D(nodeCenter.getX(), nodeCenter.getY(), getRadius());
		node.getDestination().paintLabel(this, g, maxHeight);
		g.setColor(node.getDestination().getFillColor());
		circle.fill(g);
		g.setColor(Color.black);
		circle.draw(g);
	}

	public String getLabel() {
		return getNode().getLabel();
	}

}
