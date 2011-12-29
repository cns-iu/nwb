package edu.iu.epic.modelbuilder.gui.utility;

public class IDGenerator {
	private int genericCounter;
	private int compartmentCounter;
	private int simpleTransitionCounter;
	private int parameterCounter;
	private int complexTransitionCounter;

	public IDGenerator() {
		this.genericCounter = 0;
		this.compartmentCounter = 0;
		this.simpleTransitionCounter = 0;
		this.parameterCounter = 0;
		this.complexTransitionCounter = 0;
	}
	
	/**
	 * @return the genericCounter
	 */
	public String getGenericCounter() {
		genericCounter++;
		return "GENERIC_" + genericCounter;
	}

	/**
	 * @return the compartmentCounter
	 */
	public String getCompartmentCounter() {
		compartmentCounter++;
		return "COMPARTMENT_" + compartmentCounter;
	}

	/**
	 * @return the simpleTransitionCounter
	 */
	public String getSimpleTransitionCounter() {
		simpleTransitionCounter++;
		return "SIMPLE_TRANSITION_" + simpleTransitionCounter;
	}

	/**
	 * @return the parameterCounter
	 */
	public String getParameterCounter() {
		parameterCounter++;
		return "new_parameter_" + parameterCounter;
	}

	/**
	 * @return the complexTransitionCounter
	 */
	public String getComplexTransitionCounter() {
		complexTransitionCounter++;
		return "COMPLEX_TRANSITION_" + complexTransitionCounter;
	}
	

}
