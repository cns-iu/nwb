package edu.iu.sci2.visualization.scimaps.rendering.scimaps;

import java.awt.Color;

import oim.vivo.scimapcore.journal.Node;
import edu.iu.sci2.visualization.scimaps.tempvis.GraphicsState;

/**
 * This class is responsible for rendering a node
 * 
 */
public class NodeRenderer {

	public static void renderEmpty(GraphicsState state, Node node) {
		state.save();
		state.current.setColor(Color.BLACK);

		state.drawArc((int) node.getX(), (int) node.getY(),
				(int) (Node.MINIMUM_SIZE / 5.0), 0, 360);
		state.restore();
	}

	public static void render(GraphicsState state, Node node, float total,
			float scalingFactor) {
		float radius = Node.calculateRadius(total, scalingFactor);

		state.save();
		state.current.setColor(node.getColor());
		state.drawArc((int) node.getX(), (int) node.getY(), (int) radius, 0,
				360);
		state.restore();
	}

	public static void render(GraphicsState state, double x, double y,
			double radius, double brightness) {
		state.save();
		state.setGray(brightness);
		state.drawArc((int) x, (int) y, (int) radius, 0, 360);
		state.restore();
	}
}
