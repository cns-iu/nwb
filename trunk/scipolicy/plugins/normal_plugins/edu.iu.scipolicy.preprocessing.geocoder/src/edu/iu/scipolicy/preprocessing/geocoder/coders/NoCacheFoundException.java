package edu.iu.scipolicy.preprocessing.geocoder.coders;

public class NoCacheFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	NoCacheFoundException(String message) {
		super(message);
	}
}
