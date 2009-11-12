package edu.iu.epic.modeling.compartment.model.exception;

public class InvalidParameterNameException extends ModelModificationException {
	private static final long serialVersionUID = 1L;

	public InvalidParameterNameException(String parameterName) {
		super("'" + parameterName + "' is not a valid parameter name.");
	}
}
