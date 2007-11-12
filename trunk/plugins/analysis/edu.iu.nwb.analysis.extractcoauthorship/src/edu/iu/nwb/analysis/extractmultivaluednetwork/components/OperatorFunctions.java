package edu.iu.nwb.analysis.extractmultivaluednetwork.components;


public class OperatorFunctions {
	
	public static int integerResult(UtilityFunction uf){
		return ((Number)uf.getResult()).intValue();
	}
	
	public static double doubleResult(UtilityFunction uf){
		return ((Number)uf.getResult()).doubleValue();
	}
	
	public static float floatResult(UtilityFunction uf){
		return ((Number)uf.getResult()).floatValue();
	}
	
	

}

class DoubleArithmeticMean implements UtilityFunction{
	double total;
	long items;


	public DoubleArithmeticMean(){
		total = 0;
		items = 0;
	}

	public Object getResult() {
		// TODO Auto-generated method stub
		return new Double(total/((double)items));
	}

	public Class getType() {
		// TODO Auto-generated method stub
		return Double.class;
	}

	public void operate(Object o) {
		if(o instanceof Number){
			items += 1;
			total += ((Number)o).doubleValue();
		}else {
			throw new IllegalArgumentException("DoubleArithmeticMean can only operate on Numbers.");
		}

	}
}

class FloatArithmeticMean implements UtilityFunction{
	float total;
	long items;
	
	public FloatArithmeticMean(){
		total = 0;
		items = 0;
	}
	
	public Object getResult() {
		// TODO Auto-generated method stub
		return new Float(total/((float)items));
	}

	public Class getType() {
		// TODO Auto-generated method stub
		return Float.class;
	}

	public void operate(Object o) {
		if(o instanceof Number){
			items += 1;
			total += ((Number)o).floatValue();
		}else{
			throw new IllegalArgumentException("FloatArithmeticMean can only operate on Numbers.");
		}
		
	}
	
}

class Count implements UtilityFunction{
	int total;

	public Count(){
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

	public void operate(Object o) {
		
		if(o instanceof Number){ //reset number value and add one.
			total = ((Number)o).intValue();
			total += 1;
		}else {
			total += 1;
		}
		
	}

}

class IntegerSum implements UtilityFunction {
	int total;
	
	public IntegerSum(){
		total = 0;
	}

	public Object getResult() {
		// TODO Auto-generated method stub
		return (Number)(new Integer(total));
	}

	public Class getType() {
		// TODO Auto-generated method stub
		return Integer.class;
	}

	public void operate(Object o) {
		if(o instanceof Number){
			total += ((Number)o).intValue();
		}
		else{
			throw new IllegalArgumentException("IntegerSum can only operate on Numbers.");
		}
	}
}

class DoubleSum implements UtilityFunction {
	double total;
	
	public DoubleSum(){
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
		if(o instanceof Number){
			total += ((Number)o).doubleValue();
		}
		else{
			throw new IllegalArgumentException("DoubleSum can only operate on Numbers.");
		}

	}

}

class FloatSum implements UtilityFunction {
	float total;
	
	public FloatSum(){
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
		if(o instanceof Number){
			total += ((Number)o).floatValue();
		}
		else{
			throw new IllegalArgumentException("FloatSum can only operate on Numbers.");
		}
		
	}
	
}

class DoubleMax implements UtilityFunction{
	double value;
	
	public Object getResult() {
		// TODO Auto-generated method stub
		return new Double(value);
	}

	public Class getType() {
		// TODO Auto-generated method stub
		return Double.class;
	}

	public void operate(Object o) {
		if(o instanceof Number){
			try{
				new Double(value);
			}catch(NullPointerException npe){
				value = ((Number)o).doubleValue();
			}
			if(((Number)o).doubleValue() > value){
				value = ((Number)o).doubleValue();
			}
		}
		
	}
	
}

class FloatMax implements UtilityFunction{
	float value;
	
	public Object getResult() {
		// TODO Auto-generated method stub
		return new Float(value);
	}

	public Class getType() {
		// TODO Auto-generated method stub
		return Float.class;
	}

	public void operate(Object o) {
		if(o instanceof Number){
			try{
				new Float(value);
			}catch(NullPointerException npe){
				value = ((Number)o).floatValue();
			}
			if(((Number)o).doubleValue() > value){
				value = ((Number)o).floatValue();
			}
		}
		
	}
	
}

class IntegerMax implements UtilityFunction{
	int value;
	
	public Object getResult() {
		// TODO Auto-generated method stub
		return new Integer(value);
	}

	public Class getType() {
		// TODO Auto-generated method stub
		return Integer.class;
	}

	public void operate(Object o) {
		if(o instanceof Number){
			try{
				new Double(value);
			}catch(NullPointerException npe){
				value = ((Number)o).intValue();
			}
			if(((Number)o).doubleValue() > value){
				value = ((Number)o).intValue();
			}
		}
		
	}
	
}

class DoubleMin implements UtilityFunction{
	double value;
	
	public Object getResult() {
		// TODO Auto-generated method stub
		return new Double(value);
	}

	public Class getType() {
		// TODO Auto-generated method stub
		return Double.class;
	}

	public void operate(Object o) {
		if(o instanceof Number){
			try{
				new Double(value);
			}catch(NullPointerException npe){
				value = ((Number)o).doubleValue();
			}
			if(((Number)o).doubleValue() < value){
				value = ((Number)o).doubleValue();
			}
		}
		
	}
	
}

class FloatMin implements UtilityFunction{
	float value;
	
	public Object getResult() {
		// TODO Auto-generated method stub
		return new Float(value);
	}

	public Class getType() {
		// TODO Auto-generated method stub
		return Float.class;
	}

	public void operate(Object o) {
		if(o instanceof Number){
			try{
				new Float(value);
			}catch(NullPointerException npe){
				value = ((Number)o).floatValue();
			}
			if(((Number)o).doubleValue() < value){
				value = ((Number)o).floatValue();
			}
		}
		
	}
	
}

class IntegerMin implements UtilityFunction{
	int value;
	
	public Object getResult() {
		// TODO Auto-generated method stub
		return new Integer(value);
	}

	public Class getType() {
		// TODO Auto-generated method stub
		return Integer.class;
	}

	public void operate(Object o) {
		if(o instanceof Number){
			try{
				new Double(value);
			}catch(NullPointerException npe){
				value = ((Number)o).intValue();
			}
			if(((Number)o).doubleValue() < value){
				value = ((Number)o).intValue();
			}
		}
		
	}
	
}

