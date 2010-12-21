package edu.iu.scipolicy.visualization.geomaps.utility;

import java.util.Collection;


public class Range<T> {
	private T min;
	private T max;

	public Range(T min, T max) {
		this.min = min;
		this.max = max;
	}

	public T getMin() {
		return min;
	}

	public T getMax() {
		return max;
	}

	public void setMin(T value) {
		this.min = value;
	}
	
	public void setMax(T value) {
		this.max = value;
	}

	public static Range<Double> calculateRange(
			Collection<Double> values) {
		Range<Double> range =
			new Range<Double>(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
	
		for ( double value : values ) {
			if (value < range.getMin()) {
				range.setMin(value);
			}
			if (value > range.getMax()) {
				range.setMax(value);
			}
		}
		
		return range;
	}
}
