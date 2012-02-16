package edu.iu.nwb.converter.postscript.gs.ghostscript;

public class GhostscriptException extends Exception {
	private static final long serialVersionUID = -6838432223119343116L;

	public GhostscriptException(String message) {
		super(message);
	}
	
	public GhostscriptException(String message, Throwable cause) {
		super(message, cause);
	}
}