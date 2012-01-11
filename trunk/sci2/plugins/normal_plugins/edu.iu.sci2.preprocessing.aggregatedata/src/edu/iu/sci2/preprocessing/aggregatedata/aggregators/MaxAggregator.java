package edu.iu.sci2.preprocessing.aggregatedata.aggregators;

import java.util.Collections;
import java.util.List;

import edu.iu.sci2.preprocessing.aggregatedata.SingleFunctionAggregator;

public class MaxAggregator<T extends Comparable<T>> implements SingleFunctionAggregator<T> {
	@Override
	public T aggregateValue(List<T> objectsToAggregate) {
		return Collections.max(objectsToAggregate);
	}
}
