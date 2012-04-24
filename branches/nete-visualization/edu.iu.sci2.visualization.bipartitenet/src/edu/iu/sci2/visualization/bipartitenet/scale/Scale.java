package edu.iu.sci2.visualization.bipartitenet.scale;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;


public interface Scale<I,O> extends Function<I,O> {
	/**
	 * Returns the minimum and maximum input values that have been submitted to {@code train(...)}.
	 * The ImmutableList is exactly two elements long.
	 * <p>
	 * This operation is not required to be supported, if the Scale completely ignores its input
	 * values.
	 * @return a 2-element list containing the minimum and maximum values.
	 * @throws IllegalStateException if the Scale has not been trained yet
	 * @throws UnsupportedOperationException if the Scale ignores its inputs
	 */
	ImmutableList<I> getExtrema();
	
	/**
	 * Calibrates the scale to the data.  All of the data that you will apply the Scale to should
	 * be included in the training data.  You can call {@code train(...)} several times, and each
	 * batch of data will be considered together with the others.  
	 * @param trainingData
	 * @throws IllegalStateException if it is called after {@link #doneTraining()}
	 */
	void train(Iterable<I> trainingData);
	
	/**
	 * Tells the Scale that the training data is done.  After this call, {@link #train(Iterable)}
	 * must throw an IllegalStateException if it is called again.  Furthermore, the results of
	 * {@code apply} must not change for a given input, after this call.
	 */
	void doneTraining();
}
