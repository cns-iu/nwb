package edu.iu.nwb.util.nwbfile;

public class ParsingException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public ParsingException(Throwable exception) {
		super(exception);
	}
	
	public ParsingException(String message) {
		super(message);
	}
}
