package edu.iu.sci2.visualization.bipartitenet.algorithm;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Ordering;

import edu.iu.sci2.visualization.bipartitenet.model.Node;
/**
 * The options available to the user on the algorithm parameters screen, for ways to sort nodes.
 * <p>
 * This is an Enum (and not just a Map) because of the need to programmatically
 * access particular <b>keys</b>.
 * <p>
 * The identifiers are the labels the user will see on the algorithm parameters menu.
 *
 */
public enum NodeOrderingOption {
	LABEL_ASC("Label, ascending", "Alphabetical",
			Node.LABEL_ORDERING),

	LABEL_DESC("Label, descending", "Alphabetical",
			Node.LABEL_ORDERING.reverse()),


	LABEL_NUMERIC_ASC("Label, numeric, ascending", "Numeric",
			Node.NUMERIC_LABEL_ORDERING),

	LABEL_NUMERIC_DESC("Label, numeric, descending", "Numeric",
			Node.NUMERIC_LABEL_ORDERING.reverse()),


	WEIGHT_ASC("Weight, ascending", "Weight",
			Node.WEIGHT_ORDERING),

	WEIGHT_DESC("Weight, descending", "Weight",
			Node.WEIGHT_ORDERING.reverse());
	
	private final String identifier;
	private final Ordering<Node> ordering;
	private final String shortIdentifier;

	public String getShortIdentifier() {
		return shortIdentifier;
	}

	private NodeOrderingOption(String identifier, String shortIdentifier, Ordering<Node> ordering) {
		this.identifier = identifier;
		this.shortIdentifier = shortIdentifier;
		this.ordering = ordering;
	}
	
	private static final ImmutableBiMap<String, Ordering<Node>> ORDERINGS = constructOrderings();
	
	private static ImmutableBiMap<String, Ordering<Node>> constructOrderings() {
		ImmutableBiMap.Builder<String, Ordering<Node>> builder = ImmutableBiMap.builder();
		
		for (NodeOrderingOption entry : NodeOrderingOption.values()) {
			builder.put(entry.getIdentifier(), entry.getOrdering());
		}
		
		return builder.build();
	}
	
	public static ImmutableList<String> getIdentifiers() {
		return ImmutableList.copyOf(ORDERINGS.keySet());
	}

	/**
	 * @throws IllegalArgumentException
	 *             if there is no constant with the specified identifier
	 */
	public static Ordering<Node> getOrdering(String identifier) {
		Preconditions.checkArgument(ORDERINGS.containsKey(identifier));
		
		return ORDERINGS.get(identifier);
	}

	public static NodeOrderingOption getOption(String identifier) {
		for (NodeOrderingOption o: NodeOrderingOption.values()) {
			if (identifier.equals(o.getIdentifier())) {
				return o;
			}
		}
		throw new IllegalArgumentException("Identifier " + identifier + " does not match any NodeOrderingOption");
	}
	
	public String getIdentifier() {
		return identifier;
	}

	public Ordering<Node> getOrdering() {
		return this.ordering;
	}
}
