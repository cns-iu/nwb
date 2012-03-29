package edu.iu.nwb.modeling.discretenetworkdynamics.functions;

import java.math.BigInteger;


public abstract class DivisionFunction extends AbstractFunction{
	private static final int precedence = 7;
	private static final int numberOfArguments = 2;

	public DivisionFunction(){
		super(precedence,-1);
	}
	
	public int getNumberOfArguments() {
		// TODO Auto-generated method stub
		return DivisionFunction.numberOfArguments;
	}

}


class PolyDivision extends DivisionFunction {
	private static final boolean isPoly = true;

	public boolean getIsPolynomial() {
		
		return PolyDivision.isPoly;
	}

	
	public BigInteger operate(BigInteger[] arguments, BigInteger numberOfStates) {
		if(arguments.length != super.getNumberOfArguments()){
			return null;
		}else{
			BigInteger val1 = arguments[0].mod(numberOfStates);
			BigInteger val2 = arguments[1].mod(numberOfStates);
			return (val1.multiply(val2.modInverse(numberOfStates)).mod(numberOfStates));
		}
	}
	
}