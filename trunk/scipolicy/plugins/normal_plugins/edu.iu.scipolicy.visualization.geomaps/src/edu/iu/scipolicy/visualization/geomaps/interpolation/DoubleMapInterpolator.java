package edu.iu.scipolicy.visualization.geomaps.interpolation;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.cishell.framework.algorithm.AlgorithmExecutionException;

/* For a Map<K, Double>, creates a new Map<K, T> where each entry value
 * is interpolated by the given interpolator.
 */
public class DoubleMapInterpolator<K, T> {
	private DoubleInterpolator<T> doubleInterpolator;

	public DoubleMapInterpolator(DoubleInterpolator<T> interpolator) {
		this.doubleInterpolator = interpolator;
	}

	public Map<K, T> getInterpolatedMap(Map<K, Double> valueMap) throws AlgorithmExecutionException {
		Map<K, T> interpolatedMap = new HashMap<K, T>();
		for ( Entry<K, Double> valueMapEntry : valueMap.entrySet() ) {
			K coordinate = valueMapEntry.getKey();
			Double value = valueMapEntry.getValue();
			T interpolatedValue = doubleInterpolator.interpolate(value);

			interpolatedMap.put(coordinate, interpolatedValue);
		}

		return interpolatedMap;
	}
}