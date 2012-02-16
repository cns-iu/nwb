package edu.iu.sci2.preprocessing.aggregatedata.aggregators;

import java.util.List;

import edu.iu.sci2.preprocessing.aggregatedata.SingleFunctionAggregator;

public class LongDifferenceAggregator implements SingleFunctionAggregator<Long> {

	@Override
	public Long aggregateValue(List<Long> objectsToAggregate) {
		// For efficacy sake, the long primitive is used.
		long difference = 0L;

		for (Long currentValue : objectsToAggregate) {
			// checkAdditionForOverOrUnderflow only works with addition so
			// update the values accordingly.
			LongAggregatorHelper.checkAdditionForOverOrUnderFlow(-difference,
					currentValue);

			difference = currentValue - difference;
		}

		return difference;
	}

}
