package edu.iu.sci2.visualization.geomaps.utility;

import java.util.Arrays;
import java.util.NoSuchElementException;

import com.google.common.base.Objects;
import com.google.common.collect.ObjectArrays;
import com.google.common.collect.Ordering;

/**
 * @param <T>	Not necessarily a Comparable type, but it should be a type such that between any
 * 				two values there is conceptually a continuum of intermediate values of the same type
 * 				(e.g. Double, Color).
 */
public class Range<T> {
	private final Pair<T> pair;

	private Range(Pair<T> pair) {
		this.pair = pair;
	}
	public static <T> Range<T> between(T pointA, T pointB) {
		return new Range<T>(Pair.of(pointA, pointB));
	}
	/**
	 * @return	The min and max of values present in {@code iterable} according to its element
	 * 			type's natural comparator.
	 * @throw	{@link NoSuchElementException}	if {@code iterable} is empty
	 */
	public static <C extends Comparable<? super C>> Range<C> over(Iterable<? extends C> iterable) {
		return between(Ordering.natural().min(iterable), Ordering.natural().max(iterable));
	}
	public static <C extends Comparable<? super C>> Range<C> over(C first, C... rest) {
		return over(Arrays.asList(ObjectArrays.concat(first, rest)));
	}
	
	public T getPointA() {
		return pair.getLeft();
	}

	public T getPointB() {
		return pair.getRight();
	}

	public boolean isEmpty() {
		return pair.arePartsEqual();
	}

	@Override
	public boolean equals(Object thatObject) {
		if (!(thatObject instanceof Range<?>)) {
			return false;
		}		
		Range<?> that = (Range<?>) thatObject;
		
		return Objects.equal(this.pair, that.pair);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(pair);
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this)
				.add("pair", pair)
				.toString();
	}
}
