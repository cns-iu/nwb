package edu.iu.cns.database.load.framework.exception;

public class InvalidDerbyFieldTypeException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public InvalidDerbyFieldTypeException() {
		super();
	}

	public InvalidDerbyFieldTypeException(String arg0) {
		super(arg0);
	}

	public InvalidDerbyFieldTypeException(Throwable arg0) {
		super(arg0);
	}

	public InvalidDerbyFieldTypeException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}
}
