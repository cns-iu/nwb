package edu.iu.sci2.visualization.bipartitenet.model;

import com.google.common.collect.Ordering;

import edu.iu.sci2.visualization.bipartitenet.algorithm.NodeOrderingOption;

// TODO some kind of connection with NodeDestination?
// TODO doc variables
public class NodeType {
	private final String name;
	private final String displayName;
	private final NodeOrderingOption orderingOption;
	private final String weightColumn;
	
	public static NodeType create(String name, String displayName,
			NodeOrderingOption orderingOption, String weightColumn) {
		return new NodeType(name, displayName, orderingOption, weightColumn);
	}
	
	public static NodeType createWithDefaultDisplayName(String name, 
			NodeOrderingOption orderingOption, String weightColumn) {
		return new NodeType(name, name, orderingOption, weightColumn);
	}
	
	private NodeType(String name, String displayName, NodeOrderingOption orderingOption, String weightColumn) {
		this.name = name;
		this.displayName = displayName;
		this.orderingOption = orderingOption;
		this.weightColumn = weightColumn;
	}
	
	public String getName() {
		return name;
	}
	public String getDisplayName() {
		return displayName;
	}
	public Ordering<Node> getOrdering() {
		return orderingOption.getOrdering();
	}
	public String getOrderingIdentifier() {
		return orderingOption.getIdentifier();
	}
	public String getWeightColumn() {
		return weightColumn;
	}

	public String getShortIdentifier() {
		return orderingOption.getShortIdentifier();
	}
}
