package edu.iu.sci2.visualization.scimaps.rendering.common.scimaps;

import java.util.Set;

import oim.vivo.scimapcore.journal.Edge;
import edu.iu.sci2.visualization.scimaps.tempvis.GraphicsState;

/**
 * This class renders an edge.
 * 
 */
public class EdgeRenderer {
	public static void renderEdges(GraphicsState state, Set<Edge> edges) {

		for (Edge edge : edges) {
			state.current.draw(edge.toLine2D());
		}
	}
}
