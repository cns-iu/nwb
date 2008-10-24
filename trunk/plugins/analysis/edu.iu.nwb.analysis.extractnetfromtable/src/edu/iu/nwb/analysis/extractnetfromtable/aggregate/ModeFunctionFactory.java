package edu.iu.nwb.analysis.extractnetfromtable.aggregate;

public class ModeFunctionFactory implements AggregateFunctionFactory{
	private static final String type = AggregateFunctionNames.MODE;

	public AggregateFunction getFunction(Class c) {
		// TODO Auto-generated method stub
		if(c.equals(int.class) || c.equals(Integer.class))
			return new IntegerMode();
		else if(c.equals(String.class)) {
			return new StringMode();
		} else {
			return new BooleanMode();
		}
	}

	public String getType() {
		// TODO Auto-generated method stub
		return ModeFunctionFactory.type;
	}

}

//TODO: These do not fit my understanding of "mode"...

class IntegerMode extends AggregateFunction{
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
			value = ((Number)o).intValue();
	}
	
}

class StringMode extends AggregateFunction{
	String value = "";
	
	public Object getResult() {
		return value;
	}
	
	public Class getType() {
		return String.class;
	}
	
	public void operate(Object o) {
		if (o != null && o instanceof String) {
			value = (String) o;
		} 
	}
}

class BooleanMode extends AggregateFunction{
	boolean value = false;
	
	public Object getResult(){
		return new Boolean(value).toString();
	}
	
	public Class getType(){
		return String.class;
	}
	
	public void operate(Object o){
		if(o==null){
			value = false;
		}else{
			value = true;
		}
	}
	
}