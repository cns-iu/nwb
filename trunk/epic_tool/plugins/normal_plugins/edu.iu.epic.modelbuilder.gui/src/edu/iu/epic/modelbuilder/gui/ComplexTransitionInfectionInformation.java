package edu.iu.epic.modelbuilder.gui;

public class ComplexTransitionInfectionInformation {
	private String infectorCompartmentName;
	private String transitionRatio;
	
	public ComplexTransitionInfectionInformation(String infectorCompartmentName,
												 String transitionRatio) {
		this.infectorCompartmentName = infectorCompartmentName;
		this.transitionRatio = transitionRatio;
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

	
}
