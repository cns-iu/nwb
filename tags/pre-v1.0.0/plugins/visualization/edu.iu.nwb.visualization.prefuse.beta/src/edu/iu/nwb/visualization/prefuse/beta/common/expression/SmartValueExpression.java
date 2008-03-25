package edu.iu.nwb.visualization.prefuse.beta.common.expression;

import prefuse.data.Schema;
import prefuse.data.Tuple;
import prefuse.data.expression.ColumnExpression;

public class SmartValueExpression extends ColumnExpression { //this should make dates act like numbers? strings of dates?
	private Object example;

	public SmartValueExpression(String column, Object example) {
		super(column);
		this.example = example;
	}
	
	public double getDouble(Tuple t) {
		if(super.getType(t.getSchema()) == double.class) {
			return super.getDouble(t);
		}
		//no need for try/catch because of getType
		double value = Double.parseDouble(super.get(t).toString());
		return value;
	}
	
	public Object get(Tuple t) {
		if(this.getType(t.getSchema()) == double.class) {
			return new Double(this.getDouble(t));
		}
		Object object = super.get(t);
		if(object == null) {
			return "";
		} else {
			return object.toString();
		}
	}
	
	public Class getType(Schema s) {
		if(example == null) {
			return String.class;
		}
		try {
			Double.parseDouble(example.toString());
			return double.class;
		} catch(NumberFormatException e) {
			return String.class;
		}
	}
}