package edu.iu.sci2.preprocessing.aggregatedata.aggregators;

import java.util.Collections;
import java.util.List;

import edu.iu.sci2.preprocessing.aggregatedata.SingleFunctionAggregator;

public class MinAggregator<T extends Comparable<T>> implements SingleFunctionAggregator<T> {
	public T aggregateValue(List<T> objectsToAggregate) {
		return Collections.min(objectsToAggregate);
	}
}
