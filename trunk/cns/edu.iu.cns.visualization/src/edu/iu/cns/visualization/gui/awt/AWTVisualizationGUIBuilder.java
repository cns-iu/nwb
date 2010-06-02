package edu.iu.cns.visualization.gui.awt;

import java.awt.Component;
import java.awt.Container;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import edu.iu.cns.visualization.gui.InputComponent;
import edu.iu.cns.visualization.gui.VisualizationGUIBuilder;
import edu.iu.cns.visualization.parameter.VisualizationParameter;

public class AWTVisualizationGUIBuilder implements VisualizationGUIBuilder<Component, Container> {
	private Map<String, AWTVisualizationParameter<?>> parameters =
		new HashMap<String, AWTVisualizationParameter<?>>();
	private Container parametersContainer = new Container();

	public <V> void addParameter(AWTVisualizationParameter<V> parameter) {
		this.parameters.put(parameter.getName(), parameter);
	}

	public <V> VisualizationParameter<V> getParameter(String name) {
		if (this.parameters.containsKey(name)) {
			return (VisualizationParameter<V>)this.parameters.get(name);
		} else {
			return null;
		}
	}

	public <V> void addParameter(String name, String label, String description) {
		addParameter(name, label, description, null, createDefaultInputComponent(name));
	}

	public <V> void addParameter(String name, String label, String description, V defaultValue) {
		addParameter(name, label, description, defaultValue, createDefaultInputComponent(name));
	}

	public <V> void addParameter(
			String name,
			String label,
			String description,
			InputComponent<V, Component> inputComponent) {
		addParameter(name, label, description, null, inputComponent);
	}

	public <V> void addParameter(
			String name,
			String label,
			String description,
			V defaultValue,
			InputComponent<V, Component> inputComponentCreator) {
		AWTVisualizationParameter<V> parameter = new AWTVisualizationParameter<V>(
			name, label, description, defaultValue, inputComponentCreator);
		addParameter(parameter);
	}

	public Container constructGUI(Collection<VisualizationParameter<?>> parameters) {
		return this.parametersContainer;
	}

	private <S> InputComponent<S, Component> createDefaultInputComponent(String name) {
		return createDefaultInputComponent(name, null);
	}

	private <S> InputComponent<S, Component> createDefaultInputComponent(
			String name, S defaultValue) {
		return null;
	}
}