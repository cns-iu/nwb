package edu.iu.scipolicy.visualization.geomaps.scaling;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.osgi.service.log.LogService;

import edu.iu.scipolicy.visualization.geomaps.GeoMapsAlgorithm;
import edu.iu.scipolicy.visualization.geomaps.utility.Range;

/* For a Map<K, Double>, creates a new Map<K, Double> where each entry value
 * is scaled according the given scaler.
 * 
 * Keeps track of the minimum and maximum values that the given scaler was
 * capable of scaling.
 * 
 * Warns how many couldn't be scaled.
 */
public class DoubleMapScaler<K> {
	private DoubleScaler doubleScaler;
	private double scalableMin;
	private double scalableMax;

	public DoubleMapScaler(DoubleScaler doubleScaler) {
		this.doubleScaler = doubleScaler;

		this.scalableMin = Double.POSITIVE_INFINITY;
		this.scalableMax = Double.NEGATIVE_INFINITY;
	}

	public Map<K, Double> scale(Map<K, Double> quantityMap) throws AlgorithmExecutionException {
		Map<K, Double> scaledQuantityMap = new HashMap<K, Double>();

		int unscalableValueCount = 0;

		this.scalableMin = Double.POSITIVE_INFINITY;
		this.scalableMax = Double.NEGATIVE_INFINITY;

		for (Entry<K, Double> mapEntry : quantityMap.entrySet()) {
			K key = mapEntry.getKey();
			double rawValue = mapEntry.getValue();

			if (doubleScaler.isScalable(rawValue)) {
				if (rawValue < scalableMin) {
					scalableMin = rawValue;
				}
				if (rawValue > scalableMax) {
					scalableMax = rawValue;
				}

				scaledQuantityMap.put(key, scale(rawValue));
			}
			else {
				unscalableValueCount++;
			}
		}

		if (unscalableValueCount > 0) {
			GeoMapsAlgorithm.logger.log(LogService.LOG_WARNING, unscalableValueCount + " values were ignored because: "	+ doubleScaler.getUnscalableMessage());
		}

		return scaledQuantityMap;
	}

	public double scale(double rawValue) throws AlgorithmExecutionException {
		return doubleScaler.scale(rawValue);
	}

	public Range<Double> getScalableRange() {
		return new Range<Double>(scalableMin, scalableMax);
	}
}
