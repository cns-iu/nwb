package edu.iu.cns.database.merge.generic.prepare.marked.grouping;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import prefuse.data.Tuple;

import com.google.common.base.Objects;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Sets;

/**
 * A {@link GroupingStrategy} that uses MergeCheck to produce a sort of graph of mergeable Tuples,
 * then traverses that graph depth-first to produce groups of mergeable groups.
 */
public class CheckBasedGroupingStrategy implements GroupingStrategy {
	private final MergeCheck mergeCheck;
	
	public CheckBasedGroupingStrategy(MergeCheck mergeCheck) {
		this.mergeCheck = mergeCheck;
	}

	/**
	 * First creates a list of "edges" for each {@link Tuple} to each {@link MergeCheck}-determined
	 * related Tuple, then traverses the resulting graph depth-first to produce groups of mergeable
	 * groups.
	 */
	public ImmutableCollection<Collection<Tuple>> splitIntoGroups(Iterable<Tuple> tuples) {
		ListMultimap<Tuple, Tuple> tupleToNeighbors = determineNeighbors(tuples);
		ImmutableCollection<Collection<Tuple>> groups =
				groupByMergeCheckGraphTraversal(tuples, tupleToNeighbors);

		return groups;
	}
	
	private static <T> ImmutableCollection<Collection<T>> groupByMergeCheckGraphTraversal(
			Iterable<T> tuples, ListMultimap<T, T> neighbors) {
		Collection<Collection<T>> groups = Sets.newHashSet();
		
		/* "Visited" tuples have already been added to some group and are avoided subsequently. */
		Set<T> visited = Sets.newHashSet();
				
		/* Iterate over all tuples to make sure each sub-graph is visited. */
 		for (T tuple : tuples) {
 			if (visited.contains(tuple)) {
 				continue;
 			} else {
 				Collection<T> group = Sets.newHashSet();
 				
 				/* Depth-first exploration of the "MergeCheck graph".
 				 * Doesn't follow previously visited tuples.				
 				 */
 				Stack<T> toCheck = new Stack<T>();
 				toCheck.add(tuple); 				
 				while (toCheck.size() > 0) {
 					T candidate = toCheck.pop();
 					if (visited.contains(candidate)) {
 						continue;
 					} else {
 						visited.add(candidate);
 						group.add(candidate);
 						
 						toCheck.addAll(neighbors.get(candidate));
 					}
 				}
 				
 				groups.add(group);
 			}
 		}
 		
 		return ImmutableSet.copyOf(groups);
 	}
	
	private ListMultimap<Tuple, Tuple> determineNeighbors(Iterable<Tuple> tuples) {
 		ListMultimap<Tuple, Tuple> neighbors = ArrayListMultimap.create();
 		
 		// Copy as a list to get an iteration order that is consistent for both loops.
 		List<Tuple> tupleList = ImmutableList.copyOf(tuples);

		for (int ii = 0; ii < tupleList.size(); ii++) {
			Tuple first = tupleList.get(ii);
			
			for (int jj = ii + 1; jj < tupleList.size(); jj++) {
				Tuple second = tupleList.get(jj);
				
				if (mergeCheck.shouldMerge(first, second)) {
					// Forced symmetry
					neighbors.put(first, second);
					neighbors.put(second, first);
				}
			}
		}
 		
 		return neighbors;
 	}
	
	@Override
	public String toString() {
		return Objects.toStringHelper(this)
						.add("mergeCheck", mergeCheck)
						.toString();
	}
}
