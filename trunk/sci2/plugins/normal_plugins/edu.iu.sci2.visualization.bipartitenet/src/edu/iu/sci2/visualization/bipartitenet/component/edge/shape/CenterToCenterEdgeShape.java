package edu.iu.sci2.visualization.bipartitenet.component.edge.shape;

import math.geom2d.curve.AbstractContinuousCurve2D;
import math.geom2d.line.LineSegment2D;
import edu.iu.sci2.visualization.bipartitenet.component.NodeView;

/**
 * Creates a line segment between the centers of the two nodes, and then clips off the
 * ends of the line segment so that it does not overlap the nodes.
 *
 */
public class CenterToCenterEdgeShape implements EdgeShape {
	/*
	 * You can also put extra space between the node and the edge, so they don't touch, by
	 * setting this to a nonzero value.
	 */
	private static final double NODE_EDGE_SPACE = 0;
	
	@Override
	public AbstractContinuousCurve2D connectNodes(NodeView source, NodeView dest) {
		LineSegment2D baseLine = new LineSegment2D(source.getNodeCenter(), dest.getNodeCenter());
		
		return GeomUtils.clipEndsFromLineSegment(baseLine, 
				source.getRadius() + NODE_EDGE_SPACE,
				dest.getRadius() + NODE_EDGE_SPACE);
	}

}
