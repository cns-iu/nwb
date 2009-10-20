package edu.iu.scipolicy.preprocessing.geocoder.coders;

public class GeoCoderException extends RuntimeException {
	GeoCoderException(String message) {
		super(message);
	}
	
	GeoCoderException(String message, Exception e) {
		super(message, e);
	}
}
