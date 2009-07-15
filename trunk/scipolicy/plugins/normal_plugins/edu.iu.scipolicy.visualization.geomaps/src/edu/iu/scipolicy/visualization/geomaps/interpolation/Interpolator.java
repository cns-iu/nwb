package edu.iu.scipolicy.visualization.geomaps.interpolation;

import org.cishell.framework.algorithm.AlgorithmExecutionException;

// T is the output type.  The input type is always double.
public interface Interpolator<T> {
	public T interpolate(double value) throws AlgorithmExecutionException;
}
