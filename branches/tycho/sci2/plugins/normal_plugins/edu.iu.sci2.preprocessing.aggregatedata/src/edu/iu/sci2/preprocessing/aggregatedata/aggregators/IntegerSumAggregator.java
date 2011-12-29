package edu.iu.sci2.preprocessing.aggregatedata.aggregators;

import java.util.List;

import edu.iu.sci2.preprocessing.aggregatedata.SingleFunctionAggregator;

public class IntegerSumAggregator implements SingleFunctionAggregator<Integer> {

	public Integer aggregateValue(List<Integer> objectsToAggregate) {
		Integer currentSummmationValue = new Integer(0);
		
		for (Integer currentValue : objectsToAggregate) {
			currentSummmationValue = currentSummmationValue.intValue() 
										+ currentValue.intValue();
		}
		
		return currentSummmationValue.intValue();
	}

}
