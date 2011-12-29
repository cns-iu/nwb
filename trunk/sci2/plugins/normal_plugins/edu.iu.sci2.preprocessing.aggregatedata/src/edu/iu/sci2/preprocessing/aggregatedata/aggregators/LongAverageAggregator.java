package edu.iu.sci2.preprocessing.aggregatedata.aggregators;

import java.util.List;

import edu.iu.sci2.preprocessing.aggregatedata.SingleFunctionAggregator;

public class LongAverageAggregator implements SingleFunctionAggregator<Long> {

	@Override
	public Long aggregateValue(List<Long> objectsToAggregate) {

		LongSumAggregator sumAggregator = new LongSumAggregator();

		// For efficacy sake, the long primitive is used.
		long total = sumAggregator.aggregateValue(objectsToAggregate);
		long average = total / objectsToAggregate.size();

		return average;
	}

}
