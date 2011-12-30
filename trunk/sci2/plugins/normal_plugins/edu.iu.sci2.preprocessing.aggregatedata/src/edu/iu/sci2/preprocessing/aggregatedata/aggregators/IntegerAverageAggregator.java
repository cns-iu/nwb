package edu.iu.sci2.preprocessing.aggregatedata.aggregators;

import java.util.List;

import edu.iu.sci2.preprocessing.aggregatedata.SingleFunctionAggregator;

public class IntegerAverageAggregator 
	extends IntegerSumAggregator 
	implements SingleFunctionAggregator<Integer> {

	public Integer aggregateValue(List<Integer> objectsToAggregate) {
		
		int total = super.aggregateValue(objectsToAggregate);
		int average = total / objectsToAggregate.size();
		
		return average;
	}
}
