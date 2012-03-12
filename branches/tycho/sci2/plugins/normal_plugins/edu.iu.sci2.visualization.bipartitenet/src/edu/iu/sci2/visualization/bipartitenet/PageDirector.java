package edu.iu.sci2.visualization.bipartitenet;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import math.geom2d.Point2D;
import math.geom2d.line.LineSegment2D;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import edu.iu.sci2.visualization.bipartitenet.component.CircleRadiusLegend;
import edu.iu.sci2.visualization.bipartitenet.component.ComplexLabelPainter;
import edu.iu.sci2.visualization.bipartitenet.component.EdgeWeightLegend;
import edu.iu.sci2.visualization.bipartitenet.component.Paintable;
import edu.iu.sci2.visualization.bipartitenet.component.PaintableContainer;
import edu.iu.sci2.visualization.bipartitenet.component.SimpleLabelPainter;
import edu.iu.sci2.visualization.bipartitenet.component.SimpleLabelPainter.XAlignment;
import edu.iu.sci2.visualization.bipartitenet.component.SimpleLabelPainter.YAlignment;
import edu.iu.sci2.visualization.bipartitenet.model.BipartiteGraphDataModel;
import edu.iu.sci2.visualization.bipartitenet.model.Edge;
import edu.iu.sci2.visualization.bipartitenet.model.Node;
import edu.iu.sci2.visualization.bipartitenet.scale.ConstantValue;
import edu.iu.sci2.visualization.bipartitenet.scale.Scale;
import edu.iu.sci2.visualization.bipartitenet.scale.ZeroAnchoredCircleRadiusScale;

public class PageDirector implements Paintable {
	public static final double LINE_SPACING = 1.2;

	public static enum Layout {
		PRINT(792, 612,
				new Point2D(18, 18), // title top-left corner
				new LineSegment2D(296, 144, 296, 412),
				new LineSegment2D(792 - 296, 144, 792 - 296, 412),
				12, 3,
				new int[] { 14, 12, 10, 10 }),
		WEB(1280, 960,
				null, // No title!
				new LineSegment2D(480, 100, 480, 780),
				new LineSegment2D(800, 100, 800, 780),
				20, 5,
				new int[] { 20, 16, 14, 10 });
		
		private final int width;
		private final int height;
		private final LineSegment2D leftLine;
		private final LineSegment2D rightLine;
		private final int maxNodeRadius;
		private final Point2D headerPosition;
		private final int maxEdgeThickness;
		private final int[] fontSizes;

		private Layout(int width, int height, Point2D headerPosition, 
				LineSegment2D leftLine, LineSegment2D rightLine,
				int maxNodeRadius, int maxEdgeThickness,
				int[] fontSizes) {
			this.width = width;
			this.height = height;
			this.headerPosition = headerPosition;
			this.leftLine = leftLine;
			this.rightLine = rightLine;
			this.maxNodeRadius = maxNodeRadius;
			this.maxEdgeThickness = maxEdgeThickness;
			assert fontSizes.length == 4;
			this.fontSizes = fontSizes;
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
		
		Point2D getHeaderPosition() {
			return headerPosition;
		}

		Point2D getFooterPosition() {
			Point2D thePoint = new Point2D(width / 2.0, height - 20);
			return thePoint;
		}

		public int getMaxNodeRadius() {
			return maxNodeRadius;
		}

		public int getMaxEdgeThickness() {
			return maxEdgeThickness;
		}
		
		public Point2D getSortLegendPosition() {
			return getLegendPositions(4).get(0);
		}
		
		public Point2D getCircleLegendPosition() {
			return getLegendPositions(4).get(1);
		}
		
		public Point2D getEdgeLegendPosition() {
			return getLegendPositions(4).get(2);
		}
		
		public Point2D getHowToReadLegendPosition() {
			return getLegendPositions(4).get(3);
		}
		
		public ImmutableList<Point2D> getLegendPositions(int numLegends) {
			List<Point2D> points = Lists.newArrayList();
			double denominator = Math.max(1, numLegends - 1);
			
			LineSegment2D legendLine = new LineSegment2D(
					18, height - estimateLegendHeight(),
					width / 2.0, height - estimateLegendHeight());
			
			for (int i = 0; i < numLegends; i++) {
				points.add(legendLine.getPoint(i / denominator));
			}
			
			return ImmutableList.copyOf(points);
		}
		
		private int estimateLegendHeight() {
			return 72 + getMaxNodeRadius() + 3 * fontSizes[0];
		}

		public Font getTitleFont() {
			return INTERNAL_BASE_FONT.deriveFont(Font.BOLD, fontSizes[0]);
		}
		public Font getDefaultNodeFont() {
			return INTERNAL_BASE_FONT.deriveFont(Font.PLAIN, fontSizes[1]);
		}
		public Font getLegendFont() {
			return INTERNAL_BASE_FONT.deriveFont(Font.PLAIN, fontSizes[2]);
		}
		public Font getFooterFont() {
			return INTERNAL_BASE_FONT.deriveFont(Font.ITALIC, fontSizes[3]);
		}
	}
	
