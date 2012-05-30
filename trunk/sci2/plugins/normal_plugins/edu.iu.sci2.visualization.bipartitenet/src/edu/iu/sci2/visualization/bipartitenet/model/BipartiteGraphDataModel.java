package edu.iu.sci2.visualization.bipartitenet.model;

import java.util.Collection;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;


public class BipartiteGraphDataModel {
	private final ImmutableList<Node> leftNodes;
	private final ImmutableList<Node> rightNodes;
	private final ImmutableList<Edge> edges;
	private final String nodeValueAttribute;
	private final String edgeValueAttribute;

	public BipartiteGraphDataModel(ImmutableList<Node> leftNodes,
			ImmutableList<Node> rightNodes,
			Collection<Edge> edges, String nodeValueAttribute, String edgeValueAttribute) {
		this.edges = ImmutableList.copyOf(edges);
		this.nodeValueAttribute = nodeValueAttribute;
		this.edgeValueAttribute = edgeValueAttribute;
		
		this.leftNodes = leftNodes;
		this.rightNodes = rightNodes;
	}


	public ImmutableList<Node> getLeftNodes() {
		return leftNodes;
	}

	public ImmutableList<Node> getRightNodes() {
		return rightNodes;
	}

	public ImmutableList<Edge> getEdges() {
		return edges;
	}
	
	@Override
	public String toString() {
		return Objects.toStringHelper(this)
				.add("left", leftNodes)
				.add("right", rightNodes)
				.add("edges", edges)
				.add("nodeValueAttribute", nodeValueAttribute)
				.add("edgeValueAttribute", edgeValueAttribute)
				.toString();
	}

	public String getNodeValueAttribute() {
		return nodeValueAttribute;
	}

	public String getEdgeValueAttribute() {
		return edgeValueAttribute;
	}
	
	public boolean hasWeightedNodes() {
		return nodeValueAttribute != null;
	}
	
	public boolean hasWeightedEdges() {
		return edgeValueAttribute != null;
	}


	public boolean hasAnyNodes() {
		return ! (leftNodes.isEmpty() && rightNodes.isEmpty()); 
	}

}
