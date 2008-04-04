package edu.iu.nwb.analysis.discretenetworkdynamics.parser;

import java.util.EmptyStackException;
import java.util.LinkedList;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.iu.nwb.analysis.discretenetworkdynamics.functions.AbstractFunction;
import edu.iu.nwb.analysis.discretenetworkdynamics.functions.AbstractFunctionFactory;

public class FunctionParser {
	private static String variables = "x\\d+|\\d+";
	private static String operators = "[-+~\\*/\\^]";
	private static String parenthesis = "[\\(\\)]";
	private static String functionTokens = variables+"|"+operators+"|"+parenthesis;

	public static LinkedList parseFunction(String functionString,boolean isPolynomialFunction)throws FunctionFormatException{
		Stack functionStack = new Stack();
		LinkedList outputQueue = new LinkedList();
		String token;

		AbstractFunctionFactory aff = AbstractFunctionFactory.getDefaultFunctionFactory();

		Pattern tokenPattern = Pattern.compile(functionTokens);
		Matcher tokenMatcher = tokenPattern.matcher(functionString);

		while(tokenMatcher.find()){
			token = tokenMatcher.group();

			System.out.print(token+"\t");
			if(token.matches(variables)){
				outputQueue.add(token);
				System.out.println("variable or number");
			}
			else if(token.matches(operators)){
				System.out.print("operator\t");
				AbstractFunction operator1 = aff.getFunction(token, isPolynomialFunction);
				
				int associativity = operator1.getAssociativity();
				System.out.println(operator1.toString());
				AbstractFunction operator2;
					try{
						
					while(functionStack.peek() instanceof AbstractFunction){
						operator2 = (AbstractFunction)functionStack.peek();
						int precedence = operator1.comparePrecedence(operator2);
						
						if((associativity == 0 || (associativity < 0 && precedence <= 0)) || (associativity > 0 && precedence < 0)){
							outputQueue.add(operator2);
							functionStack.pop();
							System.out.println("\t"+operator2.toString());
						}else{
							break;
						}
					}
					}catch(EmptyStackException ese){
						
					}
				
				
				functionStack.push(operator1);	
			}
			else if(token.matches(parenthesis)){
				System.out.println();
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
			}
		}

		while(!functionStack.isEmpty()){
			Object functionObject = functionStack.pop();
			if(functionObject.toString().matches(FunctionParser.parenthesis))
				throw new FunctionFormatException("Mismatched Parenthesis");
			outputQueue.add(functionObject);
		}


		return outputQueue;
	}



}
