package edu.iu.nwb.visualization.prefuse.beta.common.expression;

import edu.iu.nwb.visualization.prefuse.beta.common.Constants;
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
		int value;
		try {
			Object valueObject = super.get(t);
			if(valueObject == null) {
				value = Constants.DEFAULT_EXPRESSION_NUMBER;
			} else {
				value = Integer.parseInt(valueObject.toString());
			}
		} catch(NumberFormatException exception) {
			value = Constants.DEFAULT_EXPRESSION_NUMBER;
		}
		return value;
	}
}