package edu.iu.sci2.visualization.bipartitenet;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import math.geom2d.Point2D;
import math.geom2d.line.LineSegment2D;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import edu.iu.sci2.visualization.bipartitenet.component.CircleRadiusLegend;
import edu.iu.sci2.visualization.bipartitenet.component.EdgeWeightLegend;
import edu.iu.sci2.visualization.bipartitenet.component.Paintable;
import edu.iu.sci2.visualization.bipartitenet.component.PaintableContainer;
import edu.iu.sci2.visualization.bipartitenet.component.RightAlignedLabel;
import edu.iu.sci2.visualization.bipartitenet.model.BipartiteGraphDataModel;
import edu.iu.sci2.visualization.bipartitenet.model.Edge;
import edu.iu.sci2.visualization.bipartitenet.model.Node;
import edu.iu.sci2.visualization.bipartitenet.scale.ColorIntensityScale;
import edu.iu.sci2.visualization.bipartitenet.scale.ConstantValue;
import edu.iu.sci2.visualization.bipartitenet.scale.ConstantColor;
import edu.iu.sci2.visualization.bipartitenet.scale.Scale;
import edu.iu.sci2.visualization.bipartitenet.scale.ZeroAnchoredCircleRadiusScale;

public class PageDirector implements Paintable {
	private PaintableContainer painter = new PaintableContainer();
	private BipartiteGraphDataModel dataModel;

	public static final int PAGE_HEIGHT = 800;
	public static final int PAGE_WIDTH = 800;
	public static final int MAX_RADIUS = 15;

	private static final LineSegment2D LEFT_LINE = new LineSegment2D(300, 100, 300, 500);
	private static final LineSegment2D RIGHT_LINE = new LineSegment2D(500, 100, 500, 500);
	
	public static final Font BASIC_FONT = findBasicFont();
	private static final Font TITLE_FONT = BASIC_FONT.deriveFont(Font.BOLD, 16);

	private static final Point2D CIRCLE_LEGEND_POSITION = new Point2D(250, 600);
	private static final Point2D LEFT_TITLE_POSITION = LEFT_LINE.getFirstPoint().translate(MAX_RADIUS, -50);
	private static final Point2D RIGHT_TITLE_POSITION = RIGHT_LINE.getFirstPoint().translate(- MAX_RADIUS, -50);
	private static final Point2D EDGE_LEGEND_POSITION = new Point2D(550, 600);

	public PageDirector(final BipartiteGraphDataModel dataModel, final String leftSideType, String leftSideTitle, final String rightSideType, final String rightSideTitle) {
		this.dataModel = dataModel;

		// Make codings for the nodes and edges (size and color)
		// If the nodes/edges are not weighted, it makes a "constant" coding.
		// Only put in a legend if the nodes/edges are weighted.
		Scale<Double,Double> nodeCoding = makeNodeCoding();
		if (dataModel.hasWeightedNodes()) {
			painter.add(makeNodeLegend(nodeCoding));
		}

		Scale<Double,Color> edgeCoding = makeEdgeCoding();
		if (dataModel.hasWeightedEdges()) {
			painter.add(makeEdgeLegend(edgeCoding));
		}
		
		BipartiteGraphRenderer renderer = new BipartiteGraphRenderer(dataModel,
				LEFT_LINE, RIGHT_LINE, nodeCoding, edgeCoding);
		painter.add(renderer);
		
		// The titles of the two columns
		painter.add(new RightAlignedLabel(LEFT_TITLE_POSITION, leftSideTitle, TITLE_FONT));
		painter.add(new Paintable() {
			@Override
			public void paint(Graphics2D g) {
				g.setFont(TITLE_FONT);
				g.drawString(rightSideTitle, (float) RIGHT_TITLE_POSITION.getX(), (float) RIGHT_TITLE_POSITION.getY());
			}
		});
	}

	/**
	 * Looks for a font that will work on this system.  It tries several that are likely to
	 * be present on a Windows or Linux system, and falls back to Java's default font.
	 * @return
	 */
	private static Font findBasicFont() {
		final String JAVA_FALLBACK_FONT = "Dialog";
		ImmutableList<String> fontFamiliesToTry =
				ImmutableList.of("Arial", "Helvetica", "FreeSans", "Nimbus Sans");
		
		Font thisFont = new Font(JAVA_FALLBACK_FONT, Font.PLAIN, 12);
		for (String family : fontFamiliesToTry) {
			thisFont = new Font(family, Font.PLAIN, 12);
			if (! thisFont.getFamily().equals(JAVA_FALLBACK_FONT)) {
				// found one that the system has!
				break;
			}
		}
		
		return thisFont;
	}


	private double calculateMaxNodeRadius() {
		int maxNodesOnOneSide = 
				Math.max(
					dataModel.getLeftNodes().size(), 
					dataModel.getRightNodes().size());
		return Math.min(MAX_RADIUS, LEFT_LINE.getLength() / maxNodesOnOneSide);
	}

	private Scale<Double, Double> makeNodeCoding() {
		if (dataModel.hasWeightedNodes()) {
			Scale<Double, Double> nodeScale = new ZeroAnchoredCircleRadiusScale(calculateMaxNodeRadius());
			nodeScale.train(Iterables.transform(dataModel.getLeftNodes(), Node.WEIGHT_GETTER));
			nodeScale.train(Iterables.transform(dataModel.getRightNodes(), Node.WEIGHT_GETTER));
			nodeScale.doneTraining();
			return nodeScale;
		} else {
			return new ConstantValue<Double, Double>(calculateMaxNodeRadius());
		}
	}

	private Scale<Double,Color> makeEdgeCoding() {
		if (dataModel.hasWeightedEdges()) {
			Scale<Double,Color> colorScale = ColorIntensityScale.createWithDefaultColor();
			colorScale.train(Iterables.transform(dataModel.getEdges(), Edge.WEIGHT_GETTER));
			colorScale.doneTraining();
			return colorScale;
		} else {
			return new ConstantColor();
		}
	}

	private Paintable makeNodeLegend(Scale<Double,Double> coding) {
		ImmutableList<Double> labels = 
				ImmutableList.<Double>builder().add(0.0).addAll(coding.getExtrema()).build();
		CircleRadiusLegend legend = new CircleRadiusLegend(
				CIRCLE_LEGEND_POSITION, "Circle Area: "
						+ dataModel.getNodeValueAttribute(), coding, labels,
				MAX_RADIUS);
		return legend;
	}

	private Paintable makeEdgeLegend(Scale<Double, Color> edgeCoding) {
		ImmutableList<Double> edgeLegendLabels = 
				ImmutableList.<Double>builder().add(0.0).addAll(edgeCoding.getExtrema()).build();
		EdgeWeightLegend legend = new EdgeWeightLegend(EDGE_LEGEND_POSITION, 
				"Edge Weight: " + dataModel.getEdgeValueAttribute(),
				edgeCoding, edgeLegendLabels);
		return legend;
	}

	@Override
	public void paint(Graphics2D g) {
		painter.paint(g);
	}
}
