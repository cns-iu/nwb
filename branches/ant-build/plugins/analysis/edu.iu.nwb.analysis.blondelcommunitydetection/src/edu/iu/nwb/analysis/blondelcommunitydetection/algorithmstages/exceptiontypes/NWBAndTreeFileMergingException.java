package edu.iu.nwb.analysis.blondelcommunitydetection.algorithmstages.exceptiontypes;

public class NWBAndTreeFileMergingException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public NWBAndTreeFileMergingException() {
		super();
	}

	public NWBAndTreeFileMergingException(String arg0) {
		super(arg0);
	}

	public NWBAndTreeFileMergingException(Throwable arg0) {
		super(arg0);
	}

	public NWBAndTreeFileMergingException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}
}