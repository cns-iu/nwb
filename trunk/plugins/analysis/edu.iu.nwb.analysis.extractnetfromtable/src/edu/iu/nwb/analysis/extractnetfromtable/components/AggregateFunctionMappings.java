package edu.iu.nwb.analysis.extractnetfromtable.components;

import java.util.HashMap;
import java.util.Map;

public class AggregateFunctionMappings {
	private final HashMap metaColumnNameToFunctionMap = new HashMap();
	private final HashMap functionColumnToOriginalColumnMap = new HashMap();
	private final HashMap labelToFunctionMap = new HashMap();
	
	public void addFunctionMapping(String functionValueCol, String originalCol, String functionType){
		this.metaColumnNameToFunctionMap.put(functionValueCol, functionType);
		this.functionColumnToOriginalColumnMap.put(functionValueCol, originalCol);
	}
	
	public ValueAttributes addFunctionRow(Object label, ValueAttributes va){
		
			this.labelToFunctionMap.put(label, va);
			return va;
		
	}
	
	public String getFunctionFromColumnName(String colName){
		return (String) this.metaColumnNameToFunctionMap.get(colName);
	}
	
	public String getOriginalColumnFromFunctionColumn(String colName){
		return (String) this.functionColumnToOriginalColumnMap.get(colName);
	}
	
	public ValueAttributes getFunctionRow(Object label){
		return (ValueAttributes)this.labelToFunctionMap.get(label);
	}
	
	
}
