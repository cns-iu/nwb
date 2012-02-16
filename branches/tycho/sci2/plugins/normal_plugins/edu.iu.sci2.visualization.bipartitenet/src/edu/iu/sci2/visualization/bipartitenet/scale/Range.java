package edu.iu.sci2.visualization.bipartitenet.scale;

import com.google.common.base.Preconditions;
import com.google.common.collect.Ordering;
/**
 * Keeps track of the maximum and minimum values that it has seen.
 * <p>
 * Calls to {@code getMin} or {@code getMax} will fail with {@code IllegalStateException} 
 * if no non-null data points have been added.
 * 
 * 
 * @author thgsmith
 *
 * @param <T> the type of values that it will be considering
 */
public class Range<T> {
	private final Ordering<? super T> ordering;

	private T min = null;
	private T max = null;
	
	private Range(Ordering<? super T> ordering) {
		this.ordering = ordering;
	}
	
	public static <T> Range<T> copyOf(Range<T> inRange) {
		Range<T> newRange = new Range<T>(inRange.ordering);
		// not using accessors because it's ok to copy a range that hasn't been trained
		// (if you use accessors and it hasn't consider()'d anything, IllegalStateException)
		newRange.min = inRange.min;
		newRange.max = inRange.max;
		return newRange;
	}
	
	/**
	 * If you are using a type that implements Comparable,
	 * you can use this factory method to create a Range.
	 * @return a Range that will compare your values using {@link Ordering#natural()}
	 */
	public static <T extends Comparable<? super T>> Range<T> create() {
		return createWithOrdering(Ordering.<T>natural());
	}
	
	/**
	 * For creating a Range of something that doesn't implement
	 * Comparable, use this one.
	 * 
	 * @see Ordering#from(java.util.Comparator)
	 */
	public static <T> Range<T> createWithOrdering(Ordering<T> ordering) {
		return new Range<T>(ordering);
	}
	
	public void consider(T item) {
		min = ordering.nullsLast().min(min, item);
		max = ordering.nullsFirst().max(max, item);
	}
	
	public void considerAll(Iterable<? extends T> items) {
		for (T item : items) {
			consider(item);
		}
	}
	
	public T getMin() {
		Preconditions.checkState(min != null, 
				"Range is invalid because no non-null items have been submitted");
		return min;
	}
	
	public T getMax() {
		Preconditions.checkState(min != null, 
				"Range is invalid because no non-null items have been submitted");
		return max;
	}
}
