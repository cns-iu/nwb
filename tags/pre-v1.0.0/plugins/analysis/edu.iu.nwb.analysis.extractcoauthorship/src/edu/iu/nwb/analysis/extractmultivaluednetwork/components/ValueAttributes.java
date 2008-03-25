package edu.iu.nwb.analysis.extractmultivaluednetwork.components;

import java.util.HashMap;
import java.util.Map;

public class ValueAttributes {
	int rowNumber;
	Map functions;

	public ValueAttributes(int rn) {
		rowNumber = rn;
		functions = new HashMap();
	}

	public void addFunction(int columnNumber, UtilityFunction uf) {

		functions.put(new Integer(columnNumber), uf);
	}

	public UtilityFunction getFunction(int i) {
		return (UtilityFunction) functions.get(new Integer(i));
	}

	public int getRowNumber() {
		return rowNumber;
	}

}
