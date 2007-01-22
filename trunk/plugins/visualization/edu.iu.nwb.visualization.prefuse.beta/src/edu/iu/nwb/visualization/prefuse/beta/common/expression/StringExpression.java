package edu.iu.nwb.visualization.prefuse.beta.common.expression;

import prefuse.data.Schema;
import prefuse.data.Tuple;
import prefuse.data.expression.ColumnExpression;

public class StringExpression extends ColumnExpression {
	public StringExpression(String column) {
		super(column);
	}
	
	public Object get(Tuple t) {
		Object object = super.get(t);
		if(object == null) {
			return "";
		} else {
			return object.toString();
		}
	}
	
	public Class getType(Schema s) {
		return String.class;
	}
}
