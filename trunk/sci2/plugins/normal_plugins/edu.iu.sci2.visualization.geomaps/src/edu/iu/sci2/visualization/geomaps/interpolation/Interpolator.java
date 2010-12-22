package edu.iu.sci2.visualization.geomaps.interpolation;

import java.util.List;

// T is the output type.  The input type is always double.
public interface Interpolator<T> {
	public List<T> interpolate(List<Double> values);
	public T interpolate(double value);
	public double invert(T value) throws InterpolatorInversionException;
}
