package edu.iu.scipolicy.visualization.geomaps.scaling;

import java.util.ArrayList;
import java.util.List;

import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.osgi.service.log.LogService;

import edu.iu.scipolicy.visualization.geomaps.GeoMapsAlgorithm;
import edu.iu.scipolicy.visualization.geomaps.utility.Range;

public class ListScaler {
	private Scaler scaler;
	private double scalableMin;
	private double scalableMax;

	public ListScaler(Scaler scaler) {
		this.scaler = scaler;
		
		this.scalableMin = Double.POSITIVE_INFINITY;
		this.scalableMax = Double.NEGATIVE_INFINITY;
	}
	
	public List<Double> scale(List<Double> values) throws AlgorithmExecutionException {
		List<Double> scaledValues = new ArrayList<Double>();
		
		int unscalableValueCount = 0;

		this.scalableMin = Double.POSITIVE_INFINITY;
		this.scalableMax = Double.NEGATIVE_INFINITY;

		for ( Double rawValue : values) {

			if (scaler.canScale(rawValue)) {
				if (rawValue < scalableMin) {
					scalableMin = rawValue;
				}
				if (rawValue > scalableMax) {
					scalableMax = rawValue;
				}

				scaledValues.add(scaler.scale(rawValue));
			}
			else {
				unscalableValueCount++;
			}
		}

		if (unscalableValueCount > 0) {
			GeoMapsAlgorithm.logger.log(LogService.LOG_WARNING, unscalableValueCount + " values were ignored because: "	+ scaler.getUnscalableMessage());
		}
		
		return scaledValues;
	}
	
	public Range<Double> getScalableRange() {
		return new Range<Double>(scalableMin, scalableMax);
	}
}
