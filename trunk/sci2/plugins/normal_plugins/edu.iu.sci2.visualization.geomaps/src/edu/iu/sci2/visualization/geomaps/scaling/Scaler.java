package edu.iu.sci2.visualization.geomaps.scaling;

import org.cishell.framework.algorithm.AlgorithmExecutionException;

import edu.iu.sci2.visualization.geomaps.utility.Range;

// A scaler should be a monotonically increasing function!
public interface Scaler {
	public boolean canScale(double value);
	public double scale(double rawValue) throws AlgorithmExecutionException;
	public String getUnscalableMessage() throws AlgorithmExecutionException;
	public double invert(double value);
	public Range<Double> scale(Range<Double> range) throws AlgorithmExecutionException;
}