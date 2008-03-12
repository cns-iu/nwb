package edu.iu.nwb.analysis.extractnetfromtable.aggregate;

import java.util.HashMap;
import java.util.Set;


public class AssembleAggregateFunctions {
	private HashMap nameToFunctionFactory;
	
	public AssembleAggregateFunctions(){
		nameToFunctionFactory = new HashMap();
	}
	
	public static AssembleAggregateFunctions defaultAssembly(){
		AssembleAggregateFunctions aaf = new AssembleAggregateFunctions();
		
		aaf.addAggregateFunctionFactory(AggregateFunctionNames.ARITHMETICMEAN, new ArithmeticMeanFunctionFactory());
		aaf.addAggregateFunctionFactory(AggregateFunctionNames.COUNT, new CountFunctionFactory());
		aaf.addAggregateFunctionFactory(AggregateFunctionNames.GEOMETRICMEAN, new GeometricMeanFunctionFactory());
		aaf.addAggregateFunctionFactory(AggregateFunctionNames.MAX, new MaxFunctionFactory());
		aaf.addAggregateFunctionFactory(AggregateFunctionNames.MIN, new MinFunctionFactory());
		aaf.addAggregateFunctionFactory(AggregateFunctionNames.SUM, new SumFunctionFactory());
	
		return aaf;
	}
	
	public AggregateFunction getAggregateFunction(String name, Class type){
		AggregateFunction af = null;
		try{
			af = ((AggregateFunctionFactory)nameToFunctionFactory.get(name)).getFunction(type);
		}catch(NullPointerException npe){
			af = null;
		}
		return af;
	}
	
	public AggregateFunctionFactory addAggregateFunctionFactory(String functionName, AggregateFunctionFactory aff){
		//check to see if the function already exists:
		if(this.nameToFunctionFactory.get(functionName) != null){
			//redefine the function but
			//let the user know that they are 
			//redefining a FunctionFactory, 
			//throw FunctionFactoryDefinitionException
			return null;
		}
		//add the FunctionFactory to the HashMap
		
			this.nameToFunctionFactory.put(functionName, aff);
			return aff;
		
	}
	
	public AggregateFunctionFactory removeAggregateFunctionFactory(String functionName){
		return (AggregateFunctionFactory)this.nameToFunctionFactory.get(functionName);
	}
	
	public Set getFunctionNames(){
		return this.nameToFunctionFactory.keySet();
	}

}
