package edu.iu.sci2.visualization.geomaps.data.interpolation;

import com.google.common.base.Function;

import edu.iu.sci2.visualization.geomaps.utility.Range;

public interface Interpolator<U> extends Function<Double, U> {
	Range<Double> getInRange();
	Range<U> getOutRange();
}
