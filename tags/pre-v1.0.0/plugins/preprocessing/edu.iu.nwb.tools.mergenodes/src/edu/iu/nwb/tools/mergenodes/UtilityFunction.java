package edu.iu.nwb.tools.mergenodes;

public interface UtilityFunction {

	public Object getResult();

	public Class getType();

	public Object operate(Object v1, Object v2);

}