package edu.iu.nwb.analysis.extractmultivaluednetwork.components;


public interface UtilityFunction {
	
	public Object getResult();
	public void operate(Object o);
	public Class getType();
	
}