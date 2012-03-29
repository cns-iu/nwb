package edu.iu.nwb.modeling.discretenetworkdynamics.functions;


public class SubtractionFunctionFactory implements FunctionFactory{
	private static final String type = FunctionTypes.SUB;

	public AbstractFunction getFunction(boolean isPoly) {
		if(isPoly)
			return new PolySub();
		return null;
	}

	public String getType() {
		// TODO Auto-generated method stub
		return SubtractionFunctionFactory.type;
	}


}


