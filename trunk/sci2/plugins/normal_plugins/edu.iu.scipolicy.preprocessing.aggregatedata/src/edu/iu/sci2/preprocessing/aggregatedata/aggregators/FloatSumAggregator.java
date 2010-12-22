package edu.iu.sci2.preprocessing.aggregatedata.aggregators;

import java.util.List;

import edu.iu.sci2.preprocessing.aggregatedata.SingleFunctionAggregator;

public class FloatSumAggregator implements SingleFunctionAggregator<Float> {

	public Float aggregateValue(List<Float> objectsToAggregate) {
		Float currentSummmationValue = new Float(0);
		
		for (Float currentValue : objectsToAggregate) {
			currentSummmationValue = currentSummmationValue.floatValue() 
										+ currentValue.floatValue();
		}
		
		return currentSummmationValue.floatValue();
	}

}
