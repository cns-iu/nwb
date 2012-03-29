package edu.iu.nwb.visualization.roundrussell.interpolation;

import edu.iu.nwb.visualization.roundrussell.utility.Range;

/*
 * The class formerly known as ZeroLengthInterpolatorInputRangeException
 * Renamed because names with more than 50(?) characters get truncated 
 * when unzipped with some windows zip utility.
 */
public class InputRangeException extends Exception {
	private static final long serialVersionUID = 1L;

	public InputRangeException(Range<Double> inRange) {
		super(createMessage(inRange));
	}

	private static String createMessage(Range<Double> inRange) {
		return "An interpolator's input range must have nonzero length "
				+ "(that is, unequal extrema), but we have "
				+ "minimum = " + inRange.getMin() + " and "
				+ "maximum = " + inRange.getMax();
	}
}
