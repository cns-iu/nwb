package edu.iu.nwb.modeling.discretenetworkdynamics.functions;

import java.math.BigInteger;


public abstract class AdditionFunction extends AbstractFunction{
	static final private int precedence = 5;
	private static final int numberOfArguments = 2;
	
	public AdditionFunction(){
		super(precedence,0);		
	}

	public int getNumberOfArguments() {
		// TODO Auto-generated method stub
		return AdditionFunction.numberOfArguments;
	}

}


class PolyAdd extends AdditionFunction{
	private final static boolean isPoly = true;
	
	public boolean getIsPolynomial() {
		return PolyAdd.isPoly;
	}

	public BigInteger operate(BigInteger[] arguments, BigInteger numberOfStates) {
		if(arguments.length != super.getNumberOfArguments()){
			return null; //error
		}else{
			
			return (arguments[0].add(arguments[1])).mod(numberOfStates);
		}
	}	

}

class BoolAdd extends AdditionFunction{
	private static boolean isPoly = false;
	
	public boolean getIsPolynomial() {
		return BoolAdd.isPoly;
	}
	
	public BigInteger operate(BigInteger[] arguments, BigInteger numberOfStates) {
		if(arguments.length != super.getNumberOfArguments()){
			return null;
		}
		else{

			return (arguments[0].or(arguments[1])).mod(numberOfStates);
		}
	}
	
}