	private PaintableContainer painter = new PaintableContainer();
	private BipartiteGraphDataModel dataModel;

	private static final Font INTERNAL_BASE_FONT = findBasicFont(10);
//	public static final Font BASIC_FONT = findBasicFont(10);
//	private static final Font TITLE_FONT = BASIC_FONT.deriveFont(Font.BOLD, 14);
//	private static final Font SUB_TITLE_FONT = BASIC_FONT.deriveFont(Font.BOLD);
	private static final String TITLE = "Bipartite Network Graph";

	private final Layout layout;
	
	private final String footer = "NIH’s Reporter Web site (projectreporter.nih.gov), NETE & CNS (cns.iu.edu)";

	public PageDirector(final Layout layout, 
			final BipartiteGraphDataModel dataModel, 
			final String leftSideType, final String leftSideTitle, 
			final String rightSideType, final String rightSideTitle) {
		this.layout = layout;
		this.dataModel = dataModel;

		
		painter.add(makeSortingLegend());
		
		// Make codings for the nodes and edges (size and color)
		// If the nodes/edges are not weighted, it makes a "constant" coding.
		// Only put in a legend if the nodes/edges are weighted.
		Scale<Double,Double> nodeCoding = makeNodeCoding();
		if (dataModel.hasWeightedNodes()) {
			painter.add(makeNodeLegend(nodeCoding));
		}

		Scale<Double,Double> edgeCoding = makeEdgeCoding();
		if (dataModel.hasWeightedEdges()) {
			painter.add(makeEdgeLegend(edgeCoding));
		}
		
		BipartiteGraphRenderer renderer = new BipartiteGraphRenderer(dataModel,
				layout.getLeftLine(), layout.getRightLine(), layout.getMaxNodeRadius(), 
				nodeCoding, edgeCoding,
				layout.getDefaultNodeFont());
		painter.add(renderer);
		
		// The main title, and headers
		Point2D headerPosition = layout.getHeaderPosition();
		// Position's null if there should be no title 
		if (headerPosition != null) {
			painter.add(
					new ComplexLabelPainter.Builder(headerPosition, layout.getLegendFont(), Color.BLACK)
					.addLine(TITLE, layout.getTitleFont())
					.addLine("Generated from Cornell NSF Data")
					.addLine(getTimeStamp())
					.withLineSpacing(LINE_SPACING)
					.build());
		}
		
		// The titles of the two columns
		painter.add(new SimpleLabelPainter(layout.getLeftTitlePosition(), 
				XAlignment.RIGHT, YAlignment.BASELINE, leftSideTitle, layout.getTitleFont(), null));
		painter.add(new SimpleLabelPainter(layout.getRightTitlePosition(), 
				XAlignment.LEFT, YAlignment.BASELINE, rightSideTitle, layout.getTitleFont(), null));
		
		// The footer
		painter.add(new SimpleLabelPainter(layout.getFooterPosition(), 
				XAlignment.CENTER, YAlignment.BASELINE, footer, layout.getFooterFont(),
				Color.gray));
	}

