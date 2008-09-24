package edu.iu.nwb.analysis.discretenetworkdynamics.parser;

public class FunctionFormatException extends Exception{

	public FunctionFormatException(){
		super("Your function is malformed.");
	}
	
	public FunctionFormatException(String message){
		super(message);
	}
	
	public FunctionFormatException(Exception ex){
		super(ex);
	}
	
}
