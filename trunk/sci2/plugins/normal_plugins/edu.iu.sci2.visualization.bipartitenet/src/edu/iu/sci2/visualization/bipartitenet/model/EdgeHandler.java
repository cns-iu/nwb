package edu.iu.sci2.visualization.bipartitenet.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.cishell.utilities.NumberUtilities;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import edu.iu.sci2.visualization.bipartitenet.LogStream;

public class EdgeHandler {

	private final String edgeWeightColumn;
	private final NodeProvider nodeProvider;
	private final List<Edge> edges = Lists.newArrayList();

	public EdgeHandler(String edgeWeightColumn, NodeProvider nodeProvider) {
		this.edgeWeightColumn = edgeWeightColumn;
		this.nodeProvider = nodeProvider;
	}

	public void setDirectedEdgeSchema(LinkedHashMap<String, String> schema) {
		checkEdgeSchema(schema);
	}

	public void setUndirectedEdgeSchema(LinkedHashMap<String, String> schema) {
		checkEdgeSchema(schema);
	}

	private void checkEdgeSchema(LinkedHashMap<String, String> schema) {
		/* The edge weight column should come from a list of all edge columns put together in
		 * mutateParameters. */
		Preconditions.checkArgument((edgeWeightColumn == null) || (schema.containsKey(edgeWeightColumn)));
	}

	
	public void addDirectedEdge(int sourceNode, int targetNode,
			Map<String, Object> attributes) {
		// We don't currently make a distinction between directed and undirected edges.
		addUndirectedEdge(sourceNode, targetNode, attributes);
	}

	public void addUndirectedEdge(int node1, int node2,	Map<String, Object> attributes) {
		// Determine left and right
		Node left, right, something;
		something = nodeProvider.getNodeById(node1);			
		if (something.getDestination() == NodeDestination.LEFT) {
			left = something;
			right = nodeProvider.getNodeById(node2);
		} else {
			left = nodeProvider.getNodeById(node2);
			right = something;
		}
		
		if (left.getDestination() == right.getDestination()) {
			LogStream.WARNING.send("Graph is not properly bipartite: %s and %s are linked but are on the same side!",
					left, right);
		}
		
		// Find the edge weight
		double weight;
		if (edgeWeightColumn != null) {
			weight = NumberUtilities.interpretObjectAsDouble(attributes.get(edgeWeightColumn));
			if (weight < 0) {
				LogStream.WARNING.send(
						"Edge between %s and %s has negative weight (%d), which is not handled well by this algorithm's display code.", 
						left, right, weight);
			}
		} else {
			weight = 1;
		}
		
		edges.add(new Edge(left, right, weight));
	}


	public ImmutableList<Edge> getEdges() {
		return ImmutableList.copyOf(edges);
	}

	public String getWeightColumn() {
		return this.edgeWeightColumn;
	}
}
