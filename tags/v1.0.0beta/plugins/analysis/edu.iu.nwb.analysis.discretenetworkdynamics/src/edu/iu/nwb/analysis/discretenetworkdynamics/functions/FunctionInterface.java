package edu.iu.nwb.analysis.discretenetworkdynamics.functions;

import java.math.BigInteger;

public interface FunctionInterface {
	public abstract int getNumberOfArguments();
	public abstract boolean getIsPolynomial();
	public abstract BigInteger operate(BigInteger[] arguments, BigInteger numberOfStates);
}
