package edu.iu.nwb.analysis.extractnetfromtable.components;

import java.util.HashMap;
import java.util.Map;

import edu.iu.nwb.analysis.extractnetfromtable.aggregate.AggregateFunction;

public class ValueAttributes {
	int rowNumber;
	Map functions;

	public ValueAttributes(int rn) {
		rowNumber = rn;
		functions = new HashMap();
	}

	public void addFunction(int columnNumber, AggregateFunction af) {
		functions.put(new Integer(columnNumber), af);
	}

	public AggregateFunction getFunction(int i) {
		return (AggregateFunction) functions.get(new Integer(i));
	}

	public int getRowNumber() {
		return rowNumber;
	}
}
