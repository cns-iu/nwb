package edu.iu.sci2.visualization.scimaps.rendering.common.scimaps;

import java.awt.Color;

import oim.vivo.scimapcore.journal.Node;
import edu.iu.sci2.visualization.scimaps.tempvis.GraphicsState;

/**
 * This class is responsible for rendering a node
 * 
 */
public class NodeRenderer {

	/*
	 * FIXME XXX TODO This should be removed. It's only effect is drawing a
	 * circle with no width or height sinc the (int) of 1 / 5 = 0;
	 * 
	 * It is used in only one place, but I don't want it's removal to be part of
	 * my clean up commit.
	 */
	@Deprecated
	public static void renderEmpty(GraphicsState state, Node node) {
		state.save();
		state.current.setColor(Color.BLACK);

		state.drawArc((int) node.getX(), (int) node.getY(),
				(int) (Node.MINIMUM_SIZE / 5.0), 0, 360);
		state.restore();
	}

	/**
	 * Draw a {@link Node} sized by the {@code scalingFactor} and {@code weight}
	 * of the {@link GraphicsState}.
	 */
	public static void render(GraphicsState state, Node node, float weight,
			float scalingFactor) {
		float radius = Node.calculateRadius(weight, scalingFactor);

		state.save();
		state.current.setColor(node.getColor());
		state.drawArc((int) node.getX(), (int) node.getY(), (int) radius, 0,
				360);
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
