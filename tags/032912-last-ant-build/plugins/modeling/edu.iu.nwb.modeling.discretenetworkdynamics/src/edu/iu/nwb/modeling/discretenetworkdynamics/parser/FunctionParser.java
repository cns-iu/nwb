package edu.iu.nwb.modeling.discretenetworkdynamics.parser;

import java.util.EmptyStackException;
import java.util.LinkedList;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.iu.nwb.modeling.discretenetworkdynamics.functions.AbstractFunction;
import edu.iu.nwb.modeling.discretenetworkdynamics.functions.AbstractFunctionFactory;

public class FunctionParser {
	public static FunctionContainer parseFunction(String functionString,boolean isPolynomialFunction)throws FunctionFormatException{
		Stack functionStack = new Stack<String>();
		LinkedList outputQueue = new LinkedList<String>();
		String token;

		AbstractFunctionFactory aff = AbstractFunctionFactory.getDefaultFunctionFactory();

		Pattern tokenPattern = Pattern.compile(FunctionTokens.tokens);
		Matcher tokenMatcher = tokenPattern.matcher(functionString);

		while(tokenMatcher.find()){
			token = tokenMatcher.group();

			if(token.matches(FunctionTokens.variables)){
				outputQueue.add(token);
			}
			else if(token.matches(FunctionTokens.literals)){
				if(!isPolynomialFunction){
					int checkValue = new Integer(token).intValue();
					if(checkValue > 1 || checkValue < 0){
						throw new FunctionFormatException("The value: " + checkValue + " is not recognized for Boolean expressions.\n");
					}
				}
				outputQueue.add(token);
			}
			else if(token.matches(FunctionTokens.operators)){
				AbstractFunction operator1 = aff.getFunction(token, isPolynomialFunction);
				try{
				int associativity = operator1.getAssociativity();
				
				AbstractFunction operator2;
					try{
						
					while(functionStack.peek() instanceof AbstractFunction){
						operator2 = (AbstractFunction)functionStack.peek();
						int precedence = operator1.comparePrecedence(operator2);
						if(associativity == 0 || (associativity < 0 && precedence <= 0) || (associativity > 0 && precedence < 0)){
							outputQueue.add(operator2);
							functionStack.pop();
						}else{
							break;
						}
					}
					}catch(EmptyStackException ese){
						
					}
				
				functionStack.push(operator1);	
				}catch(NullPointerException npe){
					String message = "Unknown operator " + token+ " found in expression: " + functionString;
					if(!isPolynomialFunction)
						message += ".  Most likely, this operator is not defined for Boolean expressions.\n";
					throw new FunctionFormatException(message);
				}
			}
			else if(token.matches(FunctionTokens.parenthesis)){
				
				if(token.matches("\\(")){
					functionStack.push(token);
				}
				else{
					while(!functionStack.peek().toString().matches("\\(")){
						try{
							outputQueue.add(functionStack.pop());
						}catch(NullPointerException npe){
							throw new FunctionFormatException("Mismatched Parenthesis.");
						}
					}
					functionStack.pop();
				}
			}else{
				throw new FunctionFormatException("Unrecognized symbol " + token + " found in your expression.");
			}
		}

		while(!functionStack.isEmpty()){
			Object functionObject = functionStack.pop();
			if(functionObject.toString().matches(FunctionTokens.parenthesis))
				throw new FunctionFormatException("Mismatched Parenthesis");
			outputQueue.add(functionObject);
		}

		return new FunctionContainer(outputQueue);
	}



}
