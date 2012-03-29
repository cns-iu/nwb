package edu.iu.cns.database.merge.generic.prepare.marked.grouping;

import java.util.Collection;

import prefuse.data.Tuple;

import com.google.common.collect.ImmutableCollection;

/**
 * A strategy for partitioning a collection of tuples into mergeable groups.
 */
public interface GroupingStrategy {
	ImmutableCollection<Collection<Tuple>> splitIntoGroups(Iterable<Tuple> tuples);
}
