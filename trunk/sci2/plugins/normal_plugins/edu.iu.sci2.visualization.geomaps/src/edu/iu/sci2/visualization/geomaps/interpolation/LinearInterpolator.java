package edu.iu.sci2.visualization.geomaps.interpolation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.iu.sci2.visualization.geomaps.utility.Range;

public class LinearInterpolator implements Interpolator<Double> {
	private Range<Double> inRange; /* Data range based on the data value */
	private Range<Double> outRange; /* Could be the circle size range or the color range */

	public LinearInterpolator(Collection<Double> inValues, Range<Double> outRange) {
		this(Range.calculateRange(inValues), outRange);		
	}
	
	public LinearInterpolator(Range<Double> inRange, Range<Double> outRange) {
		this.inRange = inRange;
		this.outRange = outRange;
	}
	
	@Override
	public List<Double> interpolate(List<Double> values) {
		List<Double> interpolatedValues = new ArrayList<Double>();
		
		for (Double value : values) {
			double interpolatedValue = interpolate(value);
			interpolatedValues.add(interpolatedValue);
		}

		return interpolatedValues;
	}
	
	@Override
	public Double interpolate(double value) {
		return interpolate(value, inRange, outRange);
	}
	
	private static double interpolate(double value,
							   Range<Double> inRange,
							   Range<Double> outRange) {
		return interpolate(value,
						   inRange.getMin(),
						   inRange.getMax(),
						   outRange.getMin(),
						   outRange.getMax());
	}

	private static double interpolate(double in,
									  double inMin,
									  double inMax,
									  double outMin,
									  double outMax) {
		if (inMax - inMin == 0) {
			return outMin;
		}
		
		return (outMin + (in - inMin) * (outMax - outMin) / (inMax - inMin));
	}

	@Override
	public double invert(Double value) {
		Interpolator<Double> inverseInterpolator = createInverse(this);
		
		return inverseInterpolator.interpolate(value);
	}
	
	/* The inverse of a linear interpolator is the linear interpolator formed
	 * by swapping the original's input and output ranges.
	 * 
	 * The inverse is ill-defined when the original output range has zero
	 * length, because this is the input range of the inverse interpolator and
	 * and an interpolator cannot have a zero-length input range (as this would
	 * cause a division by zero during interpolation).
	 */
	private static LinearInterpolator createInverse(LinearInterpolator interpolator) {
		Range<Double> inRange = interpolator.getInRange();
		Range<Double> outRange = interpolator.getOutRange();
		
		return new LinearInterpolator(outRange, inRange);
	}

	public Range<Double> getInRange() {
		return inRange;
	}
	
	public Range<Double> getOutRange() {
		return outRange;
	}
}
