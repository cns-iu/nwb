package edu.iu.sci2.visualization.geomaps.data.interpolation;

import edu.iu.sci2.visualization.geomaps.utility.Range;

public class Interpolator1D implements Interpolator<Double> {
	private final Range<Double> inRange;
	private final Range<Double> outRange;

	private Interpolator1D(Range<Double> inRange, Range<Double> outRange) {
		assert (!outRange.isEmpty()); // TODO ?
		
		this.inRange = inRange;
		this.outRange = outRange;
	}
	public static Interpolator1D between(Range<Double> inRange, Range<Double> outRange) {
		return new Interpolator1D(inRange, outRange);
	}
	

	@Override
	public Double apply(Double value) {
		return interpolate(value, inRange, outRange);
	}
	
	private static double interpolate(
			double value, Range<Double> inRange, Range<Double> outRange) {
		return interpolate(value,
						   inRange.pointA().doubleValue(),
						   inRange.pointB().doubleValue(),
						   outRange.pointA().doubleValue(),
						   outRange.pointB().doubleValue());
	}

	private static double interpolate(
			double in, double inMin, double inMax, double outMin, double outMax) {
		if ((inMax - inMin) == 0) {
			return outMin; // TODO document this behavior
		}
		
		return (outMin + (((in - inMin) * (outMax - outMin)) / (inMax - inMin)));
	}

	@Override
	public Range<Double> inRange() {
		return inRange;
	}
	
	@Override
	public Range<Double> outRange() {
		return outRange;
	}
}
