package edu.iu.scipolicy.visualization.geomaps.interpolation;

import edu.iu.scipolicy.visualization.geomaps.utility.Range;

public class ZeroLengthInterpolatorInputRangeException extends Exception {
	private static final long serialVersionUID = 1L;

	public ZeroLengthInterpolatorInputRangeException(Range<Double> inRange) {
		super(createMessage(inRange));
	}

	private static String createMessage(Range<Double> inRange) {
		return "An interpolator's input range must have nonzero length "
				+ "(that is, unequal extrema), but we have "
				+ "minimum = " + inRange.getMin() + " and "
				+ "maximum = " + inRange.getMax();
	}
}