	private Paintable makeSortingLegend() {
		String sort;
		if (dataModel.hasWeightedNodes()) {
			sort = dataModel.getNodeValueAttribute();
		} else {
			sort = "Label (alphabetically)";
		}
		
		return new ComplexLabelPainter.Builder(layout.getSortLegendPosition(), layout.getLegendFont(), Color.black)
			.withLineSpacing(LINE_SPACING)
			.addLine("Legend", layout.getTitleFont())
			.addLine("Sorted by")
			.addLine(sort, Color.gray)
			.build();
	}

	private String getTimeStamp() {
		// MMMMMdyyyy, hmmaazzz!  And you?
		return new SimpleDateFormat("MMMMMMMMMMMMMMMMM d, yyyy | h:mm aa zzz").format(new Date());
	}

	/**
	 * Looks for a font that will work on this system.  It tries several that are likely to
	 * be present on a Windows or Linux system, and falls back to Java's default font.
	 * @return
	 */
	private static Font findBasicFont(int size) {
		final String JAVA_FALLBACK_FONT = "Dialog";
		ImmutableList<String> fontFamiliesToTry =
				ImmutableList.of("Arial", "Helvetica", "FreeSans", "Nimbus Sans");
		
		Font thisFont = new Font(JAVA_FALLBACK_FONT, Font.PLAIN, 12);
		for (String family : fontFamiliesToTry) {
			System.out.println("Trying " + family);
			thisFont = new Font(family, Font.PLAIN, size);
			if (! thisFont.getFamily().equals(JAVA_FALLBACK_FONT)) {
				// found one that the system has!
				System.out.println(String.format("Yes, deciding on %s (started with %s)", thisFont.getFamily(),
						family));
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

	private Scale<Double,Double> makeEdgeCoding() {
		if (dataModel.hasWeightedEdges()) {
			Scale<Double,Double> thicknessScale = new ZeroAnchoredCircleRadiusScale(layout.getMaxEdgeThickness());
			thicknessScale.train(Iterables.transform(dataModel.getEdges(), Edge.WEIGHT_GETTER));
			thicknessScale.doneTraining();
			return thicknessScale;
		} else {
			return new ConstantValue<Double,Double>(Double.valueOf(1));
		}
	}

	private Paintable makeNodeLegend(Scale<Double,Double> coding) {
		ArrayList<Double> labels = Lists.newArrayList(coding.getExtrema());
		double halfway = (labels.get(0) + labels.get(1)) / 2.0;
		labels.add(1, halfway);
		CircleRadiusLegend legend = new CircleRadiusLegend(
				layout.getCircleLegendPosition(), 
				ImmutableList.of(" ", "Circle Area", dataModel.getNodeValueAttribute()), 
				coding, ImmutableList.copyOf(labels),
				layout.getTitleFont(), layout.getLegendFont());
		return legend;
	}

	private Paintable makeEdgeLegend(Scale<Double,Double> edgeCoding) {
		ArrayList<Double> labels = Lists.newArrayList(edgeCoding.getExtrema());
		double halfway = (labels.get(0) + labels.get(1)) / 2.0;
		labels.add(1, halfway);
		EdgeWeightLegend legend = new EdgeWeightLegend(layout.getEdgeLegendPosition(), 
				ImmutableList.of(" ", "Edge Weight", dataModel.getEdgeValueAttribute()),
				edgeCoding, ImmutableList.copyOf(labels),
				layout.getTitleFont(), layout.getLegendFont());
		return legend;
	}

	@Override
	public void paint(Graphics2D g) {
		painter.paint(g);
	}
}
