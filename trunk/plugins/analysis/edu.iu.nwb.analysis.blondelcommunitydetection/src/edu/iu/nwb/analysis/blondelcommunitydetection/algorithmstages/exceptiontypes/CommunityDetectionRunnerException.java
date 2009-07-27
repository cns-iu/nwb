package edu.iu.nwb.analysis.blondelcommunitydetection.algorithmstages.exceptiontypes;

public class CommunityDetectionRunnerException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public CommunityDetectionRunnerException() {
		super();
	}

	public CommunityDetectionRunnerException(String arg0) {
		super(arg0);
	}

	public CommunityDetectionRunnerException(Throwable arg0) {
		super(arg0);
	}

	public CommunityDetectionRunnerException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}
}