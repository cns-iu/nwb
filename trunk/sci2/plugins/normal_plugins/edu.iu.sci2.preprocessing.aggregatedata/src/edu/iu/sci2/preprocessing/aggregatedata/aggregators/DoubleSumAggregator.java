package edu.iu.sci2.preprocessing.aggregatedata.aggregators;

import java.util.List;

import edu.iu.sci2.preprocessing.aggregatedata.SingleFunctionAggregator;

public class DoubleSumAggregator implements SingleFunctionAggregator<Double> {

	public Double aggregateValue(List<Double> objectsToAggregate) {
		double total = 0D;
		
		for (double currentValue : objectsToAggregate) {
			total += currentValue;
		}
		
		return total;
	}

}
