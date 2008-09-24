package edu.iu.nwb.modeling.discretenetworkdynamics.functions;


public class NegationFunctionFactory implements FunctionFactory{
	private static final String type = FunctionTypes.NEG;

	public AbstractFunction getFunction(boolean isPoly) {
		// TODO Auto-generated method stub
		if(isPoly)
			return new PolyNegation();
		else
			return new BoolNegation();
	}

	public String getType() {
		// TODO Auto-generated method stub
		return NegationFunctionFactory.type;
	}

}

