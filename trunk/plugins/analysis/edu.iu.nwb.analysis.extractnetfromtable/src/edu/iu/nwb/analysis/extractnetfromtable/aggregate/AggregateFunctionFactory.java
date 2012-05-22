package edu.iu.nwb.analysis.extractnetfromtable.aggregate;

public interface AggregateFunctionFactory {
	public AbstractAggregateFunction getFunction(Class c);

	public AggregateFunctionName getType();
}
