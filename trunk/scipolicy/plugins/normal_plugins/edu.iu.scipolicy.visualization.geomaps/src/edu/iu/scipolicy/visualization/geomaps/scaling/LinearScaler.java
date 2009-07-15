package edu.iu.scipolicy.visualization.geomaps.scaling;

import org.cishell.framework.algorithm.AlgorithmExecutionException;

public class LinearScaler implements Scaler {
	public boolean canScale(double value) {
		return true;
	}
	
	public double scale(double rawValue) {
		return rawValue;
	}

	public String getUnscalableMessage() throws AlgorithmExecutionException {
		throw new AlgorithmExecutionException("Faulty warning: Linear scale should be defined for any double value.");
	}
}