package edu.iu.nwb.converter.pajekmat.common;

public class MATFileFormatException extends Exception{
	
	public MATFileFormatException(String s){
		super(s);
	}
	
	public MATFileFormatException(Exception ex){
		super(ex);
	}

}
