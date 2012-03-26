package edu.iu.sci2.visualization.geomaps.utility;

import java.util.Iterator;
import java.util.List;

import com.google.common.base.Equivalence;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class Iterables2 {
	private Iterables2() {}
	
	/**
	 * @see Iterators2#omitConsecutiveDuplicates(Iterator, Equivalence)
	 */
	public static <E> Iterable<E> omitConsecutiveDuplicates(
			final Iterable<? extends E> iterable, final Equivalence<? super E> equivalence) {
		return new Iterable<E>() {
			@Override
			public Iterator<E> iterator() {
				return Iterators2.omitConsecutiveDuplicates(iterable.iterator(), equivalence);
			}			
		};
	}

	/**
	 * Split {@code toSplit} into a List of Lists of consecutively equivalent elements.
	 * 
	 * <p>For example, given {@link Equivalence#equals(Object)} this would split
	 * {@code [1, 1, 2, 2, 2, 1, 3, 3]} into {@code [[1, 1], [2, 2, 2], [1], [3, 3]]}.
	 */
	public static <E> List<List<E>> split(
			Iterable<? extends E> toSplit, Equivalence<? super E> equivalence) {
		List<List<E>> split = Lists.newArrayList();
	
		if (Iterables.isEmpty(toSplit)) {
			return split;
		} 		
		// Above check should prevent use of this null.
		E firstElement = Iterables.getFirst(toSplit, null);
		
		List<E> currentPart = Lists.newArrayList(ImmutableList.of(firstElement));		
		E previousElement = firstElement;
		
		for (E element : Iterables.skip(toSplit, 1)) {			
			if (!equivalence.equivalent(element, previousElement)) {
				split.add(currentPart);
				currentPart = Lists.newArrayList();
			}
			
			currentPart.add(element);
			
			previousElement = element;
		}
		
		split.add(currentPart);
		
		return split;
	}
}
