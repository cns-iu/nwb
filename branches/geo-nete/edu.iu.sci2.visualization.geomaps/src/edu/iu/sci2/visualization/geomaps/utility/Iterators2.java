package edu.iu.sci2.visualization.geomaps.utility;

import java.util.Iterator;

import com.google.common.base.Equivalence;
import com.google.common.collect.AbstractIterator;

public class Iterators2 {
	private Iterators2() {}

	
	/* TODO Test */
	/**
	 * An iterator that for a series of consecutive, equivalent elements yields only the first.
	 * 
	 * <p>The source iterator is not currently advanced except as necessary,
	 * but this behavior is not guaranteed.
	 */
	public static <E> Iterator<E> omitConsecutiveDuplicates(
			final Iterator<? extends E> source, final Equivalence<? super E> equivalence) {
		/* We could implement this a bit more cleanly using PeekingIterator, but if we were to look
		 * forward rather than backward then I am not sure that there would be any way to avoid
		 * advancing the source iterator earlier than necessary.
		 */
		return new AbstractIterator<E>() {
			private boolean beforeYieldingFirstElement = true;
			private E previousElement;		
			
			@Override
			protected E computeNext() {
				while (source.hasNext()) {
					E currentElement = source.next();
					
					// Skip consecutively subsequent equivalent elements
					if (beforeYieldingFirstElement ||
							!equivalence.equivalent(currentElement, previousElement)) {
						beforeYieldingFirstElement = false;
						previousElement = currentElement;
						
						return currentElement;
					}
				}
				
				return endOfData();
			}
		};
	}
}
