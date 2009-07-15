package edu.iu.scipolicy.visualization.geomaps.scaling;

import org.cishell.framework.algorithm.AlgorithmExecutionException;

public class LogScaler implements Scaler {
	public boolean canScale(double value) {
		return ( value > 0.0 );
	}

	public double scale(double rawValue) throws AlgorithmExecutionException {
		if ( canScale(rawValue) ) {
			return Math.log10(rawValue);
		}
		else {
			throw new AlgorithmExecutionException("Cannot scale " + rawValue + ".");
		}
	}	

	public String getUnscalableMessage() {
		return "Logarithmic scale is defined only for positive numbers.";
	}
}
