package edu.iu.epic.modeling.compartment.model.exceptions;


public class ModelModificationException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public ModelModificationException(String message) {
		super(message);
	}

	public ModelModificationException(String message, Throwable cause) {
		super(message, cause);
	}
}