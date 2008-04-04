package edu.iu.nwb.analysis.discretenetworkdynamics.functions;


public class PowerFunctionFactory implements FunctionFactory{
	private static final String type = FunctionTypes.POW;
	
	public AbstractFunction getFunction(boolean isPoly) {
		// TODO Auto-generated method stub
		return new PolyPower();
	}

	public String getType() {
		// TODO Auto-generated method stub
		return PowerFunctionFactory.type;
	}
	

}

