package edu.iu.scipolicy.visualization.geomaps.interpolation;



// T is the output type.  The input type is always double.
public interface Interpolator<T> {
	public T interpolate(double value);
	public double invert(T value) throws InterpolatorInversionException;
}
