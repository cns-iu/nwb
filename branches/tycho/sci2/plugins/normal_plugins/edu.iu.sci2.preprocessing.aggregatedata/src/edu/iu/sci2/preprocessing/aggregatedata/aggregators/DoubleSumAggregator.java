package edu.iu.sci2.preprocessing.aggregatedata.aggregators;

import java.util.List;

import edu.iu.sci2.preprocessing.aggregatedata.SingleFunctionAggregator;

public class DoubleSumAggregator implements SingleFunctionAggregator<Double> {

	public Double aggregateValue(List<Double> objectsToAggregate) {
		Double currentSummmationValue = new Double(0);
		
		for (Double currentValue : objectsToAggregate) {
			currentSummmationValue = currentSummmationValue.doubleValue() 
										+ currentValue.doubleValue();
		}
		
		return currentSummmationValue.doubleValue();
	}

}
