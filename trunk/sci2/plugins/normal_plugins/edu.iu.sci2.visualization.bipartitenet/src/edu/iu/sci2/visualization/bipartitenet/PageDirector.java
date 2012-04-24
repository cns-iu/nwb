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

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import edu.iu.sci2.visualization.bipartitenet.component.CircleRadiusLegend;
import edu.iu.sci2.visualization.bipartitenet.component.ComplexLabelPainter;
import edu.iu.sci2.visualization.bipartitenet.component.EdgeWeightLegend;
import edu.iu.sci2.visualization.bipartitenet.component.HowToRead;
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
	private static final String EDGE_LEGEND_TITLE = "Weight";

	private static final String CIRCLE_LEGEND_TITLE = "Area";

	private static final String MAIN_TITLE = "Network Visualization";

	private static final String FOOTER = "CNS (cns.iu.edu)";

	public static final double LINE_SPACING = 1.2;

	private static final Font BASE_FONT = findBasicFont(10);
	private static final ImmutableMap<TextType, Font> PRINT_FONTS = ImmutableMap.of(
				TextType.TITLE,      BASE_FONT.deriveFont(Font.BOLD, 14),
				TextType.NODE_LABEL, BASE_FONT.deriveFont(Font.PLAIN, 12),
				TextType.LEGEND,     BASE_FONT.deriveFont(Font.PLAIN, 10),
				TextType.FOOTER,     BASE_FONT.deriveFont(Font.ITALIC, 10),
				TextType.HOW_TO_READ,BASE_FONT.deriveFont(Font.PLAIN, 10));
	
	private static final ImmutableMap<TextType, Font> WEB_FONTS	= ImmutableMap.of(
			TextType.TITLE,      BASE_FONT.deriveFont(Font.BOLD, 20),
			TextType.NODE_LABEL, BASE_FONT.deriveFont(Font.PLAIN, 16),
			TextType.LEGEND,     BASE_FONT.deriveFont(Font.PLAIN, 14),
			TextType.FOOTER,     BASE_FONT.deriveFont(Font.ITALIC, 10),
			TextType.HOW_TO_READ,BASE_FONT.deriveFont(Font.PLAIN, 14));
	
	public static enum Layout {
		PRINT(792, 612,
				new Point2D(18, 18), // title top-left corner
				new LineSegment2D(296, 144, 296, 412),
				new LineSegment2D(792 - 296, 144, 792 - 296, 412),
				14, 3,
				PRINT_FONTS,
				true),
		WEB(1280, 960,
				null, // No title!
				new LineSegment2D(480, 100, 480, 780),
				new LineSegment2D(800, 100, 800, 780),
				24, 5,
				WEB_FONTS,
				false);
		
		private final int width;
		private final int height;
		private final LineSegment2D leftLine;
		private final LineSegment2D rightLine;
		private final int maxNodeRadius;
		private final Point2D headerPosition;
		private final int maxEdgeThickness;
		private final boolean hasHowToRead;
		private final ImmutableMap<TextType, Font> fontScheme;

		private Layout(int width, int height, Point2D headerPosition, 
				LineSegment2D leftLine, LineSegment2D rightLine,
				int maxNodeRadius, int maxEdgeThickness,
				ImmutableMap<TextType, Font> fontScheme, boolean hasHowToRead) {
			this.width = width;
			this.height = height;
			this.headerPosition = headerPosition;
			this.leftLine = leftLine;
			this.rightLine = rightLine;
			this.maxNodeRadius = maxNodeRadius;
			this.maxEdgeThickness = maxEdgeThickness;
			Preconditions.checkArgument(fontScheme.keySet()
					.equals(ImmutableSet.copyOf(TextType.values())));
			this.fontScheme = fontScheme;
			this.hasHowToRead = hasHowToRead;
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
			return 72 + getMaxNodeRadius() + 3 * getFont(TextType.TITLE).getSize();
		}

		public Font getFont(TextType type) {
			return fontScheme.get(type);
		}
		
		public boolean hasHowToRead() {
			return this.hasHowToRead;
		}
	}
	
	private PaintableContainer painter = new PaintableContainer();
	private BipartiteGraphDataModel dataModel;

	private final Layout layout;
	

	// This whitespace is distributed evenly among the spaces between all the node labels.
	// Actually, the total whitespace decreases with increasing # of labels, so this is not linear.
	// Argh! Just read the code.
	private final double WHITESPACE_AMONG_LABELS = 30;

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
		Scale<Double,Double> nodeCoding = makeNodeCodingAndLegend();
		
		Scale<Double,Double> edgeCoding = makeEdgeCodingAndLegend();
		
		if (layout.hasHowToRead()) {
			painter.add(new HowToRead(layout.getFont(TextType.TITLE),
					layout.getFont(TextType.HOW_TO_READ), 
					layout.getHowToReadLegendPosition()));
		}
		
		BipartiteGraphRenderer renderer = new BipartiteGraphRenderer(dataModel,
				layout.getLeftLine(), layout.getRightLine(), layout.getMaxNodeRadius(), 
				nodeCoding, edgeCoding,
				createNodeLabelFonts(), layout.getWidth() - layout.getRightLine().getFirstPoint().getX());
		painter.add(renderer);
		
		// The main title, and headers
		Point2D headerPosition = layout.getHeaderPosition();
		// Position's null if there should be no title 
		if (headerPosition != null) {
			painter.add(
					new ComplexLabelPainter.Builder(headerPosition, layout.getFont(TextType.LEGEND), Color.BLACK)
					.addLine(MAIN_TITLE, layout.getFont(TextType.TITLE))
					.addLine(getTimeStamp())
					.withLineSpacing(LINE_SPACING)
					.build());
		}
		
		// The titles of the two columns
		painter.add(SimpleLabelPainter.alignedBy(XAlignment.RIGHT, YAlignment.BASELINE)
				.withFont(layout.getFont(TextType.TITLE))
				.makeLabel(layout.getLeftTitlePosition(), leftSideTitle));
		painter.add(SimpleLabelPainter.alignedBy(XAlignment.LEFT,  YAlignment.BASELINE)
				.withFont(layout.getFont(TextType.TITLE))
				.makeLabel(layout.getRightTitlePosition(), rightSideTitle));
		
		// The footer
		painter.add(SimpleLabelPainter.alignedBy(XAlignment.CENTER,  YAlignment.BASELINE)
				.withFont(layout.getFont(TextType.FOOTER))
				.makeLabel(layout.getFooterPosition(), FOOTER));
	}

	private Font[] createNodeLabelFonts() {
		Font baseFont = layout.getFont(TextType.NODE_LABEL);
		double baseFontSize = baseFont.getSize2D();
		
		double pageSize = layout.getLeftLine().getLength();

		int nodesOnLeft  = dataModel.getLeftNodes().size(), 
			nodesOnRight = dataModel.getRightNodes().size();
		
		double leftFontSize = Math.min((pageSize / nodesOnLeft) 
				- (WHITESPACE_AMONG_LABELS / Math.pow(nodesOnLeft, 2)), baseFontSize);
		double rightFontSize = Math.min((pageSize / nodesOnRight) 
				- (WHITESPACE_AMONG_LABELS / Math.pow(nodesOnRight, 2)), baseFontSize);
		
		return new Font[] {baseFont.deriveFont((float) leftFontSize), baseFont.deriveFont((float) rightFontSize)};
	}

	private Paintable makeSortingLegend() {
		String sort;
		if (dataModel.hasWeightedNodes()) {
			sort = dataModel.getNodeValueAttribute();
		} else {
			sort = "Label (alphabetically)";
		}
		
		return new ComplexLabelPainter.Builder(layout.getSortLegendPosition(), layout.getFont(TextType.LEGEND), Color.black)
			.withLineSpacing(LINE_SPACING)
			.addLine("Legend", layout.getFont(TextType.TITLE))
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
			thisFont = new Font(family, Font.PLAIN, size);
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
		return Math.min(layout.getMaxNodeRadius(), layout.getLeftLine().getLength() / (maxNodesOnOneSide));
	}
	
	private Scale<Double, Double> makeNodeCodingAndLegend() {
		if (dataModel.hasWeightedNodes()) {
			Scale<Double, Double> nodeScale = new ZeroAnchoredCircleRadiusScale(calculateMaxNodeRadius());
			nodeScale.train(Iterables.transform(dataModel.getLeftNodes(), Node.WEIGHT_GETTER));
			nodeScale.train(Iterables.transform(dataModel.getRightNodes(), Node.WEIGHT_GETTER));
			nodeScale.doneTraining();
			
			painter.add(makeNodeLegend(nodeScale));

			return nodeScale;
		} else {
			return new ConstantValue<Double, Double>(calculateMaxNodeRadius());
		}
	}

	private Scale<Double,Double> makeEdgeCodingAndLegend() {
		if (dataModel.hasWeightedEdges()) {
			Scale<Double,Double> thicknessScale = new ZeroAnchoredCircleRadiusScale(layout.getMaxEdgeThickness());
			thicknessScale.train(Iterables.transform(dataModel.getEdges(), Edge.WEIGHT_GETTER));
			thicknessScale.doneTraining();
			
			painter.add(makeEdgeLegend(thicknessScale));
			
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
				ImmutableList.of(" ", CIRCLE_LEGEND_TITLE, dataModel.getNodeValueAttribute()), 
				coding, ImmutableList.copyOf(labels),
				layout.getFont(TextType.TITLE), layout.getFont(TextType.LEGEND));
		return legend;
	}

	private Paintable makeEdgeLegend(Scale<Double,Double> edgeCoding) {
		ArrayList<Double> labels = Lists.newArrayList(edgeCoding.getExtrema());
		double halfway = (labels.get(0) + labels.get(1)) / 2.0;
		labels.add(1, halfway);
		EdgeWeightLegend legend = new EdgeWeightLegend(layout.getEdgeLegendPosition(), 
				ImmutableList.of(" ", EDGE_LEGEND_TITLE, dataModel.getEdgeValueAttribute()),
				edgeCoding, ImmutableList.copyOf(labels),
				layout.getFont(TextType.TITLE), layout.getFont(TextType.LEGEND));
		return legend;
	}

	@Override
	public void paint(Graphics2D g) {
		painter.paint(g);
	}
}
