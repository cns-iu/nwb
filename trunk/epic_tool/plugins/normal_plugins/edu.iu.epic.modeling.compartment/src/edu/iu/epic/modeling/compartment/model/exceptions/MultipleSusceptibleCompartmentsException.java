package edu.iu.epic.modeling.compartment.model.exceptions;

public class MultipleSusceptibleCompartmentsException extends ModelModificationException {
	private static final long serialVersionUID = 1L;
	
	public MultipleSusceptibleCompartmentsException() {
		super("Error: A model should have only one 'susceptible' compartment.");
	}	
}
