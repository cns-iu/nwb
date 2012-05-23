package edu.iu.nwb.analysis.pagerank.weighted;

import java.util.Map;

/**
 * A {@link WeightAccessor} that returns a constant weight.
 * 
 * @author dmcoe
 * 
 */
public class ConstantWeightAccessor implements WeightAccessor {

	private float weight;

	/**
	 * A {@link WeightAccessor} that returns a constant weight regardless of the
	 * map given to {@link #getWeight(Map)}.
	 * 
	 * @param weight
	 *            The constant weight to return from {@link #getWeight(Map)}.
	 */
	public ConstantWeightAccessor(float weight) {
		this.weight = weight;
	}

	/**
	 * Unimplemented since a constant weight will be used regardless of the
	 * attributes.
	 * 
	 * @return A constant weight regardless of the attributes.
	 */
	@Override
	public float getWeight(Map<String, Object> attributes) {
		return this.weight;
	}

}
