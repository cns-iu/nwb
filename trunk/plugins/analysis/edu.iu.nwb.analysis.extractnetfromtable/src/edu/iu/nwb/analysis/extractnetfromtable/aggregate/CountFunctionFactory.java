package edu.iu.nwb.analysis.extractnetfromtable.aggregate;

public class CountFunctionFactory implements AggregateFunctionFactory{
	private static final String type = AggregateFunctionNames.COUNT;

	public AggregateFunction getFunction(Class c) {
		return new Count();
	}

	public String getType() {
		return CountFunctionFactory.type;
	}
}

class Count extends AggregateFunction {
	int total;

	public Count() {
		total = 0;
	}

	public Object getResult() {
		return new Integer(total);
	}

	public Class getType() {
		return Integer.class;
	}

	public void operate(Object o) {
		total += 1;
	}
}