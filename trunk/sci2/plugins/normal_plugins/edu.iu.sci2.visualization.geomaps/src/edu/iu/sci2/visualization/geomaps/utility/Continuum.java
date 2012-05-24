package edu.iu.sci2.visualization.geomaps.utility;

import java.util.Arrays;
import java.util.NoSuchElementException;

import com.google.common.base.Objects;
import com.google.common.collect.ObjectArrays;
import com.google.common.collect.Ordering;
import com.google.common.collect.Range;

/* TODO Might like to replace this in favor of com.google.common.collect.Range.
 * To do so we will need to parameterize only on Comparable types.
 * To do that we will need to stop talking of a Continuum<Color> and instead use some Comparable
 * proxy that is interpreted externally as a Color.
 */
/**
 * @param <T>	Not necessarily a Comparable type, but one such that between any two values there
 * 				is conceptually a continuum of intermediate values of the same type
 * 				(e.g. Double, Color).  If T is Comparable consider using {@link Range}.
 */
public class Continuum<T> {
	private final T pointA;
	private final T pointB;

	private Continuum(T pointA, T pointB) {
		this.pointA = pointA;
		this.pointB = pointB;
	}
	public static <T> Continuum<T> between(T pointA, T pointB) {
		return new Continuum<T>(pointA, pointB);
	}
	/**
	 * @return The min and max of values present in {@code iterable} according to its element
	 *         type's natural comparator.
	 * @throws NoSuchElementException
	 *             If {@code iterable} is empty
	 */
	public static <C extends Comparable<? super C>> Continuum<C> over(Iterable<? extends C> iterable) {
		return between(Ordering.natural().min(iterable), Ordering.natural().max(iterable));
	}
	public static <C extends Comparable<? super C>> Continuum<C> over(C first, C... rest) {
		return over(Arrays.asList(ObjectArrays.concat(first, rest)));
	}
	public static <C extends Comparable<? super C>> Continuum<C> fromRange(Range<C> range) {
		return between(range.lowerEndpoint(), range.upperEndpoint());
	}
	
	
	public T getPointA() {
		return pointA;
	}

	public T getPointB() {
		return pointB;
	}

	public boolean isEmpty() {
		return Objects.equal(pointA, pointB);
	}
	

	@Override
	public boolean equals(Object thatObject) {
		if (this == thatObject) { return true; }
		if (thatObject == null) { return false; }
		if (!(thatObject instanceof Continuum<?>)) { return false; }		
		Continuum<?> that = (Continuum<?>) thatObject;
		
		return Objects.equal(this.pointA, that.pointA) &&
			   Objects.equal(this.pointB, that.pointB);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(pointA, pointB);
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this)
				.add("pointA", pointA)
				.add("pointB", pointB)
				.toString();
	}
}
