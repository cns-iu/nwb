package edu.iu.sci2.visualization.bipartitenet.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.cishell.utilities.NumberUtilities;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;

import edu.iu.sci2.visualization.bipartitenet.LogStream;

public class NodeHandler implements NodeProvider {
	private  String nodeTypeCol;
	private  String nodeWeightCol;
	private  String typeForLeftSide;
	private final Map<Integer, Node> nodeById = Maps.newHashMap();
	private List<Node> leftNodes, rightNodes;
	private ImmutableMap<String,String> schema;
	private final Ordering<? super Node> leftSideSort;
	private final Ordering<? super Node> rightSideSort;

	// TODO switch to use NodeType rather than a bunch of Strings
	public NodeHandler(String nodeTypeCol, String nodeWeightCol,
			String typeForLeftSide, Ordering<? super Node> leftSideSort, Ordering<? super Node> rightSideSort) {
		this.nodeTypeCol = nodeTypeCol;
		this.nodeWeightCol = nodeWeightCol;
		this.typeForLeftSide = typeForLeftSide;
		this.leftSideSort = leftSideSort;
		this.rightSideSort = rightSideSort;
		
		this.leftNodes = Lists.newArrayList();
		this.rightNodes = Lists.newArrayList();
	}

	public void addNode(int id, String label, Map<String, ? extends Object> attributes) {
		double weight;			
		weight = getNodeWeight(attributes);
		if (weight < 0) {
			LogStream.WARNING.send(
					"Node '%s' has negative weight (%d), which is not handled well by this algorithm's display code.", label, weight);
		}
		
		String type = (String) attributes.get(nodeTypeCol);
		
		Node nodeObj = new Node(label, weight, getDestination(type));
		insert(id, nodeObj);
	}

	public double getNodeWeight(Map<String, ? extends Object> attributes) {
		if (nodeWeightCol != null) {
			return NumberUtilities.interpretObjectAsDouble(attributes.get(nodeWeightCol));
		} else {
			return 1.0;
		}
	}
	
	public void setNodeSchema(LinkedHashMap<String, String> schema) {

		/* The node weight column should come from a list of all node columns put together in
		 * mutateParameters. */
		Preconditions.checkArgument((nodeWeightCol == null) || (schema.containsKey(nodeWeightCol)));
		
		/* 
		 * The presence of this column should already have been checked during the 
		 * mutateParameters process.
		 */
		Preconditions.checkArgument(schema.containsKey(nodeTypeCol));
		
		this.schema = ImmutableMap.copyOf(schema);
	}

	private NodeDestination getDestination(String type) {
		if (typeForLeftSide.equalsIgnoreCase(type)) {
			return NodeDestination.LEFT;
		} else {
			return NodeDestination.RIGHT;
		}
	}

	private void insert(int id, Node nodeObj) {
		nodeById.put(id, nodeObj);
		if (nodeObj.getDestination() == NodeDestination.LEFT) {
			leftNodes.add(nodeObj);
		} else {
			rightNodes.add(nodeObj);
		}
	}

	public Node getNodeById(int id) {
		return nodeById.get(id);
	}

	public ImmutableList<Node> getLeftNodes() {
		return ImmutableList.copyOf(leftSideSort.sortedCopy(leftNodes));
	}

	public ImmutableList<Node> getRightNodes() {
		return ImmutableList.copyOf(rightSideSort.sortedCopy(rightNodes));
	}


	public ImmutableMap<String, String> getNodeSchema() {
		return this.schema;
	}

	public String getWeightColumn() {
		return nodeWeightCol;
	}
}
