package edu.iu.nwb.modeling.discretenetworkdynamics.functions;

public interface FunctionFactory {
	public AbstractFunction getFunction(boolean isPoly);
	public String getType();
}
