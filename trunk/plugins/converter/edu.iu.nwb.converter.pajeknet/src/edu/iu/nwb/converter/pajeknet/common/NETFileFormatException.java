package edu.iu.nwb.converter.pajeknet.common;

public class NETFileFormatException extends Exception {
	public NETFileFormatException(String s){
		super(s);
	}

	public NETFileFormatException(Exception e){
		super(e);
	}
}
