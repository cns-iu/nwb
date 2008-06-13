package edu.iu.nwb.modeling.discretenetworkdynamics.functions;


public abstract class AbstractFunction implements FunctionInterface{
	private int precedence;
	private int associativity;
	
	
	public AbstractFunction(int precedence, int associativity){
		this.precedence = precedence;
		this.associativity = associativity;
	}
	
	public int comparePrecedence(AbstractFunction af){
		if(this.precedence < af.precedence)
			return -1;
		else if(this.precedence == af.precedence)
			return 0;
		else
			return 1;
	}
	
	public int getAssociativity(){
		return this.associativity;
	}
	
}
