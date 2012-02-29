package edu.iu.sci2.visualization.bipartitenet;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.LinkedHashMap;

import math.geom2d.Point2D;
import math.geom2d.line.LineSegment2D;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import edu.iu.sci2.visualization.bipartitenet.component.EdgeView;
import edu.iu.sci2.visualization.bipartitenet.component.NodeView;
import edu.iu.sci2.visualization.bipartitenet.component.Paintable;
import edu.iu.sci2.visualization.bipartitenet.component.PaintableContainer;
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
	private final Scale<Double,Color> edgeCoding;
	private final int maxNodeRadius;

	public BipartiteGraphRenderer(BipartiteGraphDataModel skel,
			LineSegment2D leftLine, LineSegment2D rightLine,
			int maxNodeRadius, Scale<Double,Double> nodeRadiusCoding, Scale<Double,Color> edgeCoding) {
		this.data = skel;
		this.leftLine = leftLine;
		this.rightLine = rightLine;
		this.nodeRadiusCoding = nodeRadiusCoding;
		this.maxNodeRadius = maxNodeRadius;
		this.edgeCoding = edgeCoding;

		nodeToNodeView = ImmutableMap.copyOf(placeNodes());
		placeEdges();
		
		
	}

	private void placeEdges() {
		for (Edge e : data.getEdges()) {
			EdgeView ev = new EdgeView(e, nodeToNodeView.get(e.getLeftNode()),
					nodeToNodeView.get(e.getRightNode()), edgeCoding);
			
			painter.add(ev);
		}
	}

	private LinkedHashMap<Node, NodeView> placeNodes() {
		LinkedHashMap<Node,NodeView> nodeViews = Maps.newLinkedHashMap();
		nodeViews.putAll(placeNodesOnLine(data.getRightNodes(), getRightLine()));
		nodeViews.putAll(placeNodesOnLine(data.getLeftNodes(), getLeftLine()));
		
		for (NodeView nv : nodeViews.values()) {
			painter.add(nv);
		}
		
		return nodeViews;
	}

	private LinkedHashMap<Node, NodeView> placeNodesOnLine(ImmutableList<Node> nodes,
			LineSegment2D centerLine) {
		LinkedHashMap<Node,NodeView> nodeViews = Maps.newLinkedHashMap();
		int numNodes = nodes.size();
		double denominator = Math.max(1, numNodes - 1); // don't divide by 0!
		double maxHeight = Math.min(centerLine.getLength() / denominator, maxNodeRadius);
		
		for (int i = 0; i < numNodes; i++) {
			Point2D centerPoint = centerLine.getPoint(i / denominator);
			NodeView view = new NodeView(nodes.get(i), centerPoint, nodeRadiusCoding, maxHeight);
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
		
//		doTestStuff(g);
	}
	
	
}
