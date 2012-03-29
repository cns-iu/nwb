package edu.iu.sci2.preprocessing.aggregatedata.aggregators;

import java.util.List;

import edu.iu.sci2.preprocessing.aggregatedata.SingleFunctionAggregator;

public class IntegerDifferenceAggregator implements SingleFunctionAggregator<Integer> {
	public Integer aggregateValue(List<Integer> objectsToAggregate) {
		int difference = 0;

		for (int currentValue : objectsToAggregate) {
			difference = currentValue - difference;
		}

		return difference;
	}
}
