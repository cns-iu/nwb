package edu.iu.scipolicy.preprocessing.aggregatedata;

public class StringColumnAggregator<E> implements AggregatorImpl<E>{

	private StringBuffer outputAggregateValue;
	
	
	public StringColumnAggregator() {
		this.outputAggregateValue = new StringBuffer();
	}

	public E getValue() {
		return (E) outputAggregateValue.toString();
	}

	public void aggregateValue(E valueToBeAggregated) {
		outputAggregateValue.append(valueToBeAggregated.toString());
	}

}
