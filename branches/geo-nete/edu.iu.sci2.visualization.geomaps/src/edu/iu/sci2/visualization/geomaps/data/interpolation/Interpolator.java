package edu.iu.sci2.visualization.geomaps.data.interpolation;

import com.google.common.base.Function;
import com.google.common.collect.Range;

import edu.iu.sci2.visualization.geomaps.utility.Continuum;

public interface Interpolator<U> extends Function<Double, U> {
	Range<Double> getInRange();
	Continuum<U> getOutContinuum();
}
