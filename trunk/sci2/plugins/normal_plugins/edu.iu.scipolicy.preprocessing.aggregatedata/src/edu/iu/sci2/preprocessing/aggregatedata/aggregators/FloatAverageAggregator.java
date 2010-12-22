package edu.iu.sci2.preprocessing.aggregatedata.aggregators;

import java.util.List;

import edu.iu.sci2.preprocessing.aggregatedata.SingleFunctionAggregator;

public class FloatAverageAggregator 
	extends FloatSumAggregator 
	implements SingleFunctionAggregator<Float> {

	public Float aggregateValue(List<Float> objectsToAggregate) {
		
		Float currentSummmationValue = super.aggregateValue(objectsToAggregate);
		Float averageValue = currentSummmationValue.floatValue() / objectsToAggregate.size();
		
		return averageValue.floatValue();
	}
}
