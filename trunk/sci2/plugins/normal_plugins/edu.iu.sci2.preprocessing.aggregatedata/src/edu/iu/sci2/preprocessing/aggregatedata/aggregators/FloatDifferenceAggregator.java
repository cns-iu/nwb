package edu.iu.sci2.preprocessing.aggregatedata.aggregators;

import java.util.List;

import edu.iu.sci2.preprocessing.aggregatedata.SingleFunctionAggregator;

public class FloatDifferenceAggregator implements SingleFunctionAggregator<Float> {
	public Float aggregateValue(List<Float> objectsToAggregate) {
		float difference = 0F;

		for (Float currentValue : objectsToAggregate) {
			FloatAggregatorHelper.checkAdditionForOverOrUnderFlow(-difference,
					currentValue);
			difference = currentValue - difference;
		}

		return difference;
	}
}
