package edu.iu.epic.visualization.linegraph.utilities;

public class StencilException extends Exception {
	private static final long serialVersionUID = 1L;

	public StencilException (String message, Exception e) {
		super(message, e);
	}
	
	public StencilException (Exception e) {
		super(e);
	}
}
