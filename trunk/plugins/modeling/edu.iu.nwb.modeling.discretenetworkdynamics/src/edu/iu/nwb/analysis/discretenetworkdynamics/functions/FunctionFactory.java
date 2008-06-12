package edu.iu.nwb.analysis.discretenetworkdynamics.functions;

public interface FunctionFactory {
	public AbstractFunction getFunction(boolean isPoly);
	public String getType();
}
