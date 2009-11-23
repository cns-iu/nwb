package edu.iu.epic.modeling.compartment.model;

public interface Transition {
	boolean involves(Compartment compartment);
	String getRatio();
	boolean setRatio(String newRatio);
	Compartment getSource();
	Compartment getTarget();
}
