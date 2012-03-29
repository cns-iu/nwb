package edu.iu.cns.visualization.gui;

public interface InputComponent<V, C> {
	// TODO: Kill C and P everywhere.
	public V getValue();
	public void setValue(V value);
	public boolean validate(V value);
}