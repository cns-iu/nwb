package edu.iu.sci2.visualization.bipartitenet.scale;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

/**
 * A linear scale for producing predictable output values from bounded input values.
 * <p>
 * The two parameters to the constructor are the bounds of the output values that you want.  For
 * instance, if you call {@code scale = new BasicZeroAnchoredScale(0, 10)}, your {@code scale} will
 * produce only numbers between 0 and 10 inclusive, if it is used correctly.
 * <p>
 * To get this behavior, the following restrictions must be met:
 * <ol>
 * <li>The input must not contain any negative values.</li>
 * <li>The Scale must be trained on at least the maximum input value that you will be applying the
 * Scale to (it should really be trained on all input data)</li>
 * </ol>
 *    
 * @author thgsmith
 *
 */
public class BasicZeroAnchoredScale implements Scale<Double, Double> {
	private final double resultForZero;
	private final double resultForMax;
	private final double intercept;
	
	private final Range<Double> dataRange = Range.create();
	
	private double slope;
	private boolean doneTraining = false;
	
	public BasicZeroAnchoredScale(double resultForZero, double resultForMax) {
		this.resultForZero = resultForZero;
		this.resultForMax = resultForMax;
		this.intercept = resultForZero;
	}
	
	@Override
	public void train(Iterable<Double> trainingData) {
		Preconditions.checkState(! doneTraining, "Tried to add more training data after done training!");
		dataRange.considerAll(trainingData);
		this.slope = (resultForMax - resultForZero) / (dataRange.getMax() - 0);
	}
	
	@Override
	public void doneTraining() {
		this.doneTraining = true;
	}

	@Override
	public Double apply(Double value) {
		return value * slope + intercept;
	}

	@Override
	public ImmutableList<Double> getExtrema() {
		return ImmutableList.of(dataRange.getMin(), dataRange.getMax());
	}
}