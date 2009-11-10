package edu.iu.epic.modeling.compartment.model.exceptions;

public class CompartmentDoesNotExistException extends ModelModificationException {
	private static final long serialVersionUID = 1L;

	public CompartmentDoesNotExistException(String message) {
		super(message);
	}
}
