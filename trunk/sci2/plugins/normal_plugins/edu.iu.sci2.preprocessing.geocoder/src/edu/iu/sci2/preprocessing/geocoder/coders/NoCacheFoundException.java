package edu.iu.sci2.preprocessing.geocoder.coders;

public class NoCacheFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public NoCacheFoundException(String message) {
		super(message);
	}
}
