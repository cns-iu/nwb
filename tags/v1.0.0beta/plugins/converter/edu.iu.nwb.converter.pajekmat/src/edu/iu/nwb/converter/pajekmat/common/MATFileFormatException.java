package edu.iu.nwb.converter.pajekmat.common;

public class MATFileFormatException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MATFileFormatException(String s){
		super(s);
	}
	
	public MATFileFormatException(Exception ex){
		super(ex);
	}

}
