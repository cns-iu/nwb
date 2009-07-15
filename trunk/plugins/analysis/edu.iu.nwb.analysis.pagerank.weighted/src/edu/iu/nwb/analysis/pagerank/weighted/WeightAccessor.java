package edu.iu.nwb.analysis.pagerank.weighted;

import java.util.Map;

public interface WeightAccessor {
	public double getWeight(Map<String, Object> attributes);
}
