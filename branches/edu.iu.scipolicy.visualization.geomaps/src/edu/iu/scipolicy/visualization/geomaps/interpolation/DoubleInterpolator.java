package edu.iu.scipolicy.visualization.geomaps.interpolation;

import org.cishell.framework.algorithm.AlgorithmExecutionException;

public interface DoubleInterpolator<T> {
	public T interpolate(double value) throws AlgorithmExecutionException;
}
