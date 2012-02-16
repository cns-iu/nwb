package edu.iu.sci2.preprocessing.aggregatedata.aggregators;

import java.util.List;

import edu.iu.sci2.preprocessing.aggregatedata.SingleFunctionAggregator;

public class LongDifferenceAggregator implements SingleFunctionAggregator<Long> {
	public Long aggregateValue(List<Long> objectsToAggregate) {
		// For efficacy sake, the long primitive is used.
		long difference = 0L;

		for (Long currentValue : objectsToAggregate) {
			difference = currentValue - difference;
		}

		return difference;
	}
}
