package edu.iu.nwb.visualization.prefuse.beta.common.expression;

import prefuse.data.Tuple;
import prefuse.data.expression.ColumnExpression;

public class ToIntegerExpression extends ColumnExpression {
	ToIntegerExpression(String column) {
		super(column);
	}

	public int getInt(Tuple t) {
		if(super.getType(t.getSchema()) == int.class) {
			return super.getInt(t);
		}
		int value = Integer.parseInt((String) super.get(t));
		return value;
	}
}