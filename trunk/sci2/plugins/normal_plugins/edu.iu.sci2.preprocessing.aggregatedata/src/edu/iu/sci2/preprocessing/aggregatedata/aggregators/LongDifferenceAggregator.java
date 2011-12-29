package edu.iu.sci2.preprocessing.aggregatedata.aggregators;

import java.util.List;

import edu.iu.sci2.preprocessing.aggregatedata.SingleFunctionAggregator;

public class LongDifferenceAggregator implements SingleFunctionAggregator<Long> {

	@Override
	public Long aggregateValue(List<Long> objectsToAggregate) {
		// For efficacy sake, the long primitive is used.
		long total = 0L;

		for (Long currentValue : objectsToAggregate) {
			// checkAdditionForOverOrUnderflow only works with addition so
			// update the values accordingly.
			LongAggregatorHelper.checkAdditionForOverOrUnderFlow(-total,
					currentValue);

			total = currentValue - total;
		}

		return total;
	}

}
