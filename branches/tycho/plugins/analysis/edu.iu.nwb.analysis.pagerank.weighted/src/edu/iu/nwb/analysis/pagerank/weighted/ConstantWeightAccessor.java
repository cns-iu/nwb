package edu.iu.nwb.analysis.pagerank.weighted;

import java.util.Map;

public class ConstantWeightAccessor implements WeightAccessor {

	private double weight;

	public ConstantWeightAccessor(double weight) {
		this.weight = weight;
	}

	public double getWeight(Map<String, Object> attributes) {
		return weight;
	}

}
