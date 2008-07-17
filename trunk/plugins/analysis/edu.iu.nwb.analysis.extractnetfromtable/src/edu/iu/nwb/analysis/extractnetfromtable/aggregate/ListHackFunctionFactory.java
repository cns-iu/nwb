package edu.iu.nwb.analysis.extractnetfromtable.aggregate;

public class ListHackFunctionFactory implements AggregateFunctionFactory{
	private static final String type = AggregateFunctionNames.LIST;

	public AggregateFunction getFunction(Class c) {
		// TODO Auto-generated method stub
		return new ListHack();
	}

	public String getType() {
		// TODO Auto-generated method stub
		return ListHackFunctionFactory.type;
	}

}

class ListHack extends AggregateFunction{
	int value;
	
	public Object getResult() {
		// TODO Auto-generated method stub
		return new Integer(value);
	}

	public Class getType() {
		// TODO Auto-generated method stub
		return int.class;
	}

	public void operate(Object o) {
		if(o == null){
			value = 1;
		}else{
			value = ((Number)o).intValue();
		}
	}
	
}