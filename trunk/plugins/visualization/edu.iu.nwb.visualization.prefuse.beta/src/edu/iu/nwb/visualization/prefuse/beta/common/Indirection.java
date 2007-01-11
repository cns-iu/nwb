package edu.iu.nwb.visualization.prefuse.beta.common;

import java.util.Random;

import prefuse.Visualization;
import prefuse.data.Tuple;
import prefuse.data.expression.Expression;
import prefuse.data.tuple.TupleSet;

public class Indirection {
	
	private String createdField;
	private Expression expression;
	private TupleSet visGroup;

	public Indirection(Visualization visualization, String group, Expression expression) {
		this.createdField = "_x_nwb" + new Random().nextInt();
		this.expression = expression;
		this.visGroup = visualization.getGroup(group);
		visGroup.addColumn(createdField, expression);
	}
	
	public int getDataType() {
		Tuple example = (Tuple) visGroup.tuples().next();
		Class expressionClass = expression.getType(example.getSchema());
		if(expressionClass.equals(String.class)) {
			return prefuse.Constants.NOMINAL;
		} else { //assume number; this should be made always true with the expression choice
			return prefuse.Constants.NUMERICAL;
		}
	}
	
	public String getField() {
		return createdField;
	}
	
	public static Object getExample(TupleSet tuples, String column) {
		Tuple prototype = (Tuple) tuples.tuples().next();
		return prototype.get(column);
	} 
}