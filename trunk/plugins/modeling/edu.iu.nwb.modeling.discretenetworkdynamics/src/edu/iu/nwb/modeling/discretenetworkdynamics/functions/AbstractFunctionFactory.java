package edu.iu.nwb.modeling.discretenetworkdynamics.functions;

import java.util.HashMap;

public class AbstractFunctionFactory {
	private HashMap<String,FunctionFactory> typeToFunctionFactory;
	
	public AbstractFunctionFactory(){
		typeToFunctionFactory = new HashMap<String,FunctionFactory>();
	}
	
	public static AbstractFunctionFactory getDefaultFunctionFactory(){
		AbstractFunctionFactory aff = new AbstractFunctionFactory();
		
		aff.typeToFunctionFactory.put(FunctionTypes.ADD, new AdditionFunctionFactory());
		aff.typeToFunctionFactory.put(FunctionTypes.DIV, new DivisionFunctionFactory());
		aff.typeToFunctionFactory.put(FunctionTypes.POW, new PowerFunctionFactory());
		aff.typeToFunctionFactory.put(FunctionTypes.SUB, new SubtractionFunctionFactory());
		aff.typeToFunctionFactory.put(FunctionTypes.NEG, new NegationFunctionFactory());
		aff.typeToFunctionFactory.put(FunctionTypes.MUL, new MultiplicationFunctionFactory());
		
		return aff;
	}
	
	public AbstractFunction getFunction(String type, boolean isPolynomial){
		FunctionFactory functFact = (FunctionFactory)this.typeToFunctionFactory.get(type);
		
		return (AbstractFunction)functFact.getFunction(isPolynomial);
	}
	
}
