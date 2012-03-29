package edu.iu.sci2.preprocessing.aggregatedata.aggregators;

import java.util.List;

public class IntegerAverageAggregator extends IntegerSumAggregator {
	@Override
	public Integer aggregateValue(List<Integer> objectsToAggregate) {
		
		int total = super.aggregateValue(objectsToAggregate);
		int average = total / objectsToAggregate.size();
		
		return average;
	}
}
