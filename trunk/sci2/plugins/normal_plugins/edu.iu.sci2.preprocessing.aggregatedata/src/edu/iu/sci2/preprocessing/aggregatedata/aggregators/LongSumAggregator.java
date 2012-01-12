package edu.iu.sci2.preprocessing.aggregatedata.aggregators;

import java.util.List;

import edu.iu.sci2.preprocessing.aggregatedata.SingleFunctionAggregator;

public class LongSumAggregator implements SingleFunctionAggregator<Long> {
	public Long aggregateValue(List<Long> objectsToAggregate) {
		// For efficacy sake, the long primitive is used.
		long total = 0L;

		for (Long currentValue : objectsToAggregate) {
			LongAggregatorHelper.checkAdditionForOverOrUnderFlow(total,
					currentValue);
			total += currentValue;
		}
		return total;
	}
}
