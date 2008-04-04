package edu.iu.nwb.analysis.discretenetworkdynamics.functions;


public class SubtractionFunctionFactory implements FunctionFactory{
	private static final String type = FunctionTypes.SUB;

	public AbstractFunction getFunction(boolean isPoly) {

		return new PolySub();

	}

	public String getType() {
		// TODO Auto-generated method stub
		return SubtractionFunctionFactory.type;
	}


}


