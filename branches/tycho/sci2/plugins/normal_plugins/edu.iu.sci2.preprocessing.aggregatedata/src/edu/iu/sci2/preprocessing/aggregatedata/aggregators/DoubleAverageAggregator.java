package edu.iu.sci2.preprocessing.aggregatedata.aggregators;

import java.util.List;

public class DoubleAverageAggregator extends DoubleSumAggregator {
	@Override
	public Double aggregateValue(List<Double> objectsToAggregate) {
		
		double total = super.aggregateValue(objectsToAggregate);
		double averageValue = total / objectsToAggregate.size();
		
		return averageValue;
	}
}
