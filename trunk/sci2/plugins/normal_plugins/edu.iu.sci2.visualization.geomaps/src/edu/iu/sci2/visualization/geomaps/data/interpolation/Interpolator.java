package edu.iu.sci2.visualization.geomaps.data.interpolation;

import com.google.common.base.Function;
import com.google.common.collect.Range;

import edu.iu.sci2.visualization.geomaps.utility.Continuum;

/**
 * A function that proportionally maps values in some fixed numerical input {@link Range} onto
 * values in some {@link Continuum} of the output type.
 * 
 * @param <U>
 *            The o<strong>u</strong>tput type. Not necessarily {@link Comparable}!
 */
public interface Interpolator<U> extends Function<Double, U> {
	/**
	 * The output value corresponding to this input.
	 */
	@Override
	U apply(Double input);

	/**
	 * The numerical input {@link Range} over which this interpolator can produce values in the out
	 * {@link Continuum}.
	 */
	Range<Double> getInRange();

	/**
	 * The continuum of values that this interpreter may produce.
	 */
	Continuum<U> getOutContinuum();
}
