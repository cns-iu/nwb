package edu.iu.nwb.analysis.discretenetworkdynamics.functions;


public class AdditionFunctionFactory implements FunctionFactory {
	private static final String type = FunctionTypes.ADD;

	public String getType() {
		return AdditionFunctionFactory.type;
	}

	public AbstractFunction getFunction(boolean isPoly) {
		if(isPoly){
			return new PolyAdd();
		}else{
			return new BoolAdd();
		}
	}
}


