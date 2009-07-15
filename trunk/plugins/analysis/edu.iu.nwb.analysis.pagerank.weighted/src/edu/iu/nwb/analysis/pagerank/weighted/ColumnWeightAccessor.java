package edu.iu.nwb.analysis.pagerank.weighted;

import java.util.Map;

public class ColumnWeightAccessor implements WeightAccessor {

	private static final int DEFAULT_WEIGHT = 1;
	private String weightAttribute;

	public ColumnWeightAccessor(String weightAttribute) {
		this.weightAttribute = weightAttribute;
	}

	public double getWeight(Map<String, Object> attributes) {
		Double weight = (Double) attributes.get(weightAttribute);
		if(weight == null) {
			return DEFAULT_WEIGHT;
		} else if(weight <= 0) {
			throw new IllegalArgumentException("This algorithm can only be run on positively weighted networks.");
		}
		return weight;
	}

}
