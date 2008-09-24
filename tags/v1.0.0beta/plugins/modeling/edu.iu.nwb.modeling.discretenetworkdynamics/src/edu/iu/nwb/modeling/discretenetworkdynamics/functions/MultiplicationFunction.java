package edu.iu.nwb.modeling.discretenetworkdynamics.functions;

import java.math.BigInteger;

public abstract class MultiplicationFunction extends AbstractFunction{
	private static final int precedence = 7;
	private static final int numberOfArguments = 2;
	
	public MultiplicationFunction(){
		super(precedence,0);
	}

	public int getNumberOfArguments() {
		// TODO Auto-generated method stub
		return MultiplicationFunction.numberOfArguments;
	}
	
	
}

class PolyMultiplication extends MultiplicationFunction{
	private static final boolean isPoly = true;
	
	public boolean getIsPolynomial() {
		// TODO Auto-generated method stub
		return PolyMultiplication.isPoly;
	}

	
	public BigInteger operate(BigInteger[] arguments, BigInteger numberOfStates) {
		if(arguments.length != super.getNumberOfArguments()){
			return null;
		}else{
			BigInteger val1 = arguments[0].mod(numberOfStates);
			BigInteger val2 = arguments[1].mod(numberOfStates);
			return (val1.multiply(val2)).mod(numberOfStates);
		}
	}
	
}

class BoolMultiplication extends MultiplicationFunction{	
	private static final boolean isPoly = false;
	
	public boolean getIsPolynomial() {
		// TODO Auto-generated method stub
		return BoolMultiplication.isPoly;
	}
	
	public BigInteger operate(BigInteger[] arguments, BigInteger numberOfStates) {
		if(arguments.length != super.getNumberOfArguments()){
			return null;
		}else{
			BigInteger val1 = arguments[0].mod(numberOfStates);
			BigInteger val2 = arguments[1].mod(numberOfStates);
			return (val1.and(val2)).mod(numberOfStates);
		}
	}
}