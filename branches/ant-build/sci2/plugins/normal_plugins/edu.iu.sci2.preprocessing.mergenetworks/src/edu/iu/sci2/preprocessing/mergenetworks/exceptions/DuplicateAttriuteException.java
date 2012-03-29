package edu.iu.sci2.preprocessing.mergenetworks.exceptions;

@SuppressWarnings("serial")
public class DuplicateAttriuteException extends Exception {
	
	public DuplicateAttriuteException(String message) {
		super(message);
	}
	
	public DuplicateAttriuteException(Exception cause) {
		super(createMessage(cause), cause);
	}

	private static String createMessage(Exception cause) {
		return "Input attribute already exists " + cause.getMessage();
	}

}
