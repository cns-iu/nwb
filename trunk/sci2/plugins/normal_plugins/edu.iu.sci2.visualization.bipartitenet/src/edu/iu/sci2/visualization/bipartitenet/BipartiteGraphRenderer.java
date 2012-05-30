package edu.iu.sci2.visualization.bipartitenet;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.LinkedHashMap;

import math.geom2d.Point2D;
import math.geom2d.line.LineSegment2D;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import edu.iu.sci2.visualization.bipartitenet.component.NodeView;
import edu.iu.sci2.visualization.bipartitenet.component.Paintable;
import edu.iu.sci2.visualization.bipartitenet.component.PaintableContainer;
import edu.iu.sci2.visualization.bipartitenet.component.edge.ThicknessCodedEdgeView;
import edu.iu.sci2.visualization.bipartitenet.component.edge.shape.EdgeShape;
import edu.iu.sci2.visualization.bipartitenet.model.BipartiteGraphDataModel;
import edu.iu.sci2.visualization.bipartitenet.model.Edge;
import edu.iu.sci2.visualization.bipartitenet.model.Node;
import edu.iu.sci2.visualization.bipartitenet.scale.Scale;

public class BipartiteGraphRenderer implements Paintable {
	
	private final BipartiteGraphDataModel data;
	private ImmutableMap<Node, NodeView> nodeToNodeView;
	
	private PaintableContainer painter = new PaintableContainer();

	private final LineSegment2D leftLine;

	private final LineSegment2D rightLine;
	private final Scale<Double,Double> nodeRadiusCoding;
	private final Scale<Double,Double> edgeCoding;
	private final int maxNodeRadius;
	private final Font[] nodeFonts;
	private final double nodeToPageEdgeDistance;
	private final EdgeShape edgeShape;

	public BipartiteGraphRenderer(BipartiteGraphDataModel skel,
			LineSegment2D leftLine, LineSegment2D rightLine,
			int maxNodeRadius, Scale<Double,Double> nodeRadiusCoding, 
			Scale<Double,Double> edgeCoding,
			Font[] nodeFonts, double nodeToPageEdgeDistance,
			EdgeShape edgeShape) {
		this.data = skel;
		this.leftLine = leftLine;
		this.rightLine = rightLine;
		this.nodeRadiusCoding = nodeRadiusCoding;
		this.maxNodeRadius = maxNodeRadius;
		this.edgeCoding = edgeCoding;
		this.nodeFonts = nodeFonts;
		this.nodeToPageEdgeDistance = nodeToPageEdgeDistance;
		this.edgeShape = edgeShape;

		nodeToNodeView = ImmutableMap.copyOf(placeNodes());
		placeEdges();
		
		
	}

	private void placeEdges() {
		for (Edge e : data.getEdges()) {
			Preconditions.checkState(nodeToNodeView.containsKey(e.getLeftNode()));
			Preconditions.checkState(nodeToNodeView.containsKey(e.getRightNode()));
			ThicknessCodedEdgeView ev = new ThicknessCodedEdgeView(e, nodeToNodeView.get(e.getLeftNode()),
					nodeToNodeView.get(e.getRightNode()), edgeCoding,
					edgeShape);
			
			painter.insert(ev);
		}
	}

	private LinkedHashMap<Node, NodeView> placeNodes() {
		LinkedHashMap<Node,NodeView> nodeViews = Maps.newLinkedHashMap();
		nodeViews.putAll(placeNodesOnLine(data.getLeftNodes(), getLeftLine(), nodeFonts[0]));
		nodeViews.putAll(placeNodesOnLine(data.getRightNodes(), getRightLine(), nodeFonts[1]));
		
		for (NodeView nv : nodeViews.values()) {
			painter.add(nv);
		}
		
		return nodeViews;
	}

	private LinkedHashMap<Node, NodeView> placeNodesOnLine(ImmutableList<Node> nodes,
			LineSegment2D centerLine, Font nodeFont) {
		LinkedHashMap<Node,NodeView> nodeViews = Maps.newLinkedHashMap();
		int numNodes = nodes.size();
		double denominator = Math.max(1, numNodes - 1); // don't divide by 0!
		double maxHeight = Math.min(centerLine.getLength() / denominator, maxNodeRadius);
		
		for (int i = 0; i < numNodes; i++) {
			Point2D centerPoint = centerLine.getPoint(i / denominator);
			NodeView view = new NodeView(nodes.get(i), centerPoint, nodeRadiusCoding, 
					maxHeight, nodeFont, nodeToPageEdgeDistance);
			nodeViews.put(nodes.get(i), view);
		}
		return nodeViews;
	}

	public LineSegment2D getLeftLine() {
		return leftLine;
	}

	public LineSegment2D getRightLine() {
		return rightLine;
	}

	public void paint(Graphics2D g) {
		g.setColor(Color.black);
		painter.paint(g);
	}
	
	
}
