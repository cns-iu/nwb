package edu.iu.sci2.visualization.bipartitenet.scale;

import com.google.common.collect.ImmutableList;

public class ZeroAnchoredCircleRadiusScale implements Scale<Double,Double> {
	// Don't want any nodes to be totally invisible (radius 0)
	private static final double MIN_RADIUS = 1;
	private final BasicZeroAnchoredScale areaScale;
	
	public ZeroAnchoredCircleRadiusScale(double maxRadius) {
		areaScale = new BasicZeroAnchoredScale(MIN_RADIUS, Math.PI * maxRadius * maxRadius);
	}

	@Override
	public void train(Iterable<Double> trainingData) {
		areaScale.train(trainingData);
	}

	public void doneTraining() {
		areaScale.doneTraining();
	}

	public Double apply(Double value) {
		// Don't sqrt(negative), it would be nasty to return NaN.
		if (value < 0) {
			return MIN_RADIUS;
		}
		return Math.sqrt(areaScale.apply(value) / Math.PI);
	}

	public ImmutableList<Double> getExtrema() {
		return areaScale.getExtrema();
	}
}
