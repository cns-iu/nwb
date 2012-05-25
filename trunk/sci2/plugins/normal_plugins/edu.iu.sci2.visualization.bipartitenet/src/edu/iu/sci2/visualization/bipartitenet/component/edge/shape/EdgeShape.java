package edu.iu.sci2.visualization.bipartitenet.component.edge.shape;

import edu.iu.sci2.visualization.bipartitenet.component.NodeView;
import math.geom2d.curve.AbstractContinuousCurve2D;

/**
 * An interface for objects that can create a line or curve that connects two NodeViews together.
 */
public interface EdgeShape {
	/**
	 * Creates a line or curve that connects the given NodeViews together. 
	 */
	public AbstractContinuousCurve2D connectNodes(NodeView source, NodeView dest);
}
