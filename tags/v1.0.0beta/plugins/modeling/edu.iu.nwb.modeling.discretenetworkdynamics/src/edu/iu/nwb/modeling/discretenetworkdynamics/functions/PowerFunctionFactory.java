package edu.iu.nwb.modeling.discretenetworkdynamics.functions;


public class PowerFunctionFactory implements FunctionFactory{
	private static final String type = FunctionTypes.POW;
	
	public AbstractFunction getFunction(boolean isPoly) {
		if(isPoly)
			return new PolyPower();
		return null;
	}

	public String getType() {
		// TODO Auto-generated method stub
		return PowerFunctionFactory.type;
	}
	

}

