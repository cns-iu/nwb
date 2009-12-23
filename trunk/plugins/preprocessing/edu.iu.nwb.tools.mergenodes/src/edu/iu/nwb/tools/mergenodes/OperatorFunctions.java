package edu.iu.nwb.tools.mergenodes;

import java.util.ArrayList;
import java.util.Collections;

class Count implements UtilityFunction {
	int total;

	public Count() {
		total = 0;
	}

	public Object getResult() {
		// TODO Auto-generated method stub
		return new Integer(total);
	}

	public Class getType() {
		// TODO Auto-generated method stub
		return Integer.class;
	}

	public Object operate(Object v1, Object v2) {
		total = ((Integer)v1).intValue()+ 1;
		return new Integer(total);

	} 

}

/*class DoubleArithmeticMean implements UtilityFunction {
	double total;
	long items;

	public DoubleArithmeticMean() {
		total = 0;
		items = 0;
	}

	public Object getResult() {
		// TODO Auto-generated method stub
		return new Double(total / (items));
	}

	public Class getType() {
		// TODO Auto-generated method stub
		return Double.class;
	}

	public void operate(Object o) {
		if (o instanceof Number && o != null) {
			items += 1;
			total += ((Number) o).doubleValue();
		} else {
			throw new IllegalArgumentException(
					"DoubleArithmeticMean can only operate on Numbers.");
		}

	}
}

class DoubleGeometricMean implements UtilityFunction {
	double value;
	long items;

	public DoubleGeometricMean() {
		items = 0;
		value = 1.0;
	}

	public Object getResult() {
		// TODO Auto-generated method stub
		final double result = Math.pow(value, (1.0 / items));
		return new Double(result);
	}

	public Class getType() {
		// TODO Auto-generated method stub
		return Double.class;
	}

	public void operate(Object o) {
		if (o instanceof Number && o != null) {
			items += 1;
			value *= ((Number) o).doubleValue();
		} else {
			throw new IllegalArgumentException(
					"DoubleGeometricMean can only operate on Numbers.");
		}
	}

}

class DoubleMax implements UtilityFunction {
	double value;

	public DoubleMax() {
		value = Double.MIN_VALUE;
	}

	public Object getResult() {
		// TODO Auto-generated method stub
		return new Double(value);
	}

	public Class getType() {
		// TODO Auto-generated method stub
		return Double.class;
	}

	public void operate(Object o) {
		if (o instanceof Number && o != null) {
			

				if (((Number) o).doubleValue() > value) {
					value = ((Number) o).doubleValue();
				}
			
		}else
			throw new IllegalArgumentException(
			"DoubleMax can only operate on Numbers.");

	}

}

class DoubleMin implements UtilityFunction {
	double value;

	public DoubleMin() {
		value = Double.MAX_VALUE;
	}

	public Object getResult() {
		// TODO Auto-generated method stub
		return new Double(value);
	}

	public Class getType() {
		// TODO Auto-generated method stub
		return Double.class;
	}

	public void operate(Object o) {
		if (o instanceof Number && o != null) {
			
				if (((Number) o).doubleValue() < value) {
					value = ((Number) o).doubleValue();
				}
			
		}else
			throw new IllegalArgumentException(
			"DoubleMin can only operate on Numbers.");

	}

}

class DoubleSum implements UtilityFunction {
	double total;

	public DoubleSum() {
		total = 0;
	}

	public Object getResult() {
		// TODO Auto-generated method stub
		return new Double(total);
	}

	public Class getType() {
		// TODO Auto-generated method stub
		return Double.class;
	}

	public void operate(Object o) {
		// TODO Auto-generated method stub
		if (o instanceof Number && o != null) {
			total += ((Number) o).doubleValue();
		} else {
			throw new IllegalArgumentException(
					"DoubleSum can only operate on Numbers.");
		}

	}

}

class FloatArithmeticMean implements UtilityFunction {
	float total;
	long items;

	public FloatArithmeticMean() {
		total = 0;
		items = 0;
	}

	public Object getResult() {
		// TODO Auto-generated method stub
		return new Float(total / (items));
	}

	public Class getType() {
		// TODO Auto-generated method stub
		return Float.class;
	}

	public void operate(Object o) {
		if (o instanceof Number && o != null) {
			items += 1;
			total += ((Number) o).floatValue();
		} else {
			throw new IllegalArgumentException(
					"FloatArithmeticMean can only operate on Numbers.");
		}

	}

}

class FloatGeometricMean implements UtilityFunction {
	float value;
	long items;

	public FloatGeometricMean(){
		this.value = 1;
		this.items = 0;
	}
	
	public Object getResult() {

		final float result = (float) Math.pow(value, (1 / items));
		return new Float(result);
	}

	public Class getType() {
		// TODO Auto-generated method stub
		return Float.class;
	}

	public void operate(Object o) {
		if (o instanceof Number && o != null) {
			items += 1;
			value *= ((Number) o).floatValue();
		} else {
			throw new IllegalArgumentException(
					"FloatArithmeticMean can only operate on Numbers.");
		}
	}

}

class FloatMax implements UtilityFunction {
	float value;

	public FloatMax() {
		value = Float.MIN_VALUE;
	}

	public Object getResult() {
		// TODO Auto-generated method stub
		return new Float(value);
	}

	public Class getType() {
		// TODO Auto-generated method stub
		return Float.class;
	}

	public void operate(Object o) {
		if (o instanceof Number && o != null) {
			
				if (((Number) o).floatValue() > value) {
					value = ((Number) o).floatValue();
				}
		
		}else
			throw new IllegalArgumentException(
			"FloatMax can only operate on Numbers.");

	}

}

class FloatMin implements UtilityFunction {
	float value;

	public FloatMin() {
		value = Float.MAX_VALUE;
	}

	public Object getResult() {
		// TODO Auto-generated method stub
		return new Float(value);
	}

	public Class getType() {
		// TODO Auto-generated method stub
		return Float.class;
	}

	public void operate(Object o) {
		if (o instanceof Number && o != null) {
			
				if (((Number) o).floatValue() < value) {
					value = ((Number) o).floatValue();
				}
			
		}else
			throw new IllegalArgumentException(
			"FloatMin can only operate on Numbers.");

	}

}

class FloatSum implements UtilityFunction {
	float total;

	public FloatSum() {
		total = 0;
	}

	public Object getResult() {
		// TODO Auto-generated method stub
		return new Float(total);
	}

	public Class getType() {
		// TODO Auto-generated method stub
		return Float.class;
	}

	public void operate(Object o) {
		if (o instanceof Number && o != null) {
			total += ((Number) o).floatValue();
		} else {
			throw new IllegalArgumentException(
					"FloatSum can only operate on Numbers.");
		}

	}

}

class IntegerMax implements UtilityFunction {
	int value;

	public IntegerMax() {
		value = Integer.MIN_VALUE;
	}

	public Object getResult() {
		// TODO Auto-generated method stub
		return new Integer(value);
	}

	public Class getType() {
		// TODO Auto-generated method stub
		return Integer.class;
	}

	public void operate(Object o) {
		if (o instanceof Number && o != null) {
			
				if (((Number) o).intValue() > value) {
					value = ((Number) o).intValue();
				}
		
		}else
			throw new IllegalArgumentException(
			"IntegerMax can only operate on Numbers.");

	}

}

class IntegerMin implements UtilityFunction {
	int value;

	public IntegerMin() {
		value = Integer.MAX_VALUE;
	}

	public Object getResult() {
		// TODO Auto-generated method stub
		return new Integer(value);
	}

	public Class getType() {
		// TODO Auto-generated method stub
		return Integer.class;
	}

	public void operate(Object o) {
		if (o instanceof Number && o != null) {
			
				if (((Number) o).intValue() < value) {
					value = ((Number) o).intValue();
				}
			
		}else
			throw new IllegalArgumentException(
			"IntegerMin can only operate on Numbers.");

	}

}
*/
class IntegerSum implements UtilityFunction {
	int total;

	public IntegerSum() {
		total = 0;
	}

	public Object getResult() {
		// TODO Auto-generated method stub
		return (new Integer(total));
	}

	public Class getType() {
		// TODO Auto-generated method stub
		return Integer.class;
	}

	public Object operate(Object v1, Object v2) {
		if (v1 != null && v2 != null){
			if (v1 instanceof Number && 
				v2 instanceof Number) {
					total += ((Number) v1).intValue()+((Number) v2).intValue();
					return new Integer(total);
			}
			else
				throw new IllegalArgumentException(
				"IntegerSum can only operate on Numbers.");
		} else {
			throw new IllegalArgumentException(
					"IntegerSum can only operate on Numbers.");
		}
	}
}
/*
class Median implements UtilityFunction {

	ArrayList objectList;

	public Median() {
		objectList = new ArrayList();
	}

	public Object getResult() {
		// TODO Auto-generated method stub
		return null;
	}

	public Class getType() {
		// TODO Auto-generated method stub
		return null;
	}

	public void operate(Object o) {
		if (o != null) {
			objectList.add(o);
			Collections.sort(objectList);
		}

	}

}*/

public class OperatorFunctions {
}
