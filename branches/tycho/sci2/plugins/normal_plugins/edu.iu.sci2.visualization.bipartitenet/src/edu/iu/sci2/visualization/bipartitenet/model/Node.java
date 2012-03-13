package edu.iu.sci2.visualization.bipartitenet.model;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Ordering;

public class Node {
	public static final Function<Node,Double> WEIGHT_GETTER = new Function<Node,Double>() {
		@Override
		public Double apply(Node it) {
			return it.getWeight();
		}
	};
	public static final Function<Node,String> LABEL_GETTER = new Function<Node,String>() {
		@Override
		public String apply(Node it) {
			return it.getLabel();
		}
	};
	public static final Ordering<Node> WEIGHT_ORDERING = Ordering.natural().onResultOf(WEIGHT_GETTER);
	public static final Ordering<Node> LABEL_ORDERING = Ordering.natural().onResultOf(LABEL_GETTER);
	
	
	private final String label;
	private final double weight;
	private final NodeDestination destination;


	public Node(String label, double weight, NodeDestination destination) {
		Preconditions.checkNotNull(label);
		Preconditions.checkNotNull(destination);
		this.destination = destination;
		this.label = label;
		this.weight = weight;
	}

	public String getLabel() {
		return label;
	}

	public double getWeight() {
		return weight;
	}

	public NodeDestination getDestination() {
		return destination;
	}
	
	@Override
	public String toString() {
		return Objects.toStringHelper(this)
				.add("label", label)
				.add("weight", weight)
				.add("side", destination)
				.toString();
	}
}
