package edu.iu.sci2.preprocessing.aggregatedata.aggregators;

import java.util.List;

import edu.iu.sci2.preprocessing.aggregatedata.SingleFunctionAggregator;

public class DoubleAverageAggregator 
	extends DoubleSumAggregator 
	implements SingleFunctionAggregator<Double> {

	public Double aggregateValue(List<Double> objectsToAggregate) {
		
		double total = super.aggregateValue(objectsToAggregate);
		double averageValue = total / objectsToAggregate.size();
		
		return averageValue;
	}
}
