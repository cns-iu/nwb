package edu.iu.nwb.modeling.discretenetworkdynamics.parser;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;

import edu.iu.nwb.modeling.discretenetworkdynamics.functions.AbstractFunction;


public class FunctionContainer {
	LinkedList parsedExpression;
	
	public FunctionContainer(LinkedList expression){
		this.parsedExpression = expression;
	}
	
	public int evaluate(final int[] stateSpace, final int[] nextState, BigInteger radix, boolean isSequential) throws ArithmeticException{
		Stack<BigInteger> executionStack = new Stack<BigInteger>();
		Integer value;
		Object token;
		String tokenString;
		AbstractFunction operator;
		for(Iterator it = this.parsedExpression.iterator(); it.hasNext();){
			token = it.next();
			tokenString = token.toString();
			
			if(tokenString.matches(FunctionTokens.variables)){
				int index = new Integer(tokenString.substring(1)).intValue()-1;
				
				if(!isSequential){
					value = new Integer(stateSpace[index]);
					
				}else{
					value = new Integer(nextState[index]);
					if(value.intValue() < 0){
						value = new Integer(stateSpace[index]);
					}
				}
				
				executionStack.push(new BigInteger(value.toString()));
			}
			else if(tokenString.matches(FunctionTokens.literals)){
				
				executionStack.push(new BigInteger(tokenString));
			}
			else{
				operator = (AbstractFunction)token;
				BigInteger[] operands = new BigInteger[operator.getNumberOfArguments()];
				for(int i = (operands.length-1); i >= 0; i--){
					operands[i] = executionStack.pop();
				}
				
				
				
				executionStack.push(operator.operate(operands, radix));
			}
			
			
		}
	
	
		return executionStack.pop().intValue();
	
		//return returnValue;
	}
	
	
}
