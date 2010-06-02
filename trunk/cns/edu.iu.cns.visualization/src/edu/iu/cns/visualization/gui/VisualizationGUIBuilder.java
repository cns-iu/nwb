package edu.iu.cns.visualization.gui;

import java.util.Collection;

import edu.iu.cns.visualization.parameter.VisualizationParameter;

public interface VisualizationGUIBuilder<C, P extends C> {
	public <V> VisualizationParameter<V> getParameter(String name);
	public <V> void addParameter(
			String name, String label, String description);
	public <V> void addParameter(
			String name, String label, String description, V defaultValue);
	public <V> void addParameter(
			String name, String label, String description, InputComponent<V, C> inputComponent);
	public <V> void addParameter(
			String name,
			String label,
			String description,
			V defaultValue,
			InputComponent<V, C> inputComponent);
	public P constructGUI(Collection<VisualizationParameter<?>> parameters);
}