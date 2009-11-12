package edu.iu.epic.modeling.compartment.model.exception;

public class CompartmentExistsException extends ModelModificationException {
	private static final long serialVersionUID = 1L;

	public CompartmentExistsException(String name) {
		super("The compartment " + name + " already exists.");
	}
}
