package edu.iu.nwb.modeling.discretenetworkdynamics.functions;

import java.math.BigInteger;

public abstract class SubtractionFunction extends AbstractFunction{
	private static final int precedence = 5;
	private static final int numberOfArguments = 2;
	
	public SubtractionFunction(){
		super(precedence,-1);
	}

	public int getNumberOfArguments() {
		// TODO Auto-generated method stub
		return SubtractionFunction.numberOfArguments;
	}
}

class PolySub extends SubtractionFunction{
	private static final boolean isPoly = true;

	public boolean getIsPolynomial() {
		return PolySub.isPoly;
	}

	public BigInteger operate(BigInteger[] arguments, BigInteger numberOfStates) {
		if(arguments.length != super.getNumberOfArguments()){
			return null;
		}else{
			BigInteger val1 = arguments[0].mod(numberOfStates);
			BigInteger val2 = arguments[1].mod(numberOfStates);
			return (val1.subtract(val2)).mod(numberOfStates);
		}
	}
	
}
