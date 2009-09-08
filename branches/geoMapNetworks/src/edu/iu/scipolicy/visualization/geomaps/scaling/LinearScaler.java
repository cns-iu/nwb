package edu.iu.scipolicy.visualization.geomaps.scaling;

import org.cishell.framework.algorithm.AlgorithmExecutionException;

import edu.iu.scipolicy.visualization.geomaps.utility.Range;

public class LinearScaler implements Scaler {
	public boolean canScale(double value) {
		return true;
	}
	
	public double scale(double rawValue) {
		return rawValue;
	}
	public double invert(double value) {
		return value;
	}

	public String getUnscalableMessage() throws AlgorithmExecutionException {
		throw new AlgorithmExecutionException("Faulty warning: Linear scale should be defined for any double value.");
	}

	public Range<Double> scale(Range<Double> range) {
		double scaledMin = scale(range.getMin());
		double scaledMax = scale(range.getMax());
		
		if (scaledMin < scaledMax) {
			return new Range<Double>(scaledMin, scaledMax);
		} else {
			return new Range<Double>(scaledMax, scaledMin);
		}
	}	
}