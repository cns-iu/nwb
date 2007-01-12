package edu.iu.nwb.visualization.prefuse.beta.common;

import java.util.Random;

import prefuse.Visualization;
import prefuse.data.Tuple;
import prefuse.data.expression.Expression;
import prefuse.data.tuple.TupleSet;
import prefuse.util.ColorLib;
import prefuse.util.DataLib;

public class Indirection {
	
	private String createdField;
	private Expression expression;
	private TupleSet visGroup;
	private int dataType = -1;

	public Indirection(Visualization visualization, String group, Expression expression) {
		this.createdField = "_x_nwb" + new Random().nextInt();
		this.expression = expression;
		this.visGroup = visualization.getGroup(group);
		visGroup.addColumn(createdField, expression);
	}
	
	public int getDataType() {
		if(dataType == -1) {
			Tuple example = (Tuple) visGroup.tuples().next();
			Class expressionClass = expression.getType(example.getSchema());
			if(expressionClass.equals(String.class)) {
				dataType = prefuse.Constants.NOMINAL;
			} else { //assume number; this should be made always true with the expression choice
				dataType = prefuse.Constants.NUMERICAL;
			}
		}
		return dataType;
	}
	
	public String getField() {
		return createdField;
	}
	
	public static Object getExample(TupleSet tuples, String column) {
		Tuple prototype = (Tuple) tuples.tuples().next();
		if(prototype.getSchema().getColumnIndex(column) != -1) {
			return prototype.get(column);
		} else {
			return null;
		}
	}

	public int[] getPalette() {
		int type = getDataType();
		if(type == prefuse.Constants.NUMERICAL) {
			return ColorLib.getInterpolatedPalette(ColorLib.rgb(0, 0, 0), ColorLib.rgb(0, 255, 0));
		} else {
			int size = DataLib.ordinalArray(visGroup, createdField).length;
			return ColorLib.getCategoryPalette(size);
		}
	} 
}