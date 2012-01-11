package edu.iu.sci2.preprocessing.aggregatedata.aggregators;

import java.util.List;

import edu.iu.sci2.preprocessing.aggregatedata.SingleFunctionAggregator;

public class IntegerDifferenceAggregator implements SingleFunctionAggregator<Integer> {
	@Override
	public Integer aggregateValue(List<Integer> objectsToAggregate) {
		int difference = 0;

		for (int currentValue : objectsToAggregate) {
			IntegerAggregatorHelper.checkAdditionForOverOrUnderFlow(-difference,
					currentValue);
			difference = currentValue - difference;
		}

		return difference;
	}
}
