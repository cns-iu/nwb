package edu.iu.sci2.preprocessing.aggregatedata.aggregators;

import java.util.List;

import edu.iu.sci2.preprocessing.aggregatedata.SingleFunctionAggregator;

public class FloatDifferenceAggregator implements SingleFunctionAggregator<Float> {

	public Float aggregateValue(List<Float> objectsToAggregate) {
		Float currentDifferenceValue = new Float(0);
		
		for (Float currentValue : objectsToAggregate) {
			currentDifferenceValue = currentValue.floatValue()
											- currentDifferenceValue.floatValue();
		}
		
		return currentDifferenceValue.floatValue();
	}

}
