package edu.iu.scipolicy.preprocessing.aggregatedata;

public class BooleanColumnAggregator<E> implements AggregatorImpl<E>{

	private Boolean outputAggregateValue;
	
	
	public BooleanColumnAggregator() {
		this.outputAggregateValue = new Boolean(true);
	}

	public E getValue() {
		return (E) outputAggregateValue;
	}

	public void aggregateValue(E valueToBeAggregated) {
		outputAggregateValue &= (Boolean) valueToBeAggregated;
	}

}
