package edu.iu.sci2.preprocessing.aggregatedata.aggregators;

import java.util.List;

import edu.iu.sci2.preprocessing.aggregatedata.SingleFunctionAggregator;

public class IntegerAverageAggregator 
	extends IntegerSumAggregator 
	implements SingleFunctionAggregator<Integer> {

	public Integer aggregateValue(List<Integer> objectsToAggregate) {
		
		Integer currentSummmationValue = super.aggregateValue(objectsToAggregate);
		Integer averageValue = currentSummmationValue.intValue() / objectsToAggregate.size();
		
		return averageValue.intValue();
	}
}
