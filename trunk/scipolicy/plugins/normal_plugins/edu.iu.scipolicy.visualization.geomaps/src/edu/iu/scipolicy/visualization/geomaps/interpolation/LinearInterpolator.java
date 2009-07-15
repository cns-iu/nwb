package edu.iu.scipolicy.visualization.geomaps.interpolation;

import java.util.Collection;

import org.cishell.framework.algorithm.AlgorithmExecutionException;

import edu.iu.scipolicy.visualization.geomaps.utility.Range;

public class LinearInterpolator implements Interpolator<Double> {
	private Collection<Double> values;
	private double minimumValue;
	private double maximumValue;
	private Range<Double> interpolatedRange;

	public LinearInterpolator(Collection<Double> values, Range<Double> interpolatedRange) {
		this.values = values;
		this.interpolatedRange = interpolatedRange;

		minimumValue = Double.POSITIVE_INFINITY;
		maximumValue = Double.NEGATIVE_INFINITY;
		for ( double value : values ) {
			if ( value < minimumValue ) {
				minimumValue = value;
			}
			if ( value > maximumValue) {
				maximumValue = value;
			}
		}
		assert( minimumValue < Double.POSITIVE_INFINITY );
		assert( maximumValue > Double.NEGATIVE_INFINITY );
	}

	public Double interpolate(double value) throws AlgorithmExecutionException {
		if ( values.isEmpty() ) {
			throw new AlgorithmExecutionException("Cannot interpolate over an empty set.");
		}
		// TODO Ordering of the next two branches is best?
		else if ( values.size() == 1 ) {
			// TODO: Favor the maximum or..?
			return interpolatedRange.getMax();
		}
		else if ( minimumValue == maximumValue ) {
			throw new AlgorithmExecutionException("Cannot interpolate when input data has zero variance (equal minimum and maximum).");
		}		
		else {
			return interpolate(value, minimumValue, maximumValue, interpolatedRange.getMin(), interpolatedRange.getMax());
		}
	}

	private double interpolate(double x, double xMin, double xMax, double yMin, double yMax) {
		return (yMin + ( x - xMin ) * ( yMax - yMin ) / ( xMax - xMin ));
	}
}
