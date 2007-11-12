package edu.iu.nwb.analysis.extractmultivaluednetwork.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ValueAttributes {
	int rowNumber;
	Map functions;
	
	public ValueAttributes(int rn){
		rowNumber = rn;
		functions = new HashMap();
	}
	
	public int getRowNumber(){
		return rowNumber;
	}
	
	public void addFunction(int columnNumber, UtilityFunction uf){
		
		functions.put(new Integer(columnNumber), uf);
	}
	
	public ArrayList getFunctions(){
		return new ArrayList(this.functions.values());
	}
	
	public UtilityFunction getFunction(int i){
		return (UtilityFunction)this.functions.get(new Integer(i));
	}
	
}
