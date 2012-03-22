package edu.iu.sci2.visualization.geomaps.utility;

import java.util.Collection;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Doubles;


public class Averages {
	public static final String FAIL_MESSAGE = "Cannot take the mean of zero numbers.";

	private Averages() {}
	
	public static double meanOfDoubles(Collection<Double> values) {
		Preconditions.checkArgument(!values.isEmpty(), FAIL_MESSAGE);
		
		double sum = 0.0;		
		for (double value : values) {
			sum += value;
		}
		
		return (sum / (values.size()));
	}
	
	public static double meanOfDoubles(double first, double... rest) {
		return meanOfDoubles(Doubles.asList(Doubles.concat(new double[]{ first }, rest)));
	}
}
