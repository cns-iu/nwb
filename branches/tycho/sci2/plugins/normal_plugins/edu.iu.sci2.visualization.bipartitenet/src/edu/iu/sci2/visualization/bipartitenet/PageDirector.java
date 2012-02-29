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
	public static enum Layout {
		PRINT(792, 612, 
				new LineSegment2D(296, 100, 296, 412),
				new LineSegment2D(792 - 296, 100, 792 - 296, 412),
				12),
		WEB(1280, 960, 
				new LineSegment2D(480, 100, 480, 780),
				new LineSegment2D(800, 100, 800, 780),
				20),
		SQUARE(800, 800, 
				new LineSegment2D(300, 100, 300, 500), 
				new LineSegment2D(500, 100, 500, 500),
				15);
		private final int width;
		private final int height;
		private final LineSegment2D leftLine;
		private final LineSegment2D rightLine;
		private final int maxNodeRadius;

		private Layout(int width, int height, LineSegment2D leftLine, LineSegment2D rightLine, int maxNodeRadius) {
			this.width = width;
			this.height = height;
			this.leftLine = leftLine;
			this.rightLine = rightLine;
			this.maxNodeRadius = maxNodeRadius;
		}

		public int getWidth() {
			return width;
		}

		public int getHeight() {
			return height;
		}

		LineSegment2D getLeftLine() {
			return leftLine;
		}

		LineSegment2D getRightLine() {
			return rightLine;
		}
		
		Point2D getLeftTitlePosition() {
			return leftLine.getFirstPoint().translate(maxNodeRadius, -50);
		}
		
		Point2D getRightTitlePosition() {
			return rightLine.getFirstPoint().translate(- maxNodeRadius, -50);
		}
		
		Point2D getCircleLegendPosition() {
			return leftLine.getLastPoint().translate(-50, 50);
		}
		
		Point2D getEdgeLegendPosition() {
			return rightLine.getLastPoint().translate(50, 50);
		}

		public int getMaxNodeRadius() {
			return maxNodeRadius;
		}
	}
	
	private PaintableContainer painter = new PaintableContainer();
	private BipartiteGraphDataModel dataModel;

	public static final Font BASIC_FONT = findBasicFont();
	private static final Font TITLE_FONT = BASIC_FONT.deriveFont(Font.BOLD, 14);

	private final Layout layout;

	public PageDirector(final Layout layout, 
			final BipartiteGraphDataModel dataModel, 
			final String leftSideType, final String leftSideTitle, 
			final String rightSideType, final String rightSideTitle) {
		this.layout = layout;
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
				layout.getLeftLine(), layout.getRightLine(), layout.getMaxNodeRadius(), nodeCoding, edgeCoding);
		painter.add(renderer);
		
		// The titles of the two columns
		painter.add(new RightAlignedLabel(layout.getLeftTitlePosition(), leftSideTitle, TITLE_FONT));
		final Point2D rightTitlePosition = layout.getRightTitlePosition();
		painter.add(new Paintable() {
			@Override
			public void paint(Graphics2D g) {
				g.setFont(TITLE_FONT);
				g.drawString(rightSideTitle, (float) rightTitlePosition.getX(), (float) rightTitlePosition.getY());
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
		return Math.min(layout.getMaxNodeRadius(), layout.getLeftLine().getLength() / maxNodesOnOneSide);
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
				layout.getCircleLegendPosition(), "Circle Area: "
						+ dataModel.getNodeValueAttribute(), coding, labels,
				layout.getMaxNodeRadius());
		return legend;
	}

	private Paintable makeEdgeLegend(Scale<Double, Color> edgeCoding) {
		ImmutableList<Double> edgeLegendLabels = 
				ImmutableList.<Double>builder().add(0.0).addAll(edgeCoding.getExtrema()).build();
		EdgeWeightLegend legend = new EdgeWeightLegend(layout.getEdgeLegendPosition(), 
				"Edge Weight: " + dataModel.getEdgeValueAttribute(),
				edgeCoding, edgeLegendLabels);
		return legend;
	}

	@Override
	public void paint(Graphics2D g) {
		painter.paint(g);
	}
}
