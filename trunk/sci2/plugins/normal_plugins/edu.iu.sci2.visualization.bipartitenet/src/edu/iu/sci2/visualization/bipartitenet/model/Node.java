package edu.iu.sci2.visualization.bipartitenet.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	/**
	 * Guaranteed to return a Double or null (i.e. not a NumberFormatException)
	 * <p>
	 * The function may throw a NullPointerException if the given String is
	 * null.
	 */
	static final Function<String,Double> EXTRACT_DOUBLE_VALUE = new Function<String,Double>() {
		/*
		 * Source:
		 * http://www.regular-expressions.info/floatingpoint.html
		 */
		private final Pattern FLOATING_POINT_NUMBER = Pattern.compile("[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?");
		@Override
		public Double apply(String label) {
			Matcher m = FLOATING_POINT_NUMBER.matcher(label);
			if (! m.find()) {
				return null;
			} else {
				try {
					return Double.valueOf(m.group(0));
				} catch (NumberFormatException e) {
					return null;
				}
			}
		}
	};
	
	public static final Ordering<Node> WEIGHT_ORDERING = Ordering.natural().onResultOf(WEIGHT_GETTER);
	public static final Ordering<Node> LABEL_ORDERING = Ordering.natural().onResultOf(LABEL_GETTER);
	/**
	 * An {@link Ordering} of Strings that attempts to order numerically, and then falls back to
	 * String comparison if numbers are not found.  Strings with numbers in them are sorted before
	 * Strings without numbers.
	 */
	public static final Ordering<String> NUMERIC_STRING_ORDERING = 
			Ordering.natural()
				.nullsLast()
				.onResultOf(EXTRACT_DOUBLE_VALUE)
			.compound(Ordering.natural());
	
	public static final Ordering<Node> NUMERIC_LABEL_ORDERING = NUMERIC_STRING_ORDERING
			.onResultOf(LABEL_GETTER);
	
	
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

	/*
	 * Auto-generated hashCode and equals...
	 */
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((destination == null) ? 0 : destination.hashCode());
		result = prime * result + ((label == null) ? 0 : label.hashCode());
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
		Node other = (Node) obj;
		if (destination != other.destination)
			return false;
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		if (Double.doubleToLongBits(weight) != Double
				.doubleToLongBits(other.weight))
			return false;
		return true;
	}
	
}
