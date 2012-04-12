package edu.iu.cns.database.merge.generic.prepare.marked.grouping;

import java.util.Collection;

import prefuse.data.Tuple;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.Multimaps;

/**
 * A {@link GroupingStrategy} for partitioning a collection of tuples into groups with shared keys.
 * 
 * @param <K>	The type of the keys produced by the KeyFunction.
 */
public class KeyBasedGroupingStrategy<K> implements GroupingStrategy {
	private final Function<Tuple, K> keyFunction;
	
	public KeyBasedGroupingStrategy(Function<Tuple, K> keyFunction) {
		this.keyFunction = keyFunction;
	}

	/** Divides {@code tuples} into groups with matching keys */
	public ImmutableCollection<Collection<Tuple>> splitIntoGroups(Iterable<Tuple> tuples) {
		// Map each key to the list of tuples having that key, then discard the keys.
		return Multimaps.index(tuples, this.keyFunction).asMap().values();
	}
	
	@Override
	public String toString() {
		return Objects.toStringHelper(this)
						.add("keyFunction", this.keyFunction)
						.toString();
	}
}