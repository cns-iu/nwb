package edu.iu.nwb.analysis.pagerank.weighted;

import java.util.Map;

/**
 * A {@link WeightAccessor} that uses the value from the Map given to
 * {@link #getWeight(Map)}.
 * 
 * @author dmcoe
 * 
 */
public class ColumnWeightAccessor implements WeightAccessor {

	private String weightAttribute;

	/**
	 * A {@link WeightAccessor} that uses the value from the Map given to
	 * {@link #getWeight(Map)}.
	 * 
	 * @param weightAttribute
	 *            The key to the Map given to {@link #getWeight(Map)} to get the
	 *            weight.
	 */
	public ColumnWeightAccessor(String weightAttribute) {
		this.weightAttribute = weightAttribute;
	}

	@Override
	public float getWeight(Map<String, Object> attributes)
			throws InvalidWeightException {
		Float weight = (Float) attributes.get(this.weightAttribute);
		
		// NWB does not allow null values for floats.
		assert(weight != null);
		
		if (weight <= 0) {
			throw new InvalidWeightException(
					"This algorithm can only be run on positively weighted networks.");
		}
		
		if (Float.isNaN(weight)) {
			throw new InvalidWeightException("Weight must not be NaN.");
		}

		if (Float.isInfinite(weight)) {
			throw new InvalidWeightException("Weight must not be infinite.");
		}
		
		return weight;
	}

}
