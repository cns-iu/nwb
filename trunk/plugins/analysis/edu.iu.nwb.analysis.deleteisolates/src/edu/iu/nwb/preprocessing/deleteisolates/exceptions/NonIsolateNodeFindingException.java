package edu.iu.nwb.preprocessing.deleteisolates.exceptions;

public class NonIsolateNodeFindingException extends Exception {
	private static final long serialVersionUID = 1L;
	public static final String DEFAULT_MESSAGE =
		"Failed to read NWB file from which to delete isolates.";
	
	public NonIsolateNodeFindingException() {
		this(DEFAULT_MESSAGE);
	}

	public NonIsolateNodeFindingException(String message) {
		super(message);
	}

	public NonIsolateNodeFindingException(Throwable cause) {
		this(DEFAULT_MESSAGE, cause);
	}

	public NonIsolateNodeFindingException(String message, Throwable cause) {
		super(message, cause);
	}
}