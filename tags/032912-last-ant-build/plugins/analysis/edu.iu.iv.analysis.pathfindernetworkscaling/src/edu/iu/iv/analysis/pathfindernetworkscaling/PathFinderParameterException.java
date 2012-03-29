package edu.iu.iv.analysis.pathfindernetworkscaling;

public class PathFinderParameterException extends Exception{
	
	public PathFinderParameterException(String message){
		super(message);
	}
	
	public PathFinderParameterException(Exception ex){
		super(ex);
	}
}
