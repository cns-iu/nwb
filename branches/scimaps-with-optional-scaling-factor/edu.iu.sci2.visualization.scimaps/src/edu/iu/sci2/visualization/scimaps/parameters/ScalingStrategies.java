package edu.iu.sci2.visualization.scimaps.parameters;

import java.util.Collection;
import java.util.Collections;

/**
 * Static utility methods pertaining to {@link ScalingStrategy} instances.
 */
public final class ScalingStrategies {
	private ScalingStrategies() {}
	
	/**
	 * A strategy that serves this explicit scaling factor. All data and requested maximum context
	 * will be ignored.
	 */
	public static ScalingStrategy explicit(float scalingFactor) {
		return new ExplicitScalingStrategy(scalingFactor);
	}

	private static final class ExplicitScalingStrategy implements ScalingStrategy {
		private final float scalingFactor;

		private ExplicitScalingStrategy(float scalingFactor) {
			this.scalingFactor = scalingFactor;
		}

		@Override
		public float scalingFactorFor(Collection<Float> data, float requestedMaximum) {
			return scalingFactor;
		}
	}
	
	/**
	 * A strategy that will calculate a scaling factor whose product with the largest value in the
	 * data will be the requested maximum.
	 */
	public static ScalingStrategy automatic() {
		return new AutomaticScalingStrategy();
	}

	private static final class AutomaticScalingStrategy implements ScalingStrategy {
		@Override
		public float scalingFactorFor(Collection<Float> data, float requestedMaximum) {
			/* This is the scaling factor that when multiplied by the largest value in the data
			 * will yield the requested maximum. */
			return requestedMaximum / Collections.max(data);
		}
	}
}
