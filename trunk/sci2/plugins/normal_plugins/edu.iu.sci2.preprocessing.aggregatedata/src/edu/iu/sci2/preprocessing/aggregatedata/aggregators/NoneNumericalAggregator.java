package edu.iu.sci2.preprocessing.aggregatedata.aggregators;

import java.util.List;

import edu.iu.sci2.preprocessing.aggregatedata.SingleFunctionAggregator;

public class NoneNumericalAggregator implements SingleFunctionAggregator<Object> {
	@Override
	public Object aggregateValue(List<Object> objectsToAggregate) {
		return null;
	}
}
