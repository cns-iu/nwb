package edu.iu.nwb.analysis.extractnetfromtable.aggregate;


public class MinFunctionFactory implements AggregateFunctionFactory{
	public static final String type = AggregateFunctionNames.MIN;


	public String getType() {
		return MinFunctionFactory.type;
	}


	public AggregateFunction getFunction(Class c) {
		if (c.equals(int.class) || c.equals(Integer.class)) {
			return new IntegerMin();
		}
		if (c.equals(double.class) || c.equals(Double.class)) {
			return new DoubleMin();
		}
		if (c.equals(float.class) || c.equals(Float.class)) {
			return new FloatMin();
		}
		return null;
	}
}


class DoubleMin extends AggregateFunction {
	double value;

	public DoubleMin() {
		value = Double.MAX_VALUE;
	}

	public Object getResult() {
		return new Double(value);
	}

	public Class getType() {
		return Double.class;
	}

	public void operate(Object o) {
		if (o != null && o instanceof Number) {			
			if (((Number) o).doubleValue() < value) {
				value = ((Number) o).doubleValue();
			}			
		}else
			throw new IllegalArgumentException(
					"DoubleMin can only operate on Numbers.");
	}
}

class FloatMin extends AggregateFunction {
	float value;

	public FloatMin() {
		value = Float.MAX_VALUE;
	}

	public Object getResult() {
		return new Float(value);
	}

	public Class getType() {
		return Float.class;
	}

	public void operate(Object o) {
		if (o != null && o instanceof Number) {			
			if (((Number) o).floatValue() < value) {
				value = ((Number) o).floatValue();
			}			
		}else
			throw new IllegalArgumentException(
					"FloatMin can only operate on Numbers.");
	}
}

class IntegerMin extends AggregateFunction {
	int value;

	public IntegerMin() {
		value = Integer.MAX_VALUE;
	}

	public Object getResult() {
		return new Integer(value);
	}

	public Class getType() {
		return Integer.class;
	}

	public void operate(Object o) {
		if (o != null && o instanceof Number) {
			if (((Number) o).intValue() < value) {
				value = ((Number) o).intValue();
			}		
		}else
			throw new IllegalArgumentException(
					"IntegerMin can only operate on Numbers.");
	}
}