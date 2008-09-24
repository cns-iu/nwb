package edu.iu.nwb.analysis.extractnetfromtable.components;

public class InvalidColumnNameException extends Exception{
	
	public InvalidColumnNameException(String message){
		super(message);
	}
	
	public InvalidColumnNameException(Exception ex){
		super(ex);
	}

}
