package edu.iu.nwb.analysis.extractnetfromtable.aggregate;


public class SumFunctionFactory implements AggregateFunctionFactory{
	public static final String type = AggregateFunctionNames.SUM;


	public AggregateFunction getFunction(Class c) {
		if (c.equals(int.class) || c.equals(Integer.class)) {
			return new IntegerSum();
		}
		if (c.equals(double.class) || c.equals(Double.class)) {
			return new DoubleSum();
		}
		if (c.equals(float.class) || c.equals(Float.class)) {
			return new FloatSum();
		}
		return null; //can't handle class error.
	}

	public String getType() {
		return SumFunctionFactory.type;
	}
}

class DoubleSum extends AggregateFunction {
	double total;

	public DoubleSum() {
		total = 0;
	}

	public Object getResult() {
		return new Double(total);
	}

	public Class getType() {
		return Double.class;
	}

	public void operate(Object o) {
		if (o != null && o instanceof Number) {
			total += ((Number) o).doubleValue();
		} else {
			throw new IllegalArgumentException(
			"DoubleSum can only operate on Numbers.");
		}
	}
}


class FloatSum extends AggregateFunction {
	float total;

	public FloatSum() {
		total = 0;
	}

	public Object getResult() {
		return new Float(total);
	}

	public Class getType() {
		return Float.class;
	}

	public void operate(Object o) {
		if (o != null && o instanceof Number) {
			total += ((Number) o).floatValue();
		} else {
			throw new IllegalArgumentException(
			"FloatSum can only operate on Numbers.");
		}
	}
}


class IntegerSum extends AggregateFunction {
	int total;

	public IntegerSum() {
		total = 0;
	}

	public Object getResult() {
		// TODO Auto-generated method stub
		return (new Integer(total));
	}

	public Class getType() {
		return Integer.class;
	}

	public void operate(Object o) {
		if (o != null && o instanceof Number) {
			total += ((Number) o).intValue();
		} else {
			throw new IllegalArgumentException(
			"IntegerSum can only operate on Numbers.");
		}
	}
}