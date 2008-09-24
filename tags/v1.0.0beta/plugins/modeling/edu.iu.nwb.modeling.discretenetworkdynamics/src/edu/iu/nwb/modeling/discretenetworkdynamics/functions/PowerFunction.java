package edu.iu.nwb.modeling.discretenetworkdynamics.functions;

import java.math.BigInteger;

public abstract class PowerFunction extends AbstractFunction{
	private static final int precedence = 9;
	private static final int numberOfArguments = 2;
	
	public PowerFunction(){
		super(precedence,1);
	}

	public int getNumberOfArguments() {
		// TODO Auto-generated method stub
		return PowerFunction.numberOfArguments;
	}
}

class PolyPower extends PowerFunction{
	private static final boolean isPoly = true;
	
	public boolean getIsPolynomial() {
		// TODO Auto-generated method stub
		return PolyPower.isPoly;
	}
	
	public BigInteger operate(BigInteger[] arguments, BigInteger numberOfStates) {
		if(arguments.length != super.getNumberOfArguments()){
			return null;
		}else{
			BigInteger val1 = arguments[0].mod(numberOfStates);
			BigInteger val2 = arguments[1].mod(numberOfStates);
			return (val1.pow(val2.intValue())).mod(numberOfStates);
		}
	}

	
	
}