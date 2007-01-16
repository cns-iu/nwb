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
		this.createdField = "_x_nwb" + new Random().nextInt(); //we need a random element to prevent duplication; this should be random enough given at most four numbers are needed, and this will draw from the entire range of integers 
		this.expression = expression;
		this.visGroup = visualization.getGroup(group); //get the actual group, not just the name
		visGroup.addColumn(createdField, expression); //stick in the needed additional column
	}
	
	public int getDataType() { //should we treat it like a number or like nominal/ordinal values?
		if(dataType == -1) { //we haven't set datatype yet
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
	
	//helper method to make an example from a tupleset and a column. Uses the value of that column on the first tuple, if the column exists, otherwise null
	public static Object getExample(TupleSet tuples, String column) {
		Tuple prototype = (Tuple) tuples.tuples().next();
		if(prototype.getSchema().getColumnIndex(column) != -1) {
			return prototype.get(column);
		} else {
			return null;
		}
	}

	public int[] getPalette() { //make an appropriate color palette for expressions that use color, either interpolated or just for categories
		int type = getDataType();
		if(type == prefuse.Constants.NUMERICAL) {
			return ColorLib.getInterpolatedPalette(ColorLib.rgb(0, 0, 0), ColorLib.rgb(0, 255, 0));
		} else {
			int size = DataLib.ordinalArray(visGroup, createdField).length;
			return ColorLib.getCategoryPalette(size);
		}
	} 
}