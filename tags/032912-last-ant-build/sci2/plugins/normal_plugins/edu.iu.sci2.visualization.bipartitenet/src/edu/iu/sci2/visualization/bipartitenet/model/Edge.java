package edu.iu.sci2.visualization.bipartitenet.model;

import com.google.common.base.Function;
import com.google.common.base.Objects;

public class Edge {
	public static final Function<Edge,Double> WEIGHT_GETTER = new Function<Edge,Double>(){
		@Override
		public Double apply(Edge it) {
			return it.getWeight();
		}
	};
	private final Node leftNode;
	private final Node rightNode;
	private final double weight;
	
	public Edge(Node leftNode, Node rightNode, double weight) {
		super();
		this.leftNode = leftNode;
		this.rightNode = rightNode;
		this.weight = weight;
	}
	
	
	public Node getLeftNode() {
		return leftNode;
	}
	public Node getRightNode() {
		return rightNode;
	}
	public double getWeight() {
		return weight;
	}
	
	@Override
	public String toString() {
		return Objects.toStringHelper(this)
				.addValue(leftNode.getLabel())
				.addValue(rightNode.getLabel())
				.add("weight", weight)
				.toString();
	}
	
}
