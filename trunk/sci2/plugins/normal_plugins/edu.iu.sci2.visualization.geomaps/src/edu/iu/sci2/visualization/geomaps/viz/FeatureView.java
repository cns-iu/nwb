package edu.iu.sci2.visualization.geomaps.viz;

import java.util.EnumMap;

import edu.iu.sci2.visualization.geomaps.viz.strategy.Strategy;

public class FeatureView {
	private final String featureName;
	private final EnumMap<FeatureDimension, Strategy> strategies;

	public FeatureView(String featureName, EnumMap<FeatureDimension, Strategy> strategies) {
		this.featureName = featureName;
		this.strategies = strategies;
	}
	
	
	public String getFeatureName() {
		return featureName;
	}

	public Strategy strategyFor(FeatureDimension dimension) {
		if (!strategies.containsKey(dimension)) {
			return dimension.defaultStrategy();
		}
		
		return strategies.get(dimension);
	}
}
