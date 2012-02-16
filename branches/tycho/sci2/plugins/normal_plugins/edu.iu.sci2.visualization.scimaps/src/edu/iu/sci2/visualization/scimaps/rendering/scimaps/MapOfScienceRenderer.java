package edu.iu.sci2.visualization.scimaps.rendering.scimaps;

import static edu.iu.sci2.visualization.scimaps.tempvis.GraphicsState.inch;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.geom.Line2D;
import java.util.Map;
import java.util.Set;

import oim.vivo.scimapcore.journal.Edge;
import oim.vivo.scimapcore.journal.Node;
import oim.vivo.scimapcore.journal.Nodes;
import edu.iu.sci2.visualization.scimaps.MapOfScience;
import edu.iu.sci2.visualization.scimaps.tempvis.GraphicsState;

/**
 * This is the class responsible for rendering a map of science. It includes the
 * nodes, edges, legends, category labels, etc.
 * 
 */
public class MapOfScienceRenderer {

	public static void render(GraphicsState state, MapOfScience mapOfScience,
			float scalingFactor) {
			
		state.setFontSize(6);
		renderLeftSide(state);
		renderRightSide(state);
		
		renderMapNodesAndEdges(state, mapOfScience, scalingFactor);
		
		renderCategoryLabels(state);
	}

	private static void renderCategoryLabel(GraphicsState state, String s,
			float r, float g, float b, float x, float y) {
		state.save();
		state.current.setColor(new Color(r, g, b));
		state.current.drawString(s, x, y);
		state.restore();
	}

	public static void renderCategoryLabels(GraphicsState state) {
		state.save();

		state.setFontSize(8);

		state.current.translate(0, inch(.33f));

		renderCategoryLabel(state, "Social Sciences", 1, 1, 0, inch(6.2f),
				inch(-1));
		renderCategoryLabel(state, "Electrical Engineering", 1, .5216f, 1,
				inch(.9f), inch(-1.24f));
		renderCategoryLabel(state, "& Computer Science", 1, .5216f, 1,
				inch(.9f), inch(-1.12f));
		renderCategoryLabel(state, "Biology", 0, .6f, 0, inch(3.73f),
				inch(-.6f));
		renderCategoryLabel(state, "Biotechnology", 0, 1, .5f, inch(3.1f),
				inch(-1.45f));
		renderCategoryLabel(state, "Brain Research", 1, .56f, 0, inch(5.63f),
				inch(-1.23f));
		renderCategoryLabel(state, "Medical Specialties", 1, 0, 0, inch(5.1f),
				inch(-.88f));
		renderCategoryLabel(state, "Chemical, Mechanical, & Civil Engineering",
				.38f, 1, 1, inch(2.08f), inch(-1.1f));
		renderCategoryLabel(state, "Chemistry", 0, 0, 1, inch(2.38f),
				inch(-2.45f));
		renderCategoryLabel(state, "Earth Sciences", .52f, .19f, .05f,
				inch(2.65f), inch(-.4f));
		renderCategoryLabel(state, "Health Professionals", .94f, .55f, .51f,
				inch(5.3f), inch(-2.55f));
		renderCategoryLabel(state, "Humanities", 1, 1, .5f, inch(6.18f),
				inch(-.4f));
		renderCategoryLabel(state, "Infectious Disease", .72f, 0, 0,
				inch(4.41f), inch(-1.05f));
		renderCategoryLabel(state, "Math & Physics", .64f, .078f, .98f,
				inch(1.15f), inch(-2.31f));

		state.restore();
	}

	public static void renderSideBar(GraphicsState state, String s) {
		state.save();

		state.setFontSize(7);
		state.setGray(.3);
		FontMetrics metrics = state.current.getFontMetrics();
		float halfWidth = metrics.stringWidth(s) / 2.0f;

		state.current.draw(new Line2D.Float(-(halfWidth + inch(1)), 2,
				inch(.8f) - (halfWidth + inch(1)), 2));
		state.current.drawString(s, -halfWidth, 3.5f);
		state.current.draw(new Line2D.Float(halfWidth + inch(.2f), 2, halfWidth
				+ inch(.2f) + inch(.8f), 2));

		state.restore();
	}


	private static void renderMapNodesAndEdges(GraphicsState state,
			MapOfScience mapOfScience, float scalingFactor) {

		Set<Node> nodes = MapOfScience.getNodes();
		Set<Edge> edges = MapOfScience.getEdges();
		Map<Integer, Float> mapping = mapOfScience.getIdWeightMapping();
		state.save();

		state.current.translate(inch(1), inch(.3f));
		state.setGray(.7);

		EdgeRenderer.renderEdges(state, edges);

		for (Node node : nodes) {
			if (!mapping.containsKey(node.getId())) {
				NodeRenderer.renderEmpty(state, node);
			}
		}

		for (Integer id : mapOfScience.getMappedIdsByWeight()) {

			Node node = Nodes.getNodeByID(id);

			float weight = mapping.get(node.getId());

			NodeRenderer.render(state, node, weight, scalingFactor);
		}

		state.restore();
	}

	private static void renderRightSide(GraphicsState state) {
		state.save();
		state.current.translate(inch(8), inch(-1.2f));
		state.current.rotate(Math.PI / 2);
		renderSideBar(state, "Map continued on left");
		state.restore();
	}

	private static void renderLeftSide(GraphicsState state) {
		state.save();
		state.current.translate(inch(.5f), inch(-1.2f));
		state.current.rotate(-Math.PI / 2);
		renderSideBar(state, "Map continued on right");
		state.restore();
	}
}
