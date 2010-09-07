package edu.iu.scipolicy.preprocessing.aggregatedata;

import java.util.List;

public interface SingleFunctionAggregator<E> {

	public E aggregateValue(List<E> objectsToAggregate);
}
