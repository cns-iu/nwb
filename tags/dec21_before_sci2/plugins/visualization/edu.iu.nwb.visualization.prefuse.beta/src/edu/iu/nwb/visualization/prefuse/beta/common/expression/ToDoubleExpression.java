package edu.iu.nwb.visualization.prefuse.beta.common.expression;

import edu.iu.nwb.visualization.prefuse.beta.common.Constants;
import prefuse.data.DataTypeException;
import prefuse.data.Schema;
import prefuse.data.Tuple;
import prefuse.data.expression.ColumnExpression;

public class ToDoubleExpression extends ColumnExpression {
	public ToDoubleExpression(String column) {
		super(column);
	}

	public double getDouble(Tuple t) {
		if(super.getType(t.getSchema()) == double.class) {
			try {
				return super.getDouble(t);
			} catch(DataTypeException exception) {
				return Constants.DEFAULT_EXPRESSION_NUMBER;
			}
		}
		double value;
		try {
			Object valueObject = super.get(t);
			if(valueObject == null) {
				value = Constants.DEFAULT_EXPRESSION_NUMBER;
			} else {
				value = Double.parseDouble(valueObject.toString());
			}
		} catch(NumberFormatException exception) {
			value = Constants.DEFAULT_EXPRESSION_NUMBER;
		}
		return value;
	}
	
	public Object get(Tuple t) {
		return new Double(getDouble(t));
	}
	
	public Class getType(Schema s) {
		return double.class;
	}
}