package edu.iu.sci2.visualization.bipartitenet.model;

import java.util.Collection;
import java.util.List;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;


public class BipartiteGraphDataModel {
	private final ImmutableList<Node> leftNodes;
	private final ImmutableList<Node> rightNodes;
	private final ImmutableList<Edge> edges;
	private final String nodeValueAttribute;
	private final String edgeValueAttribute;

	public BipartiteGraphDataModel(Collection<Node> allNodes,
			Collection<Edge> edges, String nodeValueAttribute, String edgeValueAttribute) {
		this.edges = ImmutableList.copyOf(edges);
		this.nodeValueAttribute = nodeValueAttribute;
		this.edgeValueAttribute = edgeValueAttribute;
		
		List<Node> leftNodes = Lists.newArrayList(),
				rightNodes = Lists.newArrayList();
		
		for (Node n : allNodes) {
			if (n.getDestination() == NodeDestination.LEFT) {
				leftNodes.add(n);
			} else {
				rightNodes.add(n);
			}
		}
		
		Ordering<Node> nodeOrdering;
		if (this.hasWeightedNodes()) {
			// Descending weight
			nodeOrdering = Node.WEIGHT_ORDERING.reverse();
		} else {
			// Ascending label
			nodeOrdering = Node.LABEL_ORDERING;
		}
		
		this.leftNodes = ImmutableList.copyOf(nodeOrdering.sortedCopy(leftNodes));
		this.rightNodes = ImmutableList.copyOf(nodeOrdering.sortedCopy(rightNodes));
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
