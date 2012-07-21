package edu.iu.sci2.visualization.scimaps.rendering.scimaps;

import oim.vivo.scimapcore.journal.Node;
import edu.iu.sci2.visualization.scimaps.tempvis.GraphicsState;

/**
 * This class is responsible for rendering a node
 * 
 */
public class NodeRenderer {

	/**
	 * Draw a {@link Node} sized by the {@code scalingFactor} and {@code weight}
	 * of the {@link GraphicsState}.
	 */
	public static void render(GraphicsState state, Node node, float weight,
			float nodeScalingFactor) {
		float radius = Node.calculateRadius(weight, nodeScalingFactor);

		state.save();
		state.current.setColor(node.getColor());
		state.drawCircle((int) node.getX(), (int) node.getY(), (int) radius);
		state.restore();
	}

	/**
	 * This supports only the old print2008, which only exists to support the
	 * USDA field rendering. SOMEDAY Update the USDA Field renderer. I can't
	 * because I have no data to test it with.
	 */
	@Deprecated
	public static void render(GraphicsState state, double x, double y,
			double radius, double brightness) {
		state.save();
		state.setGray(brightness);
		state.drawArc((int) x, (int) y, (int) radius, 0, 360);
		state.restore();
	}
}
