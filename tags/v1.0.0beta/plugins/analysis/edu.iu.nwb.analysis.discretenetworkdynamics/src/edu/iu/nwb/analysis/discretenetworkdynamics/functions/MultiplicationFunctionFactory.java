package edu.iu.nwb.analysis.discretenetworkdynamics.functions;


public class MultiplicationFunctionFactory implements FunctionFactory{
	private static final String type = FunctionTypes.MUL;
	
	public AbstractFunction getFunction(boolean isPoly) {
		// TODO Auto-generated method stub
		if(isPoly){
			return new PolyMultiplication();
		}else{
			return new BoolMultiplication();
		}
	}

	public String getType() {
		// TODO Auto-generated method stub
		return MultiplicationFunctionFactory.type;
	}

}

