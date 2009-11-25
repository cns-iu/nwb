package edu.iu.epic.modelbuilder.gui;

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
	
//	/**
//	 * @return the nodeCounter
//	 */
//	public static String getNewNodeID(String counterType) {
//		if (GlobalConstants.COMPARTMENT_TYPE_ATTRIBUTE_VALUE
//				.equalsIgnoreCase(counterType)) {
//			compartmentCounter++;
//			return "COMPARTMENT_" + compartmentCounter;
//		} else if (GlobalConstants.SIMPLE_TRANSITION_TYPE_ATTRIBUTE_VALUE
//				.equalsIgnoreCase(counterType)) {
//			simpleTransitionCounter++;
//			return "SIMPLE_TRANSITION_" + simpleTransitionCounter;
//		} else if (GlobalConstants.COMPLEX_TRANSITION_TYPE_ATTRIBUTE_VALUE
//				.equalsIgnoreCase(counterType)) {
//			complexTransitionCounter++;
//			return "COMPLEX_TRANSITION_" + complexTransitionCounter;
//		} else if (GlobalConstants.PARAMETER_ATTRIBUTE_VALUE
//				.equalsIgnoreCase(counterType)) {
//			parameterCounter++;
//			return "new_parameter_" + parameterCounter;
//		} else {
//			genericCounter++;
//			return "GENERIC_" + genericCounter;
//		}
//		
//	}

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
