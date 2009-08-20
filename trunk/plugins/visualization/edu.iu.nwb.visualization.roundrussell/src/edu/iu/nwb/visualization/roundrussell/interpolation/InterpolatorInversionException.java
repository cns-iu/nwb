package edu.iu.nwb.visualization.roundrussell.interpolation;


public class InterpolatorInversionException extends Exception {
	private static final long serialVersionUID = 1L;

	public InterpolatorInversionException(String message) {
		super(message);
	}
	
	public InterpolatorInversionException(Exception cause) {
		super(createMessage(cause), cause);
	}

	private static String createMessage(Exception cause) {
		return "Could not invert interpolator: " + cause.getMessage();
	}
}
