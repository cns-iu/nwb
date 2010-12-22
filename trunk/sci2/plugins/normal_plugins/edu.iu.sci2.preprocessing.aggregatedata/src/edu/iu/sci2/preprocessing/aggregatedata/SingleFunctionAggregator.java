package edu.iu.sci2.preprocessing.aggregatedata;

import java.util.List;

public interface SingleFunctionAggregator<E> {

	public E aggregateValue(List<E> objectsToAggregate);
}
