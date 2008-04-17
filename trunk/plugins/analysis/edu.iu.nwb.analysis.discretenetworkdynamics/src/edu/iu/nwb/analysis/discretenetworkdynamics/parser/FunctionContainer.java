package edu.iu.nwb.analysis.discretenetworkdynamics.parser;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;

import edu.iu.nwb.analysis.discretenetworkdynamics.functions.AbstractFunction;


public class FunctionContainer {
	LinkedList parsedExpression;
	
	public FunctionContainer(LinkedList expression){
		this.parsedExpression = expression;
	}
	
	public int evaluate(final int[] stateSpace, BigInteger radix){
		Stack executionStack = new Stack();
		Object token;
		String tokenString;
		AbstractFunction operator;
		for(Iterator it = this.parsedExpression.iterator(); it.hasNext();){
			token = it.next();
			tokenString = token.toString();
		
			if(tokenString.matches(FunctionTokens.variables)){
				int index = new Integer(tokenString.substring(1)).intValue()-1;
				Integer value = new Integer(stateSpace[index]);
				executionStack.push(new BigInteger(value.toString()));
			}
			else if(tokenString.matches(FunctionTokens.literals)){
				executionStack.push(new BigInteger(tokenString));
			}
			else{
				operator = (AbstractFunction)token;
				BigInteger[] operands = new BigInteger[operator.getNumberOfArguments()];
				for(int i = 0; i < operands.length; i++){
					operands[i] = (BigInteger)executionStack.pop();
				}
				executionStack.push(operator.operate(operands, radix));
			}
			
			
		}
	
	
		int returnValue = ((BigInteger)executionStack.pop()).intValue();
	
		
		return returnValue;
	}
	
	
}
