package edu.iu.epic.modeling.compartment.model;

public interface Transition {
	boolean involves(Compartment compartment);
	String getRatio();
	void setRatio(String newRatio);
}
