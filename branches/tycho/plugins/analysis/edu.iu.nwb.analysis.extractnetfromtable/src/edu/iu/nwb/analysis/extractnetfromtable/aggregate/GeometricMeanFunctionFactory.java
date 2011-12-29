package edu.iu.nwb.analysis.extractnetfromtable.aggregate;

public class GeometricMeanFunctionFactory implements AggregateFunctionFactory{
	private static final String type = AggregateFunctionNames.GEOMETRICMEAN;


	public AggregateFunction getFunction(Class c) {
		if (c.equals(int.class) || c.equals(Integer.class)) {
			return new DoubleGeometricMean();
		}
		if (c.equals(double.class) || c.equals(Double.class)) {
			return new DoubleGeometricMean();
		}
		if (c.equals(float.class) || c.equals(Float.class)) {
			return new FloatGeometricMean();
		}
		return null; //throw some sort of error to let them know that the class is not handled.

	}

	public String getType() {
		return GeometricMeanFunctionFactory.type;
	}		
}

class DoubleGeometricMean extends AggregateFunction {
	double value;
	long items;

	public DoubleGeometricMean() {
		items = 0;
		value = 1.0;
	}

	public Object getResult() {
		final double result = Math.pow(value, (1.0 / items));
		return new Double(result);
	}

	public Class getType() {
		return Double.class;
	}

	public void operate(Object o) {
		if (o != null && o instanceof Number) {
			items += 1;
			value *= ((Number) o).doubleValue();
		} else {
			throw new IllegalArgumentException("DoubleGeometricMean can only operate on Numbers.");
		}
	}
}

class FloatGeometricMean extends AggregateFunction {
	float value;
	long items;

	public FloatGeometricMean(){
		this.value = 1;
		this.items = 0;
	}

	public Object getResult() {
		final float result = (float) Math.pow(value, (1 / (float) items));
		return new Float(result);
	}

	public Class getType() {
		return Float.class;
	}

	public void operate(Object o) {
		if (o != null && o instanceof Number) {
			items += 1;
			value *= ((Number) o).floatValue();
		} else {
			throw new IllegalArgumentException(
					"FloatArithmeticMean can only operate on Numbers.");
		}
	}
}