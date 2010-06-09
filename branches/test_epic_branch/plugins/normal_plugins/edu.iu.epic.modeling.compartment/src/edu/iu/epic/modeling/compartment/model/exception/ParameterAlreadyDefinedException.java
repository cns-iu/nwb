package edu.iu.epic.modeling.compartment.model.exception;

public class ParameterAlreadyDefinedException extends ModelModificationException {
	private static final long serialVersionUID = 1L;

	public ParameterAlreadyDefinedException(String name) {
		super("The parameter '" + name + "' already has a definition.");
	}
}

