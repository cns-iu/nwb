package edu.iu.nwb.visualization.prefuse.beta.common.expression;

import prefuse.data.Schema;
import prefuse.data.Tuple;
import prefuse.data.expression.ColumnExpression;

public class ToDoubleExpression extends ColumnExpression {
	public ToDoubleExpression(String column) {
		super(column);
	}

	public double getDouble(Tuple t) {
		if(super.getType(t.getSchema()) == double.class) {
			return super.getDouble(t);
		}
		double value = Double.parseDouble((String) super.get(t));
		return value;
	}
	public Class getType(Schema s) {
		return double.class;
	}
}