package edu.iu.epic.modelbuilder.gui.transition;

import edu.iu.epic.modeling.compartment.model.InfectionTransition;

public class ComplexTransitionInfectionInformation {
	private String infectorCompartmentName;
	private String transitionRatio;
	private InfectionTransition inMemoryTransition;
	
	public ComplexTransitionInfectionInformation(String infectorCompartmentName,
												 String transitionRatio, 
												 InfectionTransition inMemoryTransition) {
		this.infectorCompartmentName = infectorCompartmentName;
		this.transitionRatio = transitionRatio;
		this.inMemoryTransition = inMemoryTransition;
	}

	/**
	 * @return the infectorCompartmentName
	 */
	public String getInfectorCompartmentName() {
		return infectorCompartmentName;
	}

	/**
	 * @return the transitionRatio
	 */
	public String getTransitionRatio() {
		return transitionRatio;
	}
	
	/**
	 * @return the transitionRatio
	 */
	public InfectionTransition getInMemoryInfectionTransition() {
		return inMemoryTransition;
	}

	
}
