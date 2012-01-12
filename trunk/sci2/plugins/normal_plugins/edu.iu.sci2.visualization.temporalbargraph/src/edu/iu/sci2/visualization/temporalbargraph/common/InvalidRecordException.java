package edu.iu.sci2.visualization.temporalbargraph.common;

public class InvalidRecordException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public InvalidRecordException() {
		super();
	}

	public InvalidRecordException(String arg0) {
		super(arg0);
	}

	public InvalidRecordException(Throwable arg0) {
		super(arg0);
	}

	public InvalidRecordException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}
}