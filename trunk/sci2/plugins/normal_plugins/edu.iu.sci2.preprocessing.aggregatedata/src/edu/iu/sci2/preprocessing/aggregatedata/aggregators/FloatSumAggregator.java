package edu.iu.sci2.preprocessing.aggregatedata.aggregators;

import java.util.List;

import edu.iu.sci2.preprocessing.aggregatedata.SingleFunctionAggregator;

public class FloatSumAggregator implements SingleFunctionAggregator<Float> {

	public Float aggregateValue(List<Float> objectsToAggregate) {
		float total = 0F;

		for (float currentValue : objectsToAggregate) {
			FloatAggregatorHelper.checkAdditionForOverOrUnderFlow(total,
					currentValue);
			total += currentValue;
		}

		return total;
	}

}
