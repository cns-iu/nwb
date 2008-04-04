package edu.iu.nwb.analysis.discretenetworkdynamics.functions;


public class DivisionFunctionFactory implements FunctionFactory{
	private static final String type = FunctionTypes.DIV;

	public AbstractFunction getFunction(boolean isPoly) {
		
		return new PolyDivision();
	}

	public String getType() {
		
		return DivisionFunctionFactory.type;
	}
	

}

