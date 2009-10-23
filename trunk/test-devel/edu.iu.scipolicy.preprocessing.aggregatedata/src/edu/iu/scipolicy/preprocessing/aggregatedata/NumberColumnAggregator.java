package edu.iu.scipolicy.preprocessing.aggregatedata;

public class NumberColumnAggregator<E> implements AggregatorImpl<E>{

	private Integer outputAggregateValue;
	
	
	public NumberColumnAggregator() {
		this.outputAggregateValue = new Integer(0);
	}

	public E getValue() {
		return (E) outputAggregateValue;
	}

	public void aggregateValue(E valueToBeAggregated) {
		outputAggregateValue += ((Number) valueToBeAggregated);
	}

}
