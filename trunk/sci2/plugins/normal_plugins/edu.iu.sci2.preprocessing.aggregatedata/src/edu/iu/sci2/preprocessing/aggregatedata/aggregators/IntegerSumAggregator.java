package edu.iu.sci2.preprocessing.aggregatedata.aggregators;

import java.util.List;

import edu.iu.sci2.preprocessing.aggregatedata.SingleFunctionAggregator;

public class IntegerSumAggregator implements SingleFunctionAggregator<Integer> {

	public Integer aggregateValue(List<Integer> objectsToAggregate) {
		int total = 0;

		for (int currentValue : objectsToAggregate) {
			total += currentValue;
		}

		return total;
	}

}
