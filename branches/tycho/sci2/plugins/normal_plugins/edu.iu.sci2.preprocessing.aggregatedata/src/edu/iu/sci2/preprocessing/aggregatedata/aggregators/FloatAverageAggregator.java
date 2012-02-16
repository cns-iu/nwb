package edu.iu.sci2.preprocessing.aggregatedata.aggregators;

import java.util.List;

public class FloatAverageAggregator extends FloatSumAggregator {
	@Override
	public Float aggregateValue(List<Float> objectsToAggregate) {
		
		float total = super.aggregateValue(objectsToAggregate);
		float averageValue = total / objectsToAggregate.size();
		
		return averageValue;
	}
}
