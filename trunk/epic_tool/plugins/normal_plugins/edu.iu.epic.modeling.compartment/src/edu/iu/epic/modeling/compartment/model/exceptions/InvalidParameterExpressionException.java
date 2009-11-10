package edu.iu.epic.modeling.compartment.model.exceptions;


public class InvalidParameterExpressionException extends ModelModificationException {
	private static final long serialVersionUID = 1L;

	public InvalidParameterExpressionException(String parameterExpression) {
		super("'" + parameterExpression + "' is not a valid parameter expression.");
	}

	public InvalidParameterExpressionException(String message, Throwable cause) {
		super(message, cause);
	}
}
