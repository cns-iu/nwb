package edu.iu.nwb.analysis.extractnetfromtable.aggregate;

public abstract class AggregateFunction {
	public abstract Object getResult();
	public abstract Class getType();
	public abstract void operate(Object o);
}
