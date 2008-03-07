package edu.iu.nwb.analysis.extractnetfromtable.aggregate;

public class CountFunctionFactory implements AggregateFunctionFactory{
	private static final String type = AggregateFunctionNames.COUNT;

	public AggregateFunction getFunction(Class c) {
		// TODO Auto-generated method stub
		return new Count();
	}

	public String getType() {
		// TODO Auto-generated method stub
		return CountFunctionFactory.type;
	}

}

class Count extends AggregateFunction {
	int total;

	public Count() {
		total = 0;
	}

	@Override
	public Object getResult() {
		// TODO Auto-generated method stub
		return new Integer(total);
	}

	@Override
	public Class getType() {
		// TODO Auto-generated method stub
		return Integer.class;
	}

	@Override
	public void operate(Object o) {
		// TODO Auto-generated method stub
		total += 1;
	}



}