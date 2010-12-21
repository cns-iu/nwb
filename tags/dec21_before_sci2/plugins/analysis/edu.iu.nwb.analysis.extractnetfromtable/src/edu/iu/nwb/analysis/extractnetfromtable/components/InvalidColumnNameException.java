package edu.iu.nwb.analysis.extractnetfromtable.components;

public class InvalidColumnNameException extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidColumnNameException(String message){
		super(message);
	}
	
	public InvalidColumnNameException(Exception ex){
		super(ex);
	}

}
