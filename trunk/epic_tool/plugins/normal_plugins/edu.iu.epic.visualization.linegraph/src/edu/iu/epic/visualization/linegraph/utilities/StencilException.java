package edu.iu.epic.visualization.linegraph.utilities;

public class StencilException extends Exception {

	public StencilException (String message, Exception e) {
		super(message, e);
	}
	
	public StencilException (Exception e) {
		super(e);
	}
}
