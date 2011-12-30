package edu.iu.sci2.preprocessing.aggregatedata.aggregators;

import java.util.List;

import edu.iu.sci2.preprocessing.aggregatedata.SingleFunctionAggregator;

public class DoubleDifferenceAggregator implements
		SingleFunctionAggregator<Double> {

	public Double aggregateValue(List<Double> objectsToAggregate) {
		double difference = 0D;
		
		for (double currentValue : objectsToAggregate) {
			DoubleAggregatorHelper.checkAdditionForOverOrUnderFlow(-difference, currentValue);
			difference = currentValue - difference;
		}

		return difference;
	}

}
