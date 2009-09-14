package edu.iu.nwb.preprocessing.deleteisolates.exceptions;

public class IsolateNodeStrippingException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public static final String DEFAULT_MESSAGE =
		"Failed to write NWB file with isolates deleted.";
	
	public IsolateNodeStrippingException() {
		this(DEFAULT_MESSAGE);
	}

	public IsolateNodeStrippingException(String message) {
		super(message);
	}

	public IsolateNodeStrippingException(Throwable cause) {
		this(DEFAULT_MESSAGE, cause);
	}

	public IsolateNodeStrippingException(String message, Throwable cause) {
		super(message, cause);
	}
}