package edu.iu.scipolicy.visualization.geomaps.interpolation;

import java.util.ArrayList;
import java.util.List;

import org.cishell.framework.algorithm.AlgorithmExecutionException;

public class ListInterpolator<T> {
	private Interpolator<T> interpolator;

	public ListInterpolator(Interpolator<T> interpolator) {
		this.interpolator = interpolator;
	}

	public List<T> getInterpolatedList(List<Double> values) throws AlgorithmExecutionException {
		List<T> interpolatedValues = new ArrayList<T>();
		
		for ( Double value : values ) {
			T interpolatedValue = interpolator.interpolate(value);

			interpolatedValues.add(interpolatedValue);
		}

		return interpolatedValues;
	}
}