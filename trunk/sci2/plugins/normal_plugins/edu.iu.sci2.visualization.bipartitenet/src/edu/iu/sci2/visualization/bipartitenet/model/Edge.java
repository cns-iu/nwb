package edu.iu.sci2.visualization.bipartitenet.model;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

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
		Preconditions.checkNotNull(leftNode);
		Preconditions.checkNotNull(rightNode);
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

	/*
	 * Auto-generated hashCode and equals...
	 */

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((leftNode == null) ? 0 : leftNode.hashCode());
		result = prime * result
				+ ((rightNode == null) ? 0 : rightNode.hashCode());
		long temp;
		temp = Double.doubleToLongBits(weight);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Edge other = (Edge) obj;
		if (leftNode == null) {
			if (other.leftNode != null)
				return false;
		} else if (!leftNode.equals(other.leftNode))
			return false;
		if (rightNode == null) {
			if (other.rightNode != null)
				return false;
		} else if (!rightNode.equals(other.rightNode))
			return false;
		if (Double.doubleToLongBits(weight) != Double
				.doubleToLongBits(other.weight))
			return false;
		return true;
	}
	
}
