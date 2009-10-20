package edu.iu.nwb.analysis.extractnetfromtable.aggregate;


public class MaxFunctionFactory implements AggregateFunctionFactory{
	public static final String type = AggregateFunctionNames.MAX;


	public AggregateFunction getFunction(Class c) {

		if (c.equals(int.class) || c.equals(Integer.class)) {
			return new IntegerMax();
		}
		if (c.equals(double.class) || c.equals(Double.class)) {
			return new DoubleMax();
		}
		if (c.equals(float.class) || c.equals(Float.class)) {
			return new FloatMax();
		}

		return null; //can't handle that class;
	}

	public String getType() {
		return MaxFunctionFactory.type;
	}
}

class IntegerMax extends AggregateFunction{
	int value;

	public IntegerMax() {
		value = Integer.MIN_VALUE;
	}

	public Object getResult() {
		return new Integer(value);
	}

	public Class getType() {
		return Integer.class;
	}

	public void operate(Object o) {
		if (o != null && o instanceof Number) {		
			if (((Number) o).intValue() > value) {
				value = ((Number) o).intValue();
			}		
		}else
			throw new IllegalArgumentException(
					"IntegerMax can only operate on Numbers.");
	}
}

class FloatMax extends AggregateFunction {
	float value;

	public FloatMax() {
		value = Float.MIN_VALUE;
	}

	public Object getResult() {
		return new Float(value);
	}

	public Class getType() {
		return Float.class;
	}

	public void operate(Object o) {
		if (o != null && o instanceof Number) {		
			if (((Number) o).floatValue() > value) {
				value = ((Number) o).floatValue();
			}		
		}else
			throw new IllegalArgumentException(
					"FloatMax can only operate on Numbers.");
	}
}

class DoubleMax extends AggregateFunction {
	double value;

	public DoubleMax() {
		value = Double.MIN_VALUE;
	}

	public Object getResult() {
		return new Double(value);
	}

	public Class getType() {
		return Double.class;
	}

	public void operate(Object o) {
		if (o != null && o instanceof Number) {
			if (((Number) o).doubleValue() > value) {
				value = ((Number) o).doubleValue();
			}		
		}else
			throw new IllegalArgumentException(
					"DoubleMax can only operate on Numbers.");
	}
}