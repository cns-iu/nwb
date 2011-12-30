package edu.iu.sci2.preprocessing.aggregatedata.aggregators;

import java.util.List;

import edu.iu.sci2.preprocessing.aggregatedata.SingleFunctionAggregator;

public class FloatAverageAggregator 
	extends FloatSumAggregator 
	implements SingleFunctionAggregator<Float> {

	public Float aggregateValue(List<Float> objectsToAggregate) {
		
		float total = super.aggregateValue(objectsToAggregate);
		float averageValue = total / objectsToAggregate.size();
		
		return averageValue;
	}
}
