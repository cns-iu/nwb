package edu.iu.cns.database.merge.generic.prepare.marked.grouping;

import java.util.Collection;

import prefuse.data.Tuple;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

/**
 * A {@link GroupingStrategy} composed of one or more sub-strategies.  Each strategy will subdivide
 * the groups produced by the preceding strategy or strategies.
 * <p/>
 * When {@link CompoundGroupingStrategy#splitIntoGroups(Iterable)} is called on a 
 * CompoundGroupingStrategy, the first component GroupingStrategy is used to divide the input into
 * groups, by calling its {@link #splitIntoGroups(Iterable)} method.  Then, the second
 * GroupingStrategy is used on each group, giving it an opportunity to subdivide the results of the
 * first GroupingStrategy. This continues for the third, fourth, etc., if supplied.
 *  
 *  
 */
public class CompoundGroupingStrategy implements GroupingStrategy {
	private final GroupingStrategy firstStrategy;
	private final GroupingStrategy secondStrategy;
	
	private CompoundGroupingStrategy(
			GroupingStrategy firstStrategy, GroupingStrategy secondStrategy) {
		this.firstStrategy = firstStrategy;
		this.secondStrategy = secondStrategy;
	}
	
	/**
	 * Compound one or more grouping strategies.  Each strategy will subdivide the groups
	 * determined by the preceding strategy.
	 * @param first		The first strategy to apply.
	 * @param rest		All subsequent subdividing strategies in the order to be applied.
	 * @return			A compound grouping strategy.
	 */
	public static GroupingStrategy compound(GroupingStrategy first, GroupingStrategy... rest) {		
		GroupingStrategy compound = first;
		
		for (GroupingStrategy strategy : rest) {
			compound = new CompoundGroupingStrategy(compound, strategy);
		}

		return compound;
	}

	/**
	 * Iteratively divide and subdivide {@code tuples} into groups using
	 * the individual strategies that make up this compound strategy.
	 */
	public ImmutableCollection<Collection<Tuple>> splitIntoGroups(Iterable<Tuple> tuples) {
		ImmutableCollection<Collection<Tuple>> firstGroups =
				this.firstStrategy.splitIntoGroups(tuples);

		Collection<Collection<Tuple>> secondGroups = Sets.newHashSet();
		for (Collection<Tuple> firstGroup : firstGroups) {
			secondGroups.addAll(this.secondStrategy.splitIntoGroups(firstGroup));
		}

		return ImmutableSet.copyOf(secondGroups);
	}
	
	@Override
	public String toString() {
		return Objects.toStringHelper(this)
						.add("firstStrategy", this.firstStrategy)
						.add("secondStrategy", this.secondStrategy)
						.toString();
	}
}
