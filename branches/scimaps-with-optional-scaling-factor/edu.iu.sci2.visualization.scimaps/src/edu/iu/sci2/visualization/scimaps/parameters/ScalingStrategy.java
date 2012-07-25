package edu.iu.sci2.visualization.scimaps.parameters;

import java.util.Collection;

/**
 * A strategy for scaling numbers.
 * 
 * @see ScalingStrategies
 */
public interface ScalingStrategy {
	/**
	 * A scaling factor appropriate for multiplying through this {@code data}.
	 * 
	 * @param data
	 *            Data that may be multiplied by this scaling factor. Implementations may choose to
	 *            be ignore this context.
	 * @param maximum
	 *            A requested maximum for the product of any particular datum and the scaling
	 *            factor. Implementations may choose to ignore this request.
	 * @return A scaling factor for the data calculated according to this strategy.
	 */
	float scalingFactorFor(Collection<Float> data, float maximum);
}
