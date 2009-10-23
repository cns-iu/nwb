package edu.iu.scipolicy.preprocessing.aggregatedata;

public interface AggregatorImpl<I> {
	public void aggregateValue(I valueToBeAggregated);
	public I getValue();
}
