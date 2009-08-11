package edu.iu.scipolicy.visualization.geomaps.utility;


public class Calculator {
	
	public static double mean(Double... values) {
		if (values.length > 0) {
			double sum = 0.0;
			
			for (double value : values) {
				// Beware overflow when values.length is great
				sum += value;
			}
			
			return (sum / values.length);
		} else {
			return 0.0;
		}
	}
}
