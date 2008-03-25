package edu.iu.nwb.analysis.extractnetfromtable.aggregate;

public interface AggregateFunctionFactory {
	public AggregateFunction getFunction(Class c);
	public String getType();
}
