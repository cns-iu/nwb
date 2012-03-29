package edu.iu.sci2.preprocessing.zip2district;

public class ZipToDistrictException extends Exception {
	private static final long serialVersionUID = -254511726416995636L;

	public ZipToDistrictException(String message) {
		super(message);
	}
	
	public ZipToDistrictException(String message, Exception e) {
		super(message, e);
	}
}
