package edu.iu.nwb.modeling.discretenetworkdynamics.functions;

import java.math.BigInteger;

public abstract class NegationFunction extends AbstractFunction{
	private static final int precedence = 9;
	private static final int numberOfArguments = 1;
	
	public NegationFunction(){
		super(precedence,1);
	}

	public int getNumberOfArguments() {
		// TODO Auto-generated method stub
		return NegationFunction.numberOfArguments;
	}

}

class PolyNegation extends NegationFunction{
	private static final boolean isPoly = true;
	
	public boolean getIsPolynomial() {
		// TODO Auto-generated method stub
		return PolyNegation.isPoly;
	}
	
	public BigInteger operate(BigInteger[] arguments, BigInteger numberOfStates) {
		if(arguments.length != super.getNumberOfArguments()){
			return null;
		}else{
			
			BigInteger value = arguments[0];
			
				value = value.negate();
				value = value.add(numberOfStates);
				value = value.mod(numberOfStates);
				
				return value;
			
		}
	}
	
}

class BoolNegation extends NegationFunction{
	private static final boolean isPoly = false;
	
	public boolean getIsPolynomial() {
		// TODO Auto-generated method stub
		return BoolNegation.isPoly;
	}

	
	public BigInteger operate(BigInteger[] arguments, BigInteger numberOfStates) {
		if(arguments.length != super.getNumberOfArguments()){
		return null;
		}else{
			if(arguments[0].compareTo(BigInteger.ZERO) == 0)
				return BigInteger.ONE;
			else{
				return BigInteger.ZERO;
			}
		}
	}
	
}