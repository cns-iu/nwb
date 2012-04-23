package edu.iu.sci2.visualization.scimaps.rendering.common.scimaps;

import static edu.iu.sci2.visualization.scimaps.tempvis.GraphicsState.inch;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.geom.Line2D;
import java.util.Map;
import java.util.Set;

import oim.vivo.scimapcore.journal.Discipline;
import oim.vivo.scimapcore.journal.Disciplines;
import oim.vivo.scimapcore.journal.Edge;
import oim.vivo.scimapcore.journal.Node;
import oim.vivo.scimapcore.journal.Nodes;
import edu.iu.sci2.visualization.scimaps.MapOfScience;
import edu.iu.sci2.visualization.scimaps.tempvis.GraphicsState;
import edu.iu.sci2.visualization.scimaps.tempvis.PageElement;

/**
 * This is the class responsible for rendering a map of science. It includes the
 * nodes, edges, legends, category labels, etc.
 * 
 */
public class MapOfScienceRenderer implements PageElement{

	private MapOfScience mapOfScience;
	private float nodeScalingFactor;
	private double pageScalingFactor;
	private double leftBoundary;
	private double bottomBoundary;

	public MapOfScienceRenderer(MapOfScience mapOfScience, float nodeScalingFactor, double pageScalingFactor, double leftBoundary, double bottomBoundary) {
		this.mapOfScience = mapOfScience;
		this.nodeScalingFactor = nodeScalingFactor;
		this.pageScalingFactor = pageScalingFactor;
		this.leftBoundary = leftBoundary;
		this.bottomBoundary = bottomBoundary;
	}
	
	/**
	 * 
	 * This will draw the {@link MapOfScience} to the given
	 * {@link GraphicsState} using a scaling factor to increase the size of the
	 * rendered nodes. If there is an issue rendering, a
	 * {@link MapOfScienceRenderingException} will be thrown.
	 * 
	 * @param state
	 * @param mapOfScience
	 * @param scalingFactor
	 * @throws MapOfScienceRenderingException
	 */
	public static void render(GraphicsState state, MapOfScience mapOfScience,
			float scalingFactor) throws MapOfScienceRenderingException {

		state.setFontSize(6);
		renderLeftSide(state);
		renderRightSide(state);

		renderMapNodesAndEdges(state, mapOfScience, scalingFactor);

		renderDisciplineLabels(state);
	}

	private static void renderDisciplineLabel(GraphicsState state,
			String label, Discipline discipline, float x, float y)
			throws MapOfScienceRenderingException {
		if (discipline == null) {
			throw new MapOfScienceRenderingException(
					"The category labeled '"
							+ label
							+ "' could not be located by id.  The underlying Map of Science might "
							+ "have changed and the algorithm needs to be updated.");
		}
		renderDisciplineLabel(state, label, discipline.getColor(), x, y);
	}

	private static void renderDisciplineLabel(GraphicsState state,
			String label, Color color, float x, float y) {
		state.save();
		state.current.setColor(color);
		state.current.drawString(label, x, y);
		state.restore();
	}

	private static void renderDisciplineLabels(GraphicsState state)
			throws MapOfScienceRenderingException {
		state.save();

		state.setFontSize(8);

		state.current.translate(0, inch(.33f));

		// SOMEDAY fix the whole map of science so that the x and y in the
		// discipline can be used to render it.

		renderDisciplineLabel(state, "Social Sciences",
				Disciplines.getDisciplineById("13"), inch(6.2f), inch(-1));
		renderDisciplineLabel(state, "Electrical Engineering",
				Disciplines.getDisciplineById("7"), inch(.9f), inch(-1.24f));
		renderDisciplineLabel(state, "& Computer Science",
				Disciplines.getDisciplineById("7"), inch(.9f), inch(-1.12f));
		renderDisciplineLabel(state, "Biology",
				Disciplines.getDisciplineById("1"), inch(3.73f), inch(-.6f));
		renderDisciplineLabel(state, "Biotechnology",
				Disciplines.getDisciplineById("2"), inch(3.1f), inch(-1.45f));
		renderDisciplineLabel(state, "Brain Research",
				Disciplines.getDisciplineById("8"), inch(5.63f), inch(-1.23f));
		renderDisciplineLabel(state, "Medical Specialties",
				Disciplines.getDisciplineById("3"), inch(5.1f), inch(-.88f));
		renderDisciplineLabel(state,
				"Chemical, Mechanical, & Civil Engineering",
				Disciplines.getDisciplineById("4"), inch(2.08f), inch(-1.1f));
		renderDisciplineLabel(state, "Chemistry",
				Disciplines.getDisciplineById("5"), inch(2.38f), inch(-2.45f));
		renderDisciplineLabel(state, "Earth Sciences",
				Disciplines.getDisciplineById("6"), inch(2.65f), inch(-.4f));
		renderDisciplineLabel(state, "Health Professionals",
				Disciplines.getDisciplineById("12"), inch(5.3f), inch(-2.55f));
		renderDisciplineLabel(state, "Humanities",
				Disciplines.getDisciplineById("9"), inch(6.18f), inch(-.4f));
		renderDisciplineLabel(state, "Infectious Disease",
				Disciplines.getDisciplineById("10"), inch(4.41f), inch(-1.05f));
		renderDisciplineLabel(state, "Math & Physics",
				Disciplines.getDisciplineById("11"), inch(1.15f), inch(-2.31f));

		state.restore();
	}

	private static void renderSideBar(GraphicsState state, String s) {
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

		Set<Edge> edges = MapOfScience.getEdges();
		Map<Integer, Float> mapping = mapOfScience.getIdWeightMapping();
		state.save();

		state.current.translate(inch(1), inch(.3f));
		state.setGray(.7);

		EdgeRenderer.renderEdges(state, edges);

		for (Integer id : mapOfScience.getMappedIdsByWeight()) {

			Node node = Nodes.getNodeByID(id.intValue());

			float weight = mapping.get(Integer.valueOf(node.getId()))
					.floatValue();

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

	/**
	 * This is the class of error that will be given if the MapOfScience cannot
	 * be rendered.
	 * 
	 */
	public static class MapOfScienceRenderingException extends Exception {
		private static final long serialVersionUID = 7330170129647808546L;

		public MapOfScienceRenderingException(String message) {
			super(message);
		}
	}

	public void render(GraphicsState state) throws PageElementRenderingException {
		state.save();
		state.current.translate(this.leftBoundary, this.bottomBoundary);
		state.current.scale(this.pageScalingFactor, this.pageScalingFactor);
		
		try {
			MapOfScienceRenderer.render(state, this.mapOfScience, this.nodeScalingFactor);
		} catch (MapOfScienceRenderingException e) {
			throw new PageElementRenderingException(e);
		} finally {
			state.restore();
		}
	}

}
