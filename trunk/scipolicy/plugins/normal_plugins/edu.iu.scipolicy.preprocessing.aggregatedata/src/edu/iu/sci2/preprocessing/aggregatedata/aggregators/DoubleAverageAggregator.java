package edu.iu.sci2.preprocessing.aggregatedata.aggregators;

import java.util.List;

import edu.iu.sci2.preprocessing.aggregatedata.SingleFunctionAggregator;

public class DoubleAverageAggregator 
	extends DoubleSumAggregator 
	implements SingleFunctionAggregator<Double> {

	public Double aggregateValue(List<Double> objectsToAggregate) {
		
		Double currentSummmationValue = super.aggregateValue(objectsToAggregate);
		Double averageValue = currentSummmationValue.doubleValue() / objectsToAggregate.size();
		
		return averageValue.doubleValue();
	}
}
