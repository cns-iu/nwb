package edu.iu.cns.database.merge.generic.prepare.marked.grouping;

import prefuse.data.Tuple;

/**
 * Determines whether to merge two tuples. 
 */
public interface MergeCheck {
	/**
	 * Should these two tuples be merged?
	 * <p/> 
	 * Conceptually this method should be reflexive, but the current implementation of
	 * {@link CheckBasedGroupingStrategy} does not check tuples against themselves.
	 * <p/>
	 * This method will be interpreted symmetrically by the current implementation of
	 * {@link CheckBasedGroupingStrategy}.
	 * <p/>
	 * This method need not necessarily be transitive.
	 */
	boolean shouldMerge(Tuple left, Tuple right);
}
