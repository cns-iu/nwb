package edu.iu.nwb.analysis.blondelcommunitydetection.algorithmstages.exceptiontypes;

public class NWBToBINConversionException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public NWBToBINConversionException() {
		super();
	}

	public NWBToBINConversionException(String arg0) {
		super(arg0);
	}

	public NWBToBINConversionException(Throwable arg0) {
		super(arg0);
	}

	public NWBToBINConversionException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}
}