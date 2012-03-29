package edu.iu.cns.persistence.session.save.exception;

public class NoCanonicalFormatException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public NoCanonicalFormatException() {
		super();
	}

	public NoCanonicalFormatException(String arg0) {
		super(arg0);
	}

	public NoCanonicalFormatException(Throwable arg0) {
		super(arg0);
	}

	public NoCanonicalFormatException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}
}
