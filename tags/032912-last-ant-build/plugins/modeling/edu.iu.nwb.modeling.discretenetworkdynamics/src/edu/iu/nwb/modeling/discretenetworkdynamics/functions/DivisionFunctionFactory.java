package edu.iu.nwb.modeling.discretenetworkdynamics.functions;


public class DivisionFunctionFactory implements FunctionFactory{
	private static final String type = FunctionTypes.DIV;

	public AbstractFunction getFunction(boolean isPoly) {
		if(isPoly)
			return new PolyDivision();
		return null;
	}

	public String getType() {
		
		return DivisionFunctionFactory.type;
	}
	

}

