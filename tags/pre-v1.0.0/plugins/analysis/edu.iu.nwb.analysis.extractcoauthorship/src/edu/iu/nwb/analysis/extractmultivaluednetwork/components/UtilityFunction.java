package edu.iu.nwb.analysis.extractmultivaluednetwork.components;

public interface UtilityFunction {

	public Object getResult();

	public Class getType();

	public void operate(Object o);

}