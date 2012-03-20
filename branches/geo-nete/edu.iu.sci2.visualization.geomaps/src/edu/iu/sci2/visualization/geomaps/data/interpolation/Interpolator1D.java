package edu.iu.sci2.visualization.geomaps.data.interpolation;

import com.google.common.collect.Range;

import edu.iu.sci2.visualization.geomaps.utility.Continuum;

public class Interpolator1D implements Interpolator<Double> {
	private final Range<Double> inRange;
	private final Continuum<Double> outContinuum;

	private Interpolator1D(Range<Double> inRange, Continuum<Double> outContinuum) {
		assert (!outContinuum.isEmpty()); // TODO ?
		
		this.inRange = inRange;
		this.outContinuum = outContinuum;
	}
	public static Interpolator1D between(Range<Double> inRange, Continuum<Double> outContinuum) {
		return new Interpolator1D(inRange, outContinuum);
	}
	

	@Override
	public Double apply(Double value) {
		return interpolate(value, inRange, outContinuum);
	}
	
	private static double interpolate(
			double value, Range<Double> inRange, Continuum<Double> outContinuum) {
		return interpolate(value,
						   inRange.lowerEndpoint().doubleValue(),
						   inRange.upperEndpoint().doubleValue(),
						   outContinuum.getPointA().doubleValue(),
						   outContinuum.getPointB().doubleValue());
	}

	private static double interpolate(
			double in, double inMin, double inMax, double outMin, double outMax) {
		if ((inMax - inMin) == 0) {
			return outMin; // TODO document this behavior
		}
		
		return (outMin + (((in - inMin) * (outMax - outMin)) / (inMax - inMin)));
	}

	@Override
	public Range<Double> getInRange() {
		return inRange;
	}
	
	@Override
	public Continuum<Double> getOutContinuum() {
		return outContinuum;
	}
}
