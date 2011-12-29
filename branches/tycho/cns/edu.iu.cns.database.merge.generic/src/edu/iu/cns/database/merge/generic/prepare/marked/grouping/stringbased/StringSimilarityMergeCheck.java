package edu.iu.cns.database.merge.generic.prepare.marked.grouping.stringbased;

import prefuse.data.Tuple;
import uk.ac.shef.wit.simmetrics.similaritymetrics.InterfaceStringMetric;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Objects;

import edu.iu.cns.database.merge.generic.prepare.marked.grouping.MergeCheck;

/**
 * A {@link MergeCheck} determined by the similarity of strings taken from some column of the
 * tuples. 
 */
public class StringSimilarityMergeCheck implements MergeCheck {
	private final String columnName;
	private final Function<String, String> function;
	private final InterfaceStringMetric metric;
	private final float threshold;

	/**
	 * See {@link #StringSimilarityMergeCheck(String, Function, InterfaceStringMetric, float)}
	 * where the {@link Function} does nothing. 
	 */
	public StringSimilarityMergeCheck(
			final String columnName,
			final InterfaceStringMetric metric,
			final float threshold) {
		this(columnName, Functions.<String>identity(), metric, threshold);
	}
	
	/**
	 * @param columnName	Column name containing raw strings to compare.
	 * @param function		Function to apply to raw strings before calculating similarity.
	 * @param metric		Similarity metric, see {@link StringMetric#METRICS}.
	 * @param threshold		Metric value must meet or exceed this for
	 * 						{@link #shouldMerge(Tuple, Tuple)} to return {@code true}. 
	 */
	public StringSimilarityMergeCheck(
			final String columnName,
			final Function<String, String> function,
			final InterfaceStringMetric metric,
			final float threshold) {
		this.columnName = columnName;
		this.function = function;
		this.metric = metric;
		this.threshold = threshold;
	}

	/**
	 * Calculates the similarity of strings in the {@code columnName} column after applying
	 * {@code function}.
	 * 
	 * @return	{@code true} if and only if the calculated similarity meets or exceeds
	 * 			{@code threshold}.
	 */
	public boolean shouldMerge(Tuple leftTuple, Tuple rightTuple) {
		final String leftRawValue = leftTuple.getString(columnName);
		final String rightRawValue = rightTuple.getString(columnName);
		
		final String leftValue = function.apply(leftRawValue);
		final String rightValue = function.apply(rightRawValue);
		
		final float similarity = metric.getSimilarity(leftValue, rightValue);
		
		return similarity >= threshold;
	}
	
	@Override
	public String toString() {
		return Objects.toStringHelper(this)
						.add("columnName", columnName)
						.add("function", function)
						.add("metric", metric.getShortDescriptionString())
						.add("threshold", threshold)
						.toString();
	}
}