package edu.iu.scipolicy.visualization.geomaps.scaling;

import org.cishell.framework.algorithm.AlgorithmExecutionException;

public interface DoubleScaler {
	public boolean isScalable(double value);
	public double scale(double rawValue) throws AlgorithmExecutionException;
	public String getUnscalableMessage() throws AlgorithmExecutionException;
}
