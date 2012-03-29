package edu.iu.nwb.analysis.extractnetfromtable.aggregate;


public class ArithmeticMeanFunctionFactory implements AggregateFunctionFactory{
	private final static String type = AggregateFunctionNames.ARITHMETICMEAN;

	public AggregateFunction getFunction(Class c){
		if (c.equals(int.class) || c.equals(Integer.class)) {
			return new DoubleArithmeticMean();
		}
		if (c.equals(double.class) || c.equals(Double.class)) {
			return new DoubleArithmeticMean();
		}
		if (c.equals(float.class) || c.equals(Float.class)) {
			return new FloatArithmeticMean();
		}
		return null; //throw some sort of error to let them know that the class is not handled.
	}

	public String getType() {
		return ArithmeticMeanFunctionFactory.type;
	}


}

class DoubleArithmeticMean extends AggregateFunction {
	double total;
	long items;

	public DoubleArithmeticMean() {
		total = 0;
		items = 0;
	}

	public Object getResult() {
		return new Double(total / (items));
	}

	public Class getType() {
		return Double.class;
	}

	public void operate(Object o) {
		if (o != null && o instanceof Number) {
			items += 1;
			total += ((Number) o).doubleValue();
		} else {
			throw new IllegalArgumentException(
				"DoubleArithmeticMean can only operate on Numbers.");
		}
	}
}

class FloatArithmeticMean extends AggregateFunction {
	float total;
	long items;

	public FloatArithmeticMean() {
		total = 0;
		items = 0;
	}

	public Object getResult() {
		return new Float(total / (items));
	}

	public Class getType() {
		return Float.class;
	}

	public void operate(Object o) {
		if (o != null && o instanceof Number) {
			items += 1;
			total += ((Number) o).floatValue();
		} else {
			throw new IllegalArgumentException(
			"FloatArithmeticMean can only operate on Numbers.");
		}
	}
}