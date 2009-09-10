package edu.iu.nwb.analysis.deleteisolates.exceptiontypes;

public class FindNonIsolateNodesException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public FindNonIsolateNodesException() {
		super();
	}

	public FindNonIsolateNodesException(String arg0) {
		super(arg0);
	}

	public FindNonIsolateNodesException(Throwable arg0) {
		super(arg0);
	}

	public FindNonIsolateNodesException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}
}