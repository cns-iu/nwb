package edu.iu.epic.modeling.compartment.model.exception;

public class InvalidCompartmentNameException extends ModelModificationException {
	private static final long serialVersionUID = 1L;

	public InvalidCompartmentNameException(String name) {
		super("Invalid compartment name '" + name + "'");
	}
}
