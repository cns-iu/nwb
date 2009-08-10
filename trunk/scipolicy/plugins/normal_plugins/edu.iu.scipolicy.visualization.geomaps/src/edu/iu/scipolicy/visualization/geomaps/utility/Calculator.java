package edu.iu.scipolicy.visualization.geomaps.utility;

import java.util.Arrays;
import java.util.List;

public class Calculator {
	
	public static double mean(double x1, double x2) {
		return mean(Arrays.asList(x1, x2));
	}
	public static double mean(double x1, double x2, double x3) {
		return mean(Arrays.asList(x1, x2, x3));
	}	
	public static double mean(List<Double> values) {
		if (values.size() > 0) {
			double sum = 0.0;
			
			for (double value : values) {
				// Beware overflow when values.size() is great
				sum += value;
			}
			
			return (sum / values.size());
		} else {
			return 0.0;
		}
	}
}
