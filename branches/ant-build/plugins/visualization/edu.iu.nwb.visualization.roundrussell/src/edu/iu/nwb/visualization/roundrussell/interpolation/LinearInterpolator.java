package edu.iu.nwb.visualization.roundrussell.interpolation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.iu.nwb.visualization.roundrussell.utility.Range;

public class LinearInterpolator implements Interpolator<Double> {
	private Range<Double> inRange;
	private Range<Double> outRange;

	public LinearInterpolator(Collection<Double> inValues, Range<Double> outRange)
			throws InputRangeException {
		this(Range.calculateRange(inValues), outRange);
	}
	
	public LinearInterpolator(Range<Double> inRange, Range<Double> outRange)
				throws InputRangeException {
		if (!inRange.getMin().equals(inRange.getMax())) {
			this.inRange = inRange;
			this.outRange = outRange;
		} else {
			throw new InputRangeException(inRange);
		}
	}
	
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
		if (inMax - inMin == 0.0) {
			throw new RuntimeException(
					"Unexpected error: "
					+ "Trying to interpolate from an input range with zero length.");
		} else {
			return (outMin + (in - inMin) * (outMax - outMin) / (inMax - inMin));
		}
	}

	public double invert(Double value) throws InterpolatorInversionException {
		Interpolator<Double> inverseInterpolator = createInverse(this);
		
		return inverseInterpolator.interpolate(value);
	}
	
	/* The inverse of a linear interpolator is the linear interpolator formed
	 * by swapping the original's input and output ranges.
	 * 
	 * The inverse is ill-defined when the original output range has zero
	 * length, because this is the input range of the inverse interpolator and
	 * and interpolator cannot have a zero-length input range (as this would
	 * cause a division by zero during interpolation).
	 */
	private static LinearInterpolator createInverse(LinearInterpolator interpolator) 
		throws InterpolatorInversionException {
		try {
			Range<Double> inRange = interpolator.getInRange();
			Range<Double> outRange = interpolator.getOutRange();
			
			return new LinearInterpolator(outRange, inRange);
		} catch (InputRangeException e) {
			throw new InterpolatorInversionException(e);
		}
	}

	public Range<Double> getInRange() {
		return inRange;
	}
	
	public Range<Double> getOutRange() {
		return outRange;
	}

	public List<Double> interpolate(List<Double> values) {
		List<Double> interpolatedValues = new ArrayList<Double>();
		
		for (double value : values) {
			double interpolatedValue = interpolate(value);
			interpolatedValues.add(interpolatedValue);
		}

		return interpolatedValues;
	}
}
