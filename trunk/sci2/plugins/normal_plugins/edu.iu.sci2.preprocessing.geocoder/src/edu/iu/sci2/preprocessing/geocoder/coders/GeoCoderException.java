package edu.iu.sci2.preprocessing.geocoder.coders;

public class GeoCoderException extends Exception {
	private static final long serialVersionUID = -254511726416995636L;

	public GeoCoderException(String message) {
		super(message);
	}
	
	public GeoCoderException(String message, Exception e) {
		super(message, e);
	}
}